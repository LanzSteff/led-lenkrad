package application;

import static application._Config.SIMULATION;
import static application._Config.NR_LED_PIXELS;

import java.io.IOException;

import ledStripe.LedStripeFacade;
import ledStripeSimulator.Gui;

public class Application {

	public Application() throws Exception {
		LedInterface ledInterface;

		if (SIMULATION) {
			Gui gui = new Gui();
			ledInterface = gui.getLedPanelFacade();
		} else {
			ledInterface = new LedStripeFacade(NR_LED_PIXELS);
		}
		Tests t = new Tests(ledInterface);

//		t.risingRPM();
//		t.rpmCicle();
		t.rpmNeedle();
	}

	public static void main(String[] args) throws IOException {
		try {
			new Application();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
