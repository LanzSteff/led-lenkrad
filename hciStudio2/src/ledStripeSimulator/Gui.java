package ledStripeSimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Gui {

	private JFrame frame;
	private LedPanel ledPanel;
	private JPanel panel;
	private JButton btnNewButton;

	public Gui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(getLedPanel(), BorderLayout.CENTER);
		frame.getContentPane().add(getPanel(), BorderLayout.SOUTH);
		frame.setVisible(true);

		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyChar() == 'f') {
						switchScreenMode();
					}
				}
				return false;
			}
		});
	}

	private LedPanel getLedPanel() {
		if (ledPanel == null) {
			ledPanel = new LedPanel(144);
			ledPanel.setBackground(Color.BLACK);
		}
		return ledPanel;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getBtnNewButton());
		}
		return panel;
	}

	public LedPanelFacade getLedPanelFacade() {
		return ledPanel.getFacade();
	}

	// fullscreen -------------------------------------------------------------
	boolean Am_I_In_FullScreen;
	int PrevX;
	int PrevY;
	int PrevWidth;
	int PrevHeight;

	private void switchScreenMode() {
		if (Am_I_In_FullScreen == false) {

			PrevX = frame.getX();
			PrevY = frame.getY();
			PrevWidth = frame.getWidth();
			PrevHeight = frame.getHeight();

			frame.dispose();
			frame.setUndecorated(true);
			frame.setAlwaysOnTop(true);

			frame.setBounds(0, 0, frame.getToolkit().getScreenSize().width,
					frame.getToolkit().getScreenSize().height);
			frame.setVisible(true);
			Am_I_In_FullScreen = true;
			btnNewButton.setText(">/<");
		} else {
			frame.setVisible(false);
			btnNewButton.setText("</>");
			frame.setAlwaysOnTop(false);
			frame.dispose();
			frame.setBounds(PrevX, PrevY, PrevWidth, PrevHeight);

			frame.setUndecorated(false);
			frame.setVisible(true);
			Am_I_In_FullScreen = false;
		}
	}

	// fullscreen end ---------------------------------------------------------

	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("</>");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					switchScreenMode();
				}
			});
		}
		return btnNewButton;
	}
}
