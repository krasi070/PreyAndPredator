import javax.swing.*;

public class SimulationMenuBar extends JMenuBar {
	
	public JMenuItem openItem, saveItem, saveAsItem;
	
	private JMenu fileMenu;
	
	public SimulationMenuBar() {
		initComponents();
		addComponents();
	}
	
	private void initComponents() {
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open...");
		saveItem = new JMenuItem("Save");
		saveAsItem = new JMenuItem("Save as...");
	}
	
	private void addComponents() {
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		add(fileMenu);
	}
}
