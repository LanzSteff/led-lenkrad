package application;

import static application._Config.NR_LED_PIXELS;

import java.awt.Color;
import java.util.Random;

import jssc.SerialPortException;
import rmi.RmiClient;

public class Tests {

	private final RmiClient c;
	private final LedInterface ledStripe;
	private int pos = 432/4*3-15;
	private int rpmOld, rpm, peak, counter=0; //global variables for method rpmNeedle()
	private byte color = (byte)30; //(byte)255 = Simulator, (byte)30 = sonst
	private int sleep = 0; //50 = Simulator, 0 = sonst

	public Tests(LedInterface o) {
		ledStripe = o;
		c = new RmiClient("rmi://127.0.0.1/openDsServer");
	}

	// 125 ms bei einer Baudrate von 38400, 8 pattern in der Sekunde
	// 38.6 ms bei einer Baudrate von 115200, 26 pattern in der Sekunde

	private int dtMax, n, dtSum;

	public void belastungstest() {
		new Thread() {
			public void run() {
				byte[] a = new byte[NR_LED_PIXELS * 3];
				int idx = 0;
				long t_old = System.currentTimeMillis();
				long t;
				int cnt = 0;
				while (true) {
					t = System.currentTimeMillis();
					int dt = (int) (t - t_old);
					dtSum += dt;
					if (n++ > 100 && dt > dtMax) {
						dtMax = dt;
					}
					System.out.println(dt + "  " + dtMax + "  " + (dtSum / n)
							+ "  " + cnt++);
					t_old = t;
					a[idx * 3] = (byte) 0;
					a[idx * 3 + 1] = (byte) 0;
					a[idx * 3 + 2] = (byte) 0;
					idx = (idx + 1) % NR_LED_PIXELS;
					a[idx * 3] = (byte) 255;
					a[idx * 3 + 1] = (byte) 255;
					a[idx * 3 + 2] = (byte) 255;
					ledStripe.setColorPattern(a);

				}
			}
		}.start();
	}

	public void testSetOffset() {
		byte bw[] = new byte[NR_LED_PIXELS];
		for (int i = 0; i < 6; i++) {
			bw[i] = (byte) (255);
		}

		ledStripe.setBrightness(255);
		ledStripe.setColor(new Color(255, 255, 255));
		ledStripe.setBwPattern(bw);

		for (int i = 0; i > -150; i--) {
			ledStripe.setOffset(i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void test1() {
		byte a[] = new byte[3 * NR_LED_PIXELS];
		for (int i = 0; i < 100; i += 4) {
			a[i] = (byte) 255;
		}

		ledStripe.setColorPattern(a);
		new Thread() {
			Random r = new Random();

			public void run() {
				while (true) {
					ledStripe.setOffset(r.nextInt(NR_LED_PIXELS));
					try {
						Thread.sleep(60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void test2() {

		byte bw[] = new byte[NR_LED_PIXELS];
		for (int i = 0; i < NR_LED_PIXELS; i++) {
			bw[i] = (byte) ((i * 255) / NR_LED_PIXELS);
		}
		ledStripe.setColor(Color.RED);
		ledStripe.setBrightness(255);
		ledStripe.setBwPattern(bw);

		new Thread() {
			Random rand = new Random();

			public void run() {
				while (true) {
					int r = rand.nextInt(256);
					int g = rand.nextInt(256);
					int b = rand.nextInt(256);
					ledStripe.setColor(new Color(r, b, g));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void rmiTest() throws SerialPortException {
		byte a[] = new byte[3 * NR_LED_PIXELS];
		for (int i = 0; i < 30; i += 3) {
			a[i] = (byte) 255;
		}

		ledStripe.setColorPattern(a);

		new Thread() {
			public void run() {
				while (true) {
					ledStripe.setOffset(c.getSpeed());
					try {
						Thread.sleep(70);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void risingRPM() {
		final byte a[] = new byte[3 * NR_LED_PIXELS];
		ledStripe.setOffset(NR_LED_PIXELS/4);
		new Thread() {
			public void run() {
				while(true) {
					for(int i = 0; i < a.length; i += 6) { //adjust left side of wheel
						if(i <= c.getRPM()/20) {
							if(i < 100) { //green
								a[i] = 0;
								a[i+1] = color;
								a[i+2] = 0;
								a[i+3] = 0;
								a[i+4] = color;
								a[i+5] = 0;
							} else if(i < 150) { //blue
								a[i] = 0;
								a[i+1] = 0;
								a[i+2] = color;
								a[i+3] = 0;
								a[i+4] = 0;
								a[i+5] = color;
							} else { //red
								a[i] = color;
								a[i+1] = 0;
								a[i+2] = 0;
								a[i+3] = color;
								a[i+4] = 0;
								a[i+5] = 0;
							}
						} else { //reset LEDs
							a[i] = 0;
							a[i+1] = 0;
							a[i+2] = 0;
							a[i+3] = 0;
							a[i+4] = 0;
							a[i+5] = 0;
						}
					}
					for(int i = a.length-1; i >= 0; i -= 3) { //adjust right side of wheel
						if(i >= a.length-(c.getRPM()/20)) {
							if(i > a.length-100) { //green
								a[i] = 0;
								a[i-1] = color;
								a[i-2] = 0;
								a[i-3] = 0;
								a[i-4] = color;
								a[i-5] = 0;
							} else if(i > a.length-150) { //blue
								a[i] = color;
								a[i-1] = 0;
								a[i-2] = 0;
								a[i-3] = color;
								a[i-4] = 0;
								a[i-5] = 0;
							} else { //red
								a[i] = 0;
								a[i-1] = 0;
								a[i-2] = color;
								a[i-3] = 0;
								a[i-4] = 0;
								a[i-5] = color;
							}
						} else {
//							a[i] = 0;
//							a[i-1] = 0;
//							a[i-2] = 0;
						}
					}
					ledStripe.setColorPattern(a);
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void rpmBigNeedle() {
		final byte a[] = new byte[3 * NR_LED_PIXELS];
		for(int i = 0; i < 30; i += 3) {
			a[i] = color;
		}
		ledStripe.setOffset((NR_LED_PIXELS/4*3)-15);
		ledStripe.setColorPattern(a);
		new Thread() {
			public void run() {
				while (true) {
					ledStripe.setOffset(c.getRPM()/10);
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void rpmCicle() {
		final byte a[] = new byte[3 * NR_LED_PIXELS];
		for(int i = 0; i < 30; i += 3) { //set inital LEDs
			a[i] = color;
		}
		ledStripe.setOffset((NR_LED_PIXELS/4*3)-15);
		ledStripe.setColorPattern(a);
		new Thread() {
			public void run() {
				while(true) { //adjust spinning speed
					int rpm = c.getRPM();
					if(rpm < 1000) {
						pos += 1;
						ledStripe.setOffset(pos);
					} else if(rpm < 1500) {
						pos += 2;
						ledStripe.setOffset(pos);
					} else if(rpm < 2000) {
						pos += 4;
						ledStripe.setOffset(pos);
					} else if(rpm < 2500) {
						pos += 6;
						ledStripe.setOffset(pos);
					} else if(rpm < 3000) {
						pos += 8;
						ledStripe.setOffset(pos);
					} else if(rpm < 3500) {
						pos += 10;
						ledStripe.setOffset(pos);
					} else if(rpm < 4000) {
						pos += 12;
						ledStripe.setOffset(pos);
					} else if(rpm < 4500) {
						pos += 14;
						ledStripe.setOffset(pos);
					} else if(rpm < 5000) {
						pos += 16;
						ledStripe.setOffset(pos);
					} else {
						pos += 18;
						ledStripe.setOffset(pos);
					}
//					ledStripe.setOffset(c.getRPM()/10);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void rpmNeedle() {
		final byte a[] = new byte[3 * NR_LED_PIXELS];
		new Thread() {
			public void run() {
				while(true) {
					int zero = a.length-1;///4*3; //LED at point zero
					for(int i = 0; i < a.length; i++) { //reset all LEDs
						a[i] = 0;
					}
					rpm = c.getRPM();
					if(rpm < 1) {
						rpm = 1;
					}
					if(rpm < rpmOld) {
						peak = zero-((rpmOld/50*3)+2);
						a[peak] = color; //add red peak
						counter++;
						if(counter > 40) {
							a[peak] = 0; //remove red peak after some time
							rpmOld = rpm;
						}
					}
					else {
						rpmOld = rpm;
						counter = 0;
					}
					a[zero-((rpm/50*3)+1)] = color; //dynamic green(+1) for RPM needle
					a[zero-((1000/50*3))] = color; //static blue(+2) at 1000 RPM
					a[zero-((2000/50*3))] = color; //static blue(+2) at 2000 RPM
					a[zero-((3000/50*3))] = color; //static blue(+2) at 3000 RPM
					a[zero-((4000/50*3))] = color; //static blue(+2) at 4000 RPM
//					a[zero-(5000/60*3)+2] = color; //static blue(+2) at 5000 RPM
//					a[zero-(750/60*3)+2] = color; //static blue(+2) at 750 RPM (idle)
					ledStripe.setColorPattern(a);
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}
