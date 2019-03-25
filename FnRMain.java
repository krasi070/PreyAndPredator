
public class FnRMain {

	public static void main(String[] args) {
		int height = 100;
		int width = 100;
		Simulator s = new Simulator(height, width);
		SimulatorView view = new SimulatorView(s, height, width);
	}

}
