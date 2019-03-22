import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;

public class ControlsTab extends JPanel {

	public final String TITLE = "Controls";
	
	public JLabel stepLabel, speedLabel, rabbitPopulationLabel, foxPopulationLabel;
    public JButton pauseButton, stepButton, resetButton;
    public JSlider speedSlider;
    public JSpinner stepSpinner;
    public JProgressBar ratioBar;
	
    private final String STEP_PREFIX = "Step: ";
    private final String SPEED_PREFIX = "Speed: ";
    private final String RABBIT_POPULATION_PREFIX = "Rabbits: ";
    private final String FOX_POPULATION_PREFIX = "Foxes: ";
    private final String START_BUTTON_TEXT = "Start";
    private final String STEP_BUTTON_TEXT = "Step";
    private final String RESET_BUTTON_TEXT = "Reset";
    
    private final int MAX_STEP_AMOUNT = 100;
    private final int SPEED_SLIDER_MIN_VALUE = 0;
    private final int SPEED_SLIDER_MAX_VALUE = 150;
    private final int SPEED_SLIDER_INIT_VALUE = 105;
    private final int SPEED_SLIDER_TICK_INTERVAL = 15;
    
    public ControlsTab() {
    	initComponents();
    	layoutComponents(); 
    }
    
    private void initComponents() {
    	stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
    	rabbitPopulationLabel = new JLabel(RABBIT_POPULATION_PREFIX, JLabel.LEFT);
    	foxPopulationLabel = new JLabel(FOX_POPULATION_PREFIX, JLabel.RIGHT);
        speedLabel = new JLabel(SPEED_PREFIX, JLabel.RIGHT);
    	
		pauseButton = new JButton(START_BUTTON_TEXT);
		
		stepButton = new JButton(STEP_BUTTON_TEXT);
		
		stepSpinner = new JSpinner(new SpinnerListModel(getStepStrings()));
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)stepSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		resetButton = new JButton(RESET_BUTTON_TEXT);
		
		speedSlider = new JSlider(
				JSlider.HORIZONTAL, 
				SPEED_SLIDER_MIN_VALUE,
				SPEED_SLIDER_MAX_VALUE,
				SPEED_SLIDER_INIT_VALUE);
		speedSlider.setMajorTickSpacing(SPEED_SLIDER_TICK_INTERVAL);
		speedSlider.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(SPEED_SLIDER_MIN_VALUE, new JLabel("Slow"));
		labelTable.put(SPEED_SLIDER_MAX_VALUE, new JLabel("Fast"));
		speedSlider.setLabelTable(labelTable);
		speedSlider.setPaintLabels(true);
		
		ratioBar = new JProgressBar(0, 100);
		ratioBar.setValue(50);
		ratioBar.setBackground(Color.orange);
		ratioBar.setForeground(Color.blue);
	}
    
	private void layoutComponents() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Speed Label
		constraints.insets = new Insets(10, 30, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		this.add(speedLabel, constraints);
		
		// Speed Slider
		constraints.insets = new Insets(38, 0, 0, 30);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		this.add(speedSlider, constraints);
		
		// Step Button
		constraints.insets = new Insets(30, 30, 0, 0);
		constraints.ipady = 10;
		constraints.weightx = 0.5;
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.gridwidth = 3;
		this.add(stepButton, constraints);
		
		// Step Spinner
		constraints.insets = new Insets(30, 10, 0, 30);
		constraints.ipady = 16;
		constraints.gridx = 3;
		this.add(stepSpinner, constraints);
		
		// Start Button
		constraints.insets = new Insets(30, 30, 0, 0);
		constraints.ipady = 10;
		constraints.gridx = 0;
		constraints.gridy = 2;
		this.add(pauseButton, constraints);
		
		// Reset Button
		constraints.insets = new Insets(30, 10, 0, 30);
		constraints.gridx = 3;
		this.add(resetButton, constraints);
		
		// Rabbit Population Label
		constraints.insets = new Insets(30, 30, 0, 30);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 3;
		this.add(rabbitPopulationLabel, constraints);
		
		// Fox Population Label
		constraints.insets = new Insets(30, 30, 0, 30);
		constraints.gridx = 3;
		constraints.gridwidth = 3;
		this.add(foxPopulationLabel, constraints);
		
		// Ratio Bar
		constraints.insets = new Insets(0, 30, 0, 30);
		constraints.ipady = 25;
		constraints.gridy = 4;
		constraints.gridx = 0;
		constraints.gridwidth = 6;
		this.add(ratioBar, constraints);
		
		// Step Label
		constraints.insets = new Insets(0, 2, 2, 0);
		constraints.ipady = 0;
		constraints.weightx = 0;
		constraints.weighty = 0.5;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 6;
		constraints.anchor = GridBagConstraints.LAST_LINE_START;
		this.add(stepLabel, constraints);
	}
	
	private String[] getStepStrings() {
		String[] allowedSteps = new String[MAX_STEP_AMOUNT];
		for (int i = 1; i <= allowedSteps.length; i++) {
			allowedSteps[i - 1] = i + "";
		}
		
		return allowedSteps;
	}
}