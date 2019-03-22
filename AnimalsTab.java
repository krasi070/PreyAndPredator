import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

// Add color picker
public class AnimalsTab extends JPanel implements ActionListener {

	public final String TITLE = "Animals";
	
	private final String RABBIT_TITLE = "Rabbit";
	private final String FOX_TITLE = "Fox";
	private final String MAX_AGE_TEXT = "Maximum age: ";
	private final String BREEDING_AGE_TEXT = "Breeding age: ";
	private final String BREEDING_PROBABILITY_TEXT = "Breeding probability: ";
	private final String MAX_LITTER_SIZE_TEXT = "Maximum litter size: ";
	private final String RABBIT_FOOD_VALUE_TEXT = "Rabbit food value: ";
	private final String APPLY_BUTTON_TEXT = "Apply";
	private final String DEFAULT_BUTTON_TEXT = "Default";
	
	private final int MIN_SPINNER_VALUE = 1;
	private final int MAX_SPINNER_VALUE = 1000;
	
	private JPanel rabbitContainer, foxContainer;
	
	private JLabel rabbitBreedingAgeLabel, rabbitMaxAgeLabel, 
		rabbitBreedingProbabilityLabel, rabbitMaxLitterSizeLabel;
	private JSpinner rabbitBreedingAgeSpinner, rabbitMaxAgeSpinner, 
		rabbitBreedingProbabilitySpinner, rabbitMaxLitterSizeSpinner;
	
	private JLabel foxBreedingAgeLabel, foxMaxAgeLabel, foxBreedingProbabilityLabel, 
		foxMaxLitterSizeLabel, foxFoodValueLabel;
	private JSpinner foxBreedingAgeSpinner, foxMaxAgeSpinner, foxBreedingProbabilitySpinner, 
		foxMaxLitterSizeSpinner, foxFoodValueSpinner;
	
	private JButton rabbitApplyButton, rabbitDefaultButton;
	private JButton foxApplyButton, foxDefaultButton;
	
	public AnimalsTab() {
		initComponents();
		layoutComponents();
	}
	
	public void setCorrectValuesInSpinners() {
		// Rabbit Spinners
		rabbitMaxAgeSpinner.setValue(Integer.toString(Rabbit.maxAge));
		rabbitBreedingAgeSpinner.setValue(Integer.toString(Rabbit.breedingAge));
		rabbitBreedingProbabilitySpinner.setValue((int)(Rabbit.breedingProbability * 100) + "%");
		rabbitMaxLitterSizeSpinner.setValue(Integer.toString(Rabbit.maxLitterSize));
		
		// Fox Spinners
		foxMaxAgeSpinner.setValue(Integer.toString(Fox.maxAge));
		foxBreedingAgeSpinner.setValue(Integer.toString(Fox.breedingAge));
		foxBreedingProbabilitySpinner.setValue((int)(Fox.breedingProbability * 100) + "%");
		foxMaxLitterSizeSpinner.setValue(Integer.toString(Fox.maxLitterSize));
		foxFoodValueSpinner.setValue(Integer.toString(Fox.rabbitFoodValue));
	}
	
	public void actionPerformed(ActionEvent e) {
		// TO DO
	}
	
	private void initComponents() {
		// Rabbit Labels
		rabbitBreedingAgeLabel = new JLabel(BREEDING_AGE_TEXT, JLabel.RIGHT);
		rabbitMaxAgeLabel = new JLabel(MAX_AGE_TEXT, JLabel.RIGHT);
		rabbitBreedingProbabilityLabel = new JLabel(BREEDING_PROBABILITY_TEXT, JLabel.RIGHT);
		rabbitMaxLitterSizeLabel = new JLabel(MAX_LITTER_SIZE_TEXT, JLabel.RIGHT);
		
		// Fox Labels
		foxBreedingAgeLabel = new JLabel(BREEDING_AGE_TEXT, JLabel.RIGHT);
		foxMaxAgeLabel = new JLabel(MAX_AGE_TEXT, JLabel.RIGHT);
		foxBreedingProbabilityLabel = new JLabel(BREEDING_PROBABILITY_TEXT, JLabel.RIGHT);
		foxMaxLitterSizeLabel = new JLabel(MAX_LITTER_SIZE_TEXT, JLabel.RIGHT);
		foxFoodValueLabel = new JLabel(RABBIT_FOOD_VALUE_TEXT, JLabel.RIGHT);
		
		// Spinner values
		String[] spinnerValues = getSpinnerValues(MIN_SPINNER_VALUE, MAX_SPINNER_VALUE, "");
		String[] spinnerPercentages = getSpinnerValues(0, 100, "%");
		JSpinner.DefaultEditor spinnerEditor;
		
		// Rabbit Spinners
		rabbitBreedingAgeSpinner = new JSpinner(new SpinnerListModel(spinnerValues));
		spinnerEditor = (JSpinner.DefaultEditor)rabbitBreedingAgeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		rabbitMaxAgeSpinner = new JSpinner(new SpinnerListModel(spinnerValues));
		spinnerEditor = (JSpinner.DefaultEditor)rabbitMaxAgeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		rabbitBreedingProbabilitySpinner = new JSpinner(new SpinnerListModel(spinnerPercentages));
		spinnerEditor = (JSpinner.DefaultEditor)rabbitBreedingProbabilitySpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		rabbitMaxLitterSizeSpinner = new JSpinner(new SpinnerListModel(spinnerValues));
		spinnerEditor = (JSpinner.DefaultEditor)rabbitMaxLitterSizeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		// Fox Spinners
		foxBreedingAgeSpinner = new JSpinner(new SpinnerListModel(spinnerValues));
		spinnerEditor = (JSpinner.DefaultEditor)foxBreedingAgeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		foxMaxAgeSpinner = new JSpinner(new SpinnerListModel(spinnerValues));
		spinnerEditor = (JSpinner.DefaultEditor)foxMaxAgeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		foxBreedingProbabilitySpinner = new JSpinner(new SpinnerListModel(spinnerPercentages));
		spinnerEditor = (JSpinner.DefaultEditor)foxBreedingProbabilitySpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		foxMaxLitterSizeSpinner = new JSpinner(new SpinnerListModel(spinnerValues));
		spinnerEditor = (JSpinner.DefaultEditor)foxMaxLitterSizeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		foxFoodValueSpinner = new JSpinner(new SpinnerListModel(spinnerValues));
		spinnerEditor = (JSpinner.DefaultEditor)foxFoodValueSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		// Buttons
		rabbitApplyButton = new JButton(APPLY_BUTTON_TEXT);
		rabbitDefaultButton = new JButton(DEFAULT_BUTTON_TEXT);
		foxApplyButton = new JButton(APPLY_BUTTON_TEXT);
		foxDefaultButton = new JButton(DEFAULT_BUTTON_TEXT);
		
		// Containers
		rabbitContainer = new JPanel();
		foxContainer = new JPanel();
	}
	
	private void layoutComponents() {
		setBorders();
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		constraints.insets = new Insets(10, 30, 0, 30);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		layoutRabbitComponents();
		this.add(rabbitContainer, constraints);
		
		constraints.insets = new Insets(10, 30, 10, 30);
		constraints.gridy = 1;
		layoutFoxComponents();
		this.add(foxContainer, constraints);
	}
	
	private void layoutRabbitComponents() {
		rabbitContainer.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Max Age Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		rabbitContainer.add(rabbitMaxAgeLabel, constraints);
		
		// Max Age Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.ipady = 10;
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		rabbitContainer.add(rabbitMaxAgeSpinner, constraints);
		
		// Breeding Age Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		rabbitContainer.add(rabbitBreedingAgeLabel, constraints);
		
		// Breeding Age Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		rabbitContainer.add(rabbitBreedingAgeSpinner, constraints);
		
		// Breeding Probability Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		rabbitContainer.add(rabbitBreedingProbabilityLabel, constraints);
		
		// Breeding Age Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		rabbitContainer.add(rabbitBreedingProbabilitySpinner, constraints);
		
		// Max Litter Size Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		rabbitContainer.add(rabbitMaxLitterSizeLabel, constraints);
		
		// Breeding Age Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		rabbitContainer.add(rabbitMaxLitterSizeSpinner, constraints);
		
		// Apply Button
		constraints.insets = new Insets(10, 10, 10, 0);
		constraints.weightx = 0.5;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		rabbitContainer.add(rabbitApplyButton, constraints);
		
		// Default Button
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 3;
		rabbitContainer.add(rabbitDefaultButton, constraints);
	}
	
	private void layoutFoxComponents() {
		foxContainer.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Max Age Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		foxContainer.add(foxMaxAgeLabel, constraints);
		
		// Max Age Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.ipady = 10;
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		foxContainer.add(foxMaxAgeSpinner, constraints);
		
		// Breeding Age Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		foxContainer.add(foxBreedingAgeLabel, constraints);
		
		// Breeding Age Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		foxContainer.add(foxBreedingAgeSpinner, constraints);
		
		// Breeding Probability Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		foxContainer.add(foxBreedingProbabilityLabel, constraints);
		
		// Breeding Age Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		foxContainer.add(foxBreedingProbabilitySpinner, constraints);
		
		// Max Litter Size Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		foxContainer.add(foxMaxLitterSizeLabel, constraints);
		
		// Breeding Age Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		foxContainer.add(foxMaxLitterSizeSpinner, constraints);
		
		// Food Value Label
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.weightx = 0;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		foxContainer.add(foxFoodValueLabel, constraints);
		
		// Food Value Spinner
		constraints.insets = new Insets(10, 10, 0, 10);
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		constraints.gridwidth = 5;
		foxContainer.add(foxFoodValueSpinner, constraints);
		
		// Apply Button
		constraints.insets = new Insets(10, 10, 10, 0);
		constraints.weightx = 0.5;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 3;
		foxContainer.add(foxApplyButton, constraints);
		
		// Default Button
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 3;
		foxContainer.add(foxDefaultButton, constraints);
	}
	
	private void setBorders() {
		TitledBorder rabbitBorder = BorderFactory.createTitledBorder(RABBIT_TITLE);
		rabbitContainer.setBorder(rabbitBorder);
		
		TitledBorder foxBorder = BorderFactory.createTitledBorder(FOX_TITLE);
		foxContainer.setBorder(foxBorder);
	}
	
	private String[] getSpinnerValues(int min, int max, String addedText) {
		String[] values = new String[max - min + 1];
		for (int i = min; i <= max; i++) {
			values[i - min] = i + addedText;
		}
		
		return values;
	}
}