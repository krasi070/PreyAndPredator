import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Hashtable;
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

    private final String WINDOW_TITLE = "Fox and Rabbit Simulation";
    private final String CONTROLS_TAB_TITLE = "Controls";
    private final String ANIMALS_TAB_TITLE = "Animals";
    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final String SPEED_PREFIX = "Speed: ";
    private final String START_BUTTON_TEXT = "Start";
    private final String STOP_BUTTON_TEXT = "Stop";
    private final String STEP_BUTTON_TEXT = "Step";
    private final String RESET_BUTTON_TEXT = "Reset";
    
    private final int SPEED_OFFSET = 100;
    private final int SPEED_SLIDER_MIN_VALUE = 0;
    private final int SPEED_SLIDER_MAX_VALUE = 90;
    private final int SPEED_SLIDER_INIT_VALUE = 63;
    private final int SPEED_SLIDER_TICK_INTERVAL = 9;
    
    private Simulator simulator;
	private Timer simTimer;
    
    private JSplitPane splitPane;
    private JTabbedPane tabbedPane;
    private JPanel controlsPanel, animalsPanel;
    private JLabel stepLabel, populationLabel, speedLabel;
    private JButton pauseButton, stepButton, resetButton;
    private JSlider speedSlider;
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
    	simulator = sim;
    	simTimer = new Timer(SPEED_OFFSET - SPEED_SLIDER_INIT_VALUE, this);
    	
        stats = new FieldStats();
        colors = new LinkedHashMap<Class, Color>();

        setTitle(WINDOW_TITLE);        
        setLocation(100, 50);
        
        fieldView = new FieldView(height, width);
        initComponents();
        addComponents();

        setAnimalColors();
        
        pack();
        showStatus(simulator.getStep(), simulator.getField());
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
    public void showStatus(int step, Field field)
    {
    	// This code doesn't allow the user to close the window while the simulation is running
        /*if(!isVisible()) {
            setVisible(true);
        }*/
            
        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
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

        populationLabel.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }
    
    public void actionPerformed(ActionEvent e) {
    	if (simTimer.isRunning() && isViable(simulator.getField())) {
    		simulator.simulateOneStep();
        	showStatus(simulator.getStep(), simulator.getField());
    	}
    	    	
    	if (e.getSource() == pauseButton) {
			actPauseButton();
		}
		else if (e.getSource() == stepButton) {
			actStepButton();
		}
		else if (e.getSource() == resetButton) {
			actResetButton();
		}
    }
    
    public void stateChanged(ChangeEvent e) {
    	JSlider source = (JSlider)e.getSource();
        if (source.getValueIsAdjusting()) {
        	simTimer.setDelay(SPEED_OFFSET - source.getValue());
        }
    }
    
    private void actPauseButton() {
    	if (!simTimer.isRunning()) {
    		simTimer.start();
    		stepButton.setEnabled(false);
			pauseButton.setText(STOP_BUTTON_TEXT);
    	} 
    	else {
    		simTimer.stop();
    		stepButton.setEnabled(true);
    		pauseButton.setText(START_BUTTON_TEXT);
    	}
	}
	
	private void actStepButton() {
		if (isViable(simulator.getField())) {
			simulator.simulateOneStep();
			showStatus(simulator.getStep(), simulator.getField());
		}
	}
	
	private void actResetButton() {
		if (simTimer.isRunning()) {
			simTimer.stop();
    		stepButton.setEnabled(true);
    		pauseButton.setText(START_BUTTON_TEXT);
		}
		
		simulator.reset();
		showStatus(simulator.getStep(), simulator.getField());		
	}
    
    private void initComponents() {
    	stepLabel = new JLabel(STEP_PREFIX);
        populationLabel = new JLabel(POPULATION_PREFIX);
        speedLabel = new JLabel(SPEED_PREFIX);
    	
		pauseButton = new JButton(START_BUTTON_TEXT);
		pauseButton.addActionListener(this);
		
		stepButton = new JButton(STEP_BUTTON_TEXT);
		stepButton.addActionListener(this);
		
		resetButton = new JButton(RESET_BUTTON_TEXT);
		resetButton.addActionListener(this);
		
		speedSlider = new JSlider(
				JSlider.HORIZONTAL, 
				SPEED_SLIDER_MIN_VALUE,
				SPEED_SLIDER_MAX_VALUE,
				SPEED_SLIDER_INIT_VALUE);
		speedSlider.addChangeListener(this);
		
		controlsPanel = new JPanel();
		animalsPanel = new JPanel();
		tabbedPane = new JTabbedPane();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	}
	
	private void addComponents() {
		speedSlider.setMajorTickSpacing(SPEED_SLIDER_TICK_INTERVAL);
		speedSlider.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(SPEED_SLIDER_MIN_VALUE, new JLabel("Slow"));
		labelTable.put(SPEED_SLIDER_MAX_VALUE, new JLabel("Fast"));
		speedSlider.setLabelTable(labelTable);
		speedSlider.setPaintLabels(true);
		
		controlsPanel.add(pauseButton);
		controlsPanel.add(stepButton);
		controlsPanel.add(resetButton);
		controlsPanel.add(stepLabel);
		controlsPanel.add(populationLabel);
		controlsPanel.add(speedSlider);
		
		tabbedPane.addTab(CONTROLS_TAB_TITLE, controlsPanel);
		tabbedPane.addTab(ANIMALS_TAB_TITLE, animalsPanel);
		tabbedPane.setPreferredSize(fieldView.getPreferredSize());
		
		splitPane.setLeftComponent(fieldView);
		splitPane.setRightComponent(tabbedPane);
		
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
