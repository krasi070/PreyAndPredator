import java.io.*;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class FileCommander {
	
	private static final String RABBIT = "R";
	private static final String FOX = "F";
	private static final String SEPERATOR = "-";
	private static final String EMPTY = "_";
	
	// Returns the number of steps the simulation has ran
	public static int OpenSimulation(File file, Field field) {
		try (Scanner reader = new Scanner(file)) {
			
			reader.useLocale(Locale.UK);
			field.clear();
			
			int steps = reader.nextInt();
			
			Rabbit.maxAge = reader.nextInt();
			Rabbit.breedingAge = reader.nextInt();
			Rabbit.breedingProbability = reader.nextDouble();
			Rabbit.maxLitterSize = reader.nextInt();
			
			Fox.maxAge = reader.nextInt();
			Fox.breedingAge = reader.nextInt();
			Fox.breedingProbability = reader.nextDouble();
			Fox.maxLitterSize = reader.nextInt();
			Fox.rabbitFoodValue = reader.nextInt();
			
			for (int i = 0; i < field.getDepth(); i++) {
				for (int j = 0; j < field.getWidth(); j++) {
					String cell = reader.next();
					if (!cell.equals(EMPTY)) {
						if (cell.startsWith(RABBIT)) {
							int age = Integer.parseInt(cell.substring(RABBIT.length(), cell.length()));
							Rabbit rabbit = new Rabbit(false, field, new Location(i, j));
							rabbit.setAge(age);
							field.place(rabbit, rabbit.getLocation());
						}
						else if (cell.startsWith(FOX)) {
							String[] args = cell.substring(FOX.length(), cell.length()).split(SEPERATOR);
							int age = Integer.parseInt(args[0]);
							int foodLevel = Integer.parseInt(args[1]);
							Fox fox = new Fox(false, field, new Location(i, j));
							fox.setAge(age);
							fox.setFoodLevel(foodLevel);
							field.place(fox, new Location(i, j));
						}
					}
				}
			}
			
			return steps;
		}
		catch(Exception e) {
			displayErrorMessage();
			System.out.println(e.getMessage());
		}
		
		return 0;
	}
	
	public static void SaveSimulation(File file, Field field, int step) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			
			writer.write(Integer.toString(step));
			writer.newLine();
			
			writer.write(Rabbit.maxAge + " " + 
				    Rabbit.breedingAge + " " + 
				    Rabbit.breedingProbability + " " +
				    Rabbit.maxLitterSize);
			writer.newLine();
			
			writer.write(Fox.maxAge + " " + 
					Fox.breedingAge + " " + 
					Fox.breedingProbability + " " +
					Fox.maxLitterSize + " " +
					Fox.rabbitFoodValue);
			writer.newLine();
			
			for (int i = 0; i < field.getDepth(); i++) {
				for (int j = 0; j < field.getWidth(); j++) {
					Animal animal = (Animal)field.getObjectAt(i, j);
					if (animal instanceof Rabbit) {
						writer.write(RABBIT + animal.getAge() + " ");
					}
					else if (animal instanceof Fox) {
						Fox fox = (Fox)animal;
						writer.write(FOX + fox.getAge() + SEPERATOR + fox.getFoodLevel() + " ");
					}
					else {
						writer.write(EMPTY + " ");
					}
				}
				
				writer.newLine();
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void displayErrorMessage() {
		JOptionPane.showMessageDialog(null,
			    "The selected file was not in the correct format!",
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}
}