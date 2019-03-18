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
    
    private final int MAX_STEP_AMOUNT = 100;
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
    private JSpinner stepSpinner;
    private JProgressBar ratioBar;
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

        double ratio = 100.0 * stats.getAnimalPopulation(Rabbit.class) / stats.getWholePopulation();
        ratioBar.setValue((int)ratio);
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
			int stepAmount = Integer.parseInt(stepSpinner.getValue().toString());
			for (int i = 0; i < stepAmount; i++) {
				simulator.simulateOneStep();
			}
			
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
    	stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        populationLabel = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        speedLabel = new JLabel(SPEED_PREFIX, JLabel.RIGHT);
    	
		pauseButton = new JButton(START_BUTTON_TEXT);
		pauseButton.addActionListener(this);
		
		stepButton = new JButton(STEP_BUTTON_TEXT);
		stepButton.addActionListener(this);
		
		stepSpinner = new JSpinner(new SpinnerListModel(getStepStrings()));
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)stepSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		resetButton = new JButton(RESET_BUTTON_TEXT);
		resetButton.addActionListener(this);
		
		speedSlider = new JSlider(
				JSlider.HORIZONTAL, 
				SPEED_SLIDER_MIN_VALUE,
				SPEED_SLIDER_MAX_VALUE,
				SPEED_SLIDER_INIT_VALUE);
		speedSlider.addChangeListener(this);
		
		ratioBar = new JProgressBar(0, 100);
		ratioBar.setValue(50);
		ratioBar.setBackground(Color.orange);
		ratioBar.setForeground(Color.blue);
		
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
		
		layoutControlsPanel();
		
		tabbedPane.addTab(CONTROLS_TAB_TITLE, controlsPanel);
		tabbedPane.addTab(ANIMALS_TAB_TITLE, animalsPanel);
		tabbedPane.setPreferredSize(fieldView.getPreferredSize());
		
		splitPane.setLeftComponent(fieldView);
		splitPane.setRightComponent(tabbedPane);
		
		add(splitPane);
	}
	
	private void layoutControlsPanel() {
		controlsPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Speed Label
		constraints.insets = new Insets(40, 30, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		controlsPanel.add(speedLabel, constraints);
		
		// Speed Slider
		constraints.insets = new Insets(68, 0, 0, 30);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		controlsPanel.add(speedSlider, constraints);
		
		// Step Button
		constraints.insets = new Insets(30, 30, 0, 0);
		constraints.ipady = 10;
		constraints.weightx = 0.5;
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.gridwidth = 3;
		controlsPanel.add(stepButton, constraints);
		
		// Step Spinner
		constraints.insets = new Insets(30, 30, 0, 30);
		constraints.gridx = 3;
		controlsPanel.add(stepSpinner, constraints);
		
		// Start Button
		constraints.insets = new Insets(30, 30, 0, 0);
		constraints.ipady = 10;
		constraints.gridx = 0;
		constraints.gridy = 2;
		controlsPanel.add(pauseButton, constraints);
		
		// Reset Button
		constraints.insets = new Insets(30, 30, 0, 30);
		constraints.gridx = 3;
		controlsPanel.add(resetButton, constraints);
		
		// Ratio Bar
		constraints.insets = new Insets(30, 30, 0, 30);
		constraints.ipady = 25;
		constraints.gridy = 3;
		constraints.gridx = 0;
		constraints.gridwidth = 6;
		controlsPanel.add(ratioBar, constraints);
		
		// Step Label
		constraints.insets = new Insets(0, 2, 2, 0);
		constraints.ipady = 0;
		constraints.weightx = 0;
		constraints.weighty = 0.5;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 6;
		constraints.anchor = GridBagConstraints.LAST_LINE_START;
		controlsPanel.add(stepLabel, constraints);
		
		// Population Label
		constraints.weighty = 0;
		constraints.gridy = 5;
		controlsPanel.add(populationLabel, constraints);
	}
	
	private void setAnimalColors() {
		setColor(Rabbit.class, Color.blue);
        setColor(Fox.class, Color.orange);
	}
    
	private String[] getStepStrings() {
		String[] allowedSteps = new String[MAX_STEP_AMOUNT];
		for (int i = 1; i <= allowedSteps.length; i++) {
			allowedSteps[i - 1] = i + "";
		}
		
		return allowedSteps;
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
