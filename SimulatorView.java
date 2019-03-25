import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class SimulatorView extends JFrame implements ActionListener, ChangeListener
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String WINDOW_TITLE = "Fox and Rabbit Simulation - ";
    private final String NEW_FILE = "New File";
    private final String STEP_PREFIX = "Step: ";
    private final String RABBIT_POPULATION_PREFIX = "Rabbits: ";
    private final String FOX_POPULATION_PREFIX = "Foxes: ";
    private final String START_BUTTON_TEXT = "Start";
    private final String STOP_BUTTON_TEXT = "Stop";
    
    private final int SPEED_OFFSET = 155;
    private final int SPEED_SLIDER_INIT_VALUE = 105;
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    private File saveFile;
    
    private Simulator simulator;
	private Timer simTimer;
    
    private JSplitPane splitPane;
    private JTabbedPane tabbedPane;
    private SimulationMenuBar menu;
    private ControlsTab controlsTab;
    private AnimalsTab animalsTab;
    private FieldView fieldView;
    
    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;
    
    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(Simulator sim, int height, int width)
    {
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
    	fileChooser.setFileFilter(filter);
    	
    	simulator = sim;
    	simTimer = new Timer(SPEED_OFFSET - SPEED_SLIDER_INIT_VALUE, this);
    	
        stats = new FieldStats();
        colors = new LinkedHashMap<Class, Color>();

        setTitle(WINDOW_TITLE + NEW_FILE);        
        setLocation(100, 50);
        
        fieldView = new FieldView(height, width);
        initComponents();
        addComponents();

        setAnimalColors();
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent we) {
        		System.exit(0);
        	} 
        });
        
        pack();
        showStatus();
        setVisible(true);
    }
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color)
    {
        colors.put(animalClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass)
    {
        Color col = colors.get(animalClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus()
    {            
    	controlsTab.stepLabel.setText(STEP_PREFIX + simulator.getStep());
        stats.reset();
        
        fieldView.preparePaint();

        for(int row = 0; row < simulator.getField().getDepth(); row++) {
            for(int col = 0; col < simulator.getField().getWidth(); col++) {
                Object animal = simulator.getField().getObjectAt(row, col);
                if(animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        
        stats.countFinished();

        int rabbitPopulation = stats.getAnimalPopulation(Rabbit.class);
        double ratio = 100.0 * rabbitPopulation / stats.getWholePopulation();
        controlsTab.ratioBar.setValue((int)ratio);
        controlsTab.rabbitPopulationLabel.setText(RABBIT_POPULATION_PREFIX + rabbitPopulation);
        controlsTab.foxPopulationLabel.setText(FOX_POPULATION_PREFIX + stats.getAnimalPopulation(Fox.class));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable()
    {
        return stats.isViable(simulator.getField());
    }
    
    public void actionPerformed(ActionEvent e) {
    	boolean isViable = isViable();
    	if (simTimer.isRunning() && isViable) {
    		simulator.simulateOneStep();
        	showStatus();
    	}
    	
    	if (!isViable) {
    		simTimer.stop();
    		controlsTab.pauseButton.setEnabled(false);
    		controlsTab.pauseButton.setText(START_BUTTON_TEXT);
    	}
    	    	
    	if (e.getSource() == controlsTab.pauseButton) {
			actPauseButton();
		}
		else if (e.getSource() == controlsTab.stepButton) {
			actStepButton();
		}
		else if (e.getSource() == controlsTab.resetButton) {
			actResetButton();
		}
		else if (e.getSource() == menu.openItem) {
			actOpenItem();
		}
		else if (e.getSource() == menu.saveItem) {
			actSaveItem();
		}
		else if (e.getSource() == menu.saveAsItem) {
			actSaveAsItem();
		}
    	
    }
    
    public void stateChanged(ChangeEvent e) {
    	if (e.getSource() == controlsTab.speedSlider) {
        	simTimer.setDelay(SPEED_OFFSET - controlsTab.speedSlider.getValue());
    	}
    	else if (e.getSource() == tabbedPane) {
    		if (tabbedPane.getSelectedIndex() == 1) {
    			animalsTab.setCorrectValuesInSpinners();
    		}
    	}
    	
    }
    
    private void actPauseButton() {
    	if (!simTimer.isRunning()) {
    		simTimer.start();
    		controlsTab.stepButton.setEnabled(false);
    		controlsTab.pauseButton.setText(STOP_BUTTON_TEXT);
    	} 
    	else {
    		simTimer.stop();
    		controlsTab.stepButton.setEnabled(true);
    		controlsTab.pauseButton.setText(START_BUTTON_TEXT);
    	}
	}
	
	private void actStepButton() {
		int stepAmount = Integer.parseInt(controlsTab.stepSpinner.getValue().toString());
		for (int i = 0; i < stepAmount && isViable(); i++) {
			simulator.simulateOneStep();
			UpdateStats();
		}
		
		showStatus();
	}
	
	private void actResetButton() {
		if (simTimer.isRunning()) {
			simTimer.stop();
		}
		
		controlsTab.stepButton.setEnabled(true);
		controlsTab.pauseButton.setEnabled(true);
		controlsTab.pauseButton.setText(START_BUTTON_TEXT);
		
		simulator.reset();
		showStatus();		
	}
    
	private void actSaveItem() {
		if (saveFile == null) {
			actSaveAsItem();
		}
		else {
			FileCommander.SaveSimulation(saveFile, simulator.getField(), simulator.getStep());
		}
	}
	
	private void actSaveAsItem() {
		int returnVal = fileChooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = fileChooser.getSelectedFile();
            FileCommander.SaveSimulation(saveFile, simulator.getField(), simulator.getStep());
            setTitle(WINDOW_TITLE + saveFile.getPath());   
        } 
        else {
            System.out.println("File not saved!");
        }
	}
	
	private void actOpenItem() {
		int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = fileChooser.getSelectedFile();
            simulator.setStep(FileCommander.OpenSimulation(saveFile, simulator.getField()));
            setTitle(WINDOW_TITLE + saveFile.getPath()); 
            simulator.populateFromSave();
            showStatus();
            
            if (simTimer.isRunning()) {
            	actPauseButton();
            }
            
            controlsTab.pauseButton.setText(START_BUTTON_TEXT);
            if (isViable()) {
            	controlsTab.stepButton.setEnabled(true);
    			controlsTab.pauseButton.setEnabled(true);
        	}
            else {
            	controlsTab.pauseButton.setEnabled(false);
            	controlsTab.stepButton.setEnabled(false);
            }
        } 
        else {
        	System.out.println("File not opened!");
        }
	}
	
	private void UpdateStats() {
		stats.reset();

        for(int row = 0; row < simulator.getField().getDepth(); row++) {
            for(int col = 0; col < simulator.getField().getWidth(); col++) {
                Object animal = simulator.getField().getObjectAt(row, col);
                if(animal != null) {
                    stats.incrementCount(animal.getClass());
                }
            }
        }
        
        stats.countFinished();
	}
	
    private void initComponents() {
    	controlsTab = new ControlsTab();
    	controlsTab.pauseButton.addActionListener(this);
    	controlsTab.stepButton.addActionListener(this);
    	controlsTab.resetButton.addActionListener(this);
    	controlsTab.speedSlider.addChangeListener(this);
		
    	menu = new SimulationMenuBar();
    	menu.openItem.addActionListener(this);
    	menu.saveItem.addActionListener(this);
    	menu.saveAsItem.addActionListener(this);
    	
    	animalsTab = new AnimalsTab();
		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(this);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	}
	
	private void addComponents() {		
		tabbedPane.addTab(controlsTab.TITLE, controlsTab);
		tabbedPane.addTab(animalsTab.TITLE, animalsTab);
		Dimension tabbedPaneSize = fieldView.getPreferredSize();
		tabbedPaneSize.width = tabbedPaneSize.width / 4 * 3;
		tabbedPane.setPreferredSize(tabbedPaneSize);
		
		splitPane.setLeftComponent(fieldView);
		splitPane.setRightComponent(tabbedPane);
		
		setJMenuBar(menu);
		add(splitPane);
	}
	
	private void setAnimalColors() {
		setColor(Rabbit.class, Color.blue);
        setColor(Fox.class, Color.orange);
	}
	
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
        	if (size.equals(new Dimension(0, 0))) {
            //if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
