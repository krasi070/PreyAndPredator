import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

// Add color picker
public class AnimalsTab extends JPanel implements ActionListener {

	public final String TITLE = "Animals";
	
	private final String RABBIT_APPLY_MESSAGE = "Are you sure you want to apply these changes to every rabbit?";
	private final String FOX_APPLY_MESSAGE = "Are you sure you want to apply these changes to every fox?";
	private final String RABBIT_TITLE = "Rabbit";
	private final String FOX_TITLE = "Fox";
	private final String MAX_AGE_TEXT = "Maximum age: ";
	private final String BREEDING_AGE_TEXT = "Breeding age: ";
	private final String BREEDING_PROBABILITY_TEXT = "Breeding probability: ";
	private final String MAX_LITTER_SIZE_TEXT = "Maximum litter size: ";
	private final String RABBIT_FOOD_VALUE_TEXT = "Rabbit food value: ";
	private final String APPLY_BUTTON_TEXT = "Apply";
	private final String DEFAULT_BUTTON_TEXT = "Default";
	
	private final int MIN_AGE_VALUE = 1;
	private final int MAX_AGE_VALUE = 1000;
	private final int MIN_LITTER_SIZE_VALUE = 0;
	private final int MAX_LITTER_SIZE_VALUE = 8;
	private final int MIN_FOOD_VALUE = 1;
	private final int MAX_FOOD_VALUE = 100;
	
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
		setCorrectValuesInRabbitSpinners();
		setCorrectValuesInFoxSpinners();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rabbitApplyButton) {
			applyRabbitChanges();
		}
		else if (e.getSource() == foxApplyButton) {
			applyFoxChanges();
		}
		else if (e.getSource() == rabbitDefaultButton) {
			Rabbit.setDefaultValues();
			setCorrectValuesInRabbitSpinners();
		}
		else if (e.getSource() == foxDefaultButton) {
			Fox.setDefaultValues();
			setCorrectValuesInFoxSpinners();
		}
	}
	
	private void applyRabbitChanges() {
		int chosenOption = JOptionPane.showConfirmDialog(this, RABBIT_APPLY_MESSAGE);
		if (chosenOption == 0) {
			Rabbit.maxAge = Integer.parseInt(rabbitMaxAgeSpinner.getValue().toString());
			Rabbit.breedingAge = Integer.parseInt(rabbitBreedingAgeSpinner.getValue().toString());
			
			String withPercentageSign = rabbitBreedingProbabilitySpinner.getValue().toString();
			String withoutPercentageSign = withPercentageSign.substring(0, withPercentageSign.length() - 1);
			Rabbit.breedingProbability = Double.parseDouble(withoutPercentageSign) / 100.0;
			
			Rabbit.maxLitterSize = Integer.parseInt(rabbitMaxLitterSizeSpinner.getValue().toString());
		}
	}
	
	private void applyFoxChanges() {
		int chosenOption = JOptionPane.showConfirmDialog(this, FOX_APPLY_MESSAGE);
		if (chosenOption == 0) {
			Fox.maxAge = Integer.parseInt(foxMaxAgeSpinner.getValue().toString());
			Fox.breedingAge = Integer.parseInt(foxBreedingAgeSpinner.getValue().toString());
			
			String withPercentageSign = foxBreedingProbabilitySpinner.getValue().toString();
			String withoutPercentageSign = withPercentageSign.substring(0, withPercentageSign.length() - 1);
			Fox.breedingProbability = Double.parseDouble(withoutPercentageSign) / 100.0;
			
			Fox.maxLitterSize = Integer.parseInt(foxMaxLitterSizeSpinner.getValue().toString());
			Fox.rabbitFoodValue = Integer.parseInt(foxFoodValueSpinner.getValue().toString());
		}
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
		String[] ageValues = getSpinnerValues(MIN_AGE_VALUE, MAX_AGE_VALUE, "");
		String[] litterSizeValues = getSpinnerValues(MIN_LITTER_SIZE_VALUE, MAX_LITTER_SIZE_VALUE, "");
		String[] foodValues = getSpinnerValues(MIN_FOOD_VALUE, MAX_FOOD_VALUE, "");
		String[] spinnerPercentages = getSpinnerValues(0, 100, "%");
		JSpinner.DefaultEditor spinnerEditor;
		
		// Rabbit Spinners
		rabbitBreedingAgeSpinner = new JSpinner(new SpinnerListModel(ageValues));
		spinnerEditor = (JSpinner.DefaultEditor)rabbitBreedingAgeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		rabbitMaxAgeSpinner = new JSpinner(new SpinnerListModel(ageValues));
		spinnerEditor = (JSpinner.DefaultEditor)rabbitMaxAgeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		rabbitBreedingProbabilitySpinner = new JSpinner(new SpinnerListModel(spinnerPercentages));
		spinnerEditor = (JSpinner.DefaultEditor)rabbitBreedingProbabilitySpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		rabbitMaxLitterSizeSpinner = new JSpinner(new SpinnerListModel(litterSizeValues));
		spinnerEditor = (JSpinner.DefaultEditor)rabbitMaxLitterSizeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		// Fox Spinners
		foxBreedingAgeSpinner = new JSpinner(new SpinnerListModel(ageValues));
		spinnerEditor = (JSpinner.DefaultEditor)foxBreedingAgeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		foxMaxAgeSpinner = new JSpinner(new SpinnerListModel(ageValues));
		spinnerEditor = (JSpinner.DefaultEditor)foxMaxAgeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		foxBreedingProbabilitySpinner = new JSpinner(new SpinnerListModel(spinnerPercentages));
		spinnerEditor = (JSpinner.DefaultEditor)foxBreedingProbabilitySpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		foxMaxLitterSizeSpinner = new JSpinner(new SpinnerListModel(litterSizeValues));
		spinnerEditor = (JSpinner.DefaultEditor)foxMaxLitterSizeSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		foxFoodValueSpinner = new JSpinner(new SpinnerListModel(foodValues));
		spinnerEditor = (JSpinner.DefaultEditor)foxFoodValueSpinner.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		// Buttons
		rabbitApplyButton = new JButton(APPLY_BUTTON_TEXT);
		rabbitApplyButton.addActionListener(this);
		
		rabbitDefaultButton = new JButton(DEFAULT_BUTTON_TEXT);
		rabbitDefaultButton.addActionListener(this);
		
		foxApplyButton = new JButton(APPLY_BUTTON_TEXT);
		foxApplyButton.addActionListener(this);
		
		foxDefaultButton = new JButton(DEFAULT_BUTTON_TEXT);
		foxDefaultButton.addActionListener(this);
		
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
		constraints.ipady = 10;
		
		ArrayList<JComponent> components = new ArrayList<>();
		components.add(rabbitMaxAgeLabel);
		components.add(rabbitMaxAgeSpinner);
		components.add(rabbitBreedingAgeLabel);
		components.add(rabbitBreedingAgeSpinner);
		components.add(rabbitBreedingProbabilityLabel);
		components.add(rabbitBreedingProbabilitySpinner);
		components.add(rabbitMaxLitterSizeLabel);
		components.add(rabbitMaxLitterSizeSpinner);
		
		for (int y = 0; y < components.size(); y += 2) {
			for (int x = 0; x < 2; x++) {
				constraints.insets = new Insets(10, 10, 0, x * 10);
				constraints.weightx = x;
				constraints.gridx = x;
				constraints.gridy = y / 2;
				constraints.gridwidth = 1 + x * 5;
				rabbitContainer.add(components.get(y + x), constraints);
			}
		}
		
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
		constraints.ipady = 10;
		
		ArrayList<JComponent> components = new ArrayList<>();
		components.add(foxMaxAgeLabel);
		components.add(foxMaxAgeSpinner);
		components.add(foxBreedingAgeLabel);
		components.add(foxBreedingAgeSpinner);
		components.add(foxBreedingProbabilityLabel);
		components.add(foxBreedingProbabilitySpinner);
		components.add(foxMaxLitterSizeLabel);
		components.add(foxMaxLitterSizeSpinner);
		components.add(foxFoodValueLabel);
		components.add(foxFoodValueSpinner);
		
		for (int y = 0; y < components.size(); y += 2) {
			for (int x = 0; x < 2; x++) {
				constraints.insets = new Insets(10, 10, 0, x * 10);
				constraints.weightx = x;
				constraints.gridx = x;
				constraints.gridy = y / 2;
				constraints.gridwidth = 1 + x * 5;
				foxContainer.add(components.get(y + x), constraints);
			}
		}
		
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
	
	private void setCorrectValuesInRabbitSpinners() {
		rabbitMaxAgeSpinner.setValue(Integer.toString(Rabbit.maxAge));
		rabbitBreedingAgeSpinner.setValue(Integer.toString(Rabbit.breedingAge));
		rabbitBreedingProbabilitySpinner.setValue((int)(Rabbit.breedingProbability * 100) + "%");
		rabbitMaxLitterSizeSpinner.setValue(Integer.toString(Rabbit.maxLitterSize));
	}
	
	private void setCorrectValuesInFoxSpinners() {
		foxMaxAgeSpinner.setValue(Integer.toString(Fox.maxAge));
		foxBreedingAgeSpinner.setValue(Integer.toString(Fox.breedingAge));
		foxBreedingProbabilitySpinner.setValue((int)(Fox.breedingProbability * 100) + "%");
		foxMaxLitterSizeSpinner.setValue(Integer.toString(Fox.maxLitterSize));
		foxFoodValueSpinner.setValue(Integer.toString(Fox.rabbitFoodValue));
	}
	
	private String[] getSpinnerValues(int min, int max, String addedText) {
		String[] values = new String[max - min + 1];
		for (int i = min; i <= max; i++) {
			values[i - min] = i + addedText;
		}
		
		return values;
	}
}