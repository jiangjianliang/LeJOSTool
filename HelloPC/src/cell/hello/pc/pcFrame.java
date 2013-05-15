package cell.hello.pc;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class pcFrame extends JFrame {

	private static final long serialVersionUID = 3798395486791804473L;
	private GraphicPanel graphicPanel;
	private JPanel buttonsPanel;
	private JButton btForwardA, btBackwardA, btStop, btForwardB, btBackwardB, btSpeedUp, btSpeedDown;
	private TextArea infoArea;
	private pcControlTest control;
	
	private Timer timer;

	public static void main(String[] args) {
		pcFrame pcGUI = new pcFrame();
		pcGUI.setSize(360, 500);
		pcGUI.setLocationRelativeTo(null);
		pcGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pcGUI.setVisible(true);
	}

	public pcFrame() {		
		graphicPanel = new GraphicPanel();
		graphicPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		buttonsPanel = new JPanel(new GridLayout(3, 3, 5, 5));
		buttonsPanel.setBorder(new TitledBorder("command buttons"));
		btForwardA = new JButton("forward(A)");
		btBackwardA = new JButton("backward(A)");
		btStop = new JButton("stop");
		btForwardB = new JButton("forward(B)");
		btBackwardB = new JButton("backward(B)");
		btSpeedUp = new JButton("speed up");
		btSpeedDown = new JButton("speed down");
		buttonsPanel.add(btForwardA);
		buttonsPanel.add(btForwardB);
		buttonsPanel.add(btSpeedUp);
		buttonsPanel.add(btBackwardA);
		buttonsPanel.add(btBackwardB);
		buttonsPanel.add(btSpeedDown);
		buttonsPanel.add(btStop);
		infoArea = new TextArea();
		setLayout(new GridLayout(3, 1));
		add(graphicPanel);
		add(infoArea);
		add(buttonsPanel);
		
		addListener();
		control = new pcControlTest();
		
		timer = new Timer(200, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//System.out.println("update.");
					if (control.update())
						updateGUI();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "update error.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		timer.start();
		updateGUI();
		//System.out.println("init over.");
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try {
				control.commandExit();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "exit error.", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		super.processWindowEvent(e);
	}
	
	private void updateGUI() {
		int pos = control.getTrainPos();
		//update text field
		infoArea.setText("train position : ");
		if (pos == 0)	infoArea.append("stationA -> stationB");
		else if (pos == 1)	infoArea.append("stationB");
		else if (pos == 2)	infoArea.append("stationB -> stationA");
		else			infoArea.append("stationA");
		//update graphic panel
		graphicPanel.repaint();
	}

	private void addListener() {
		btForwardA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandForward(3);
					infoArea.setText("train is forward to A.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "forward(A) error.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btBackwardA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandBackward(3);
					infoArea.setText("train is backward to A.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "backward(A) error.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandStop();
					infoArea.setText("train is stop.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "stop error.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btForwardB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandForward(1);
					infoArea.setText("train is forward to B.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "forward(B) error.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btBackwardB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandBackward(1);
					infoArea.setText("train is backward to A.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "backward(B) error.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btSpeedUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandSpeedUp();
					infoArea.setText("speed up.\n current speed : " + control.getTrainSpeed());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "speed up error.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btSpeedDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandSpeedDown();
					infoArea.setText("speed down.\n current speed : " + control.getTrainSpeed());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "speed down error.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	class GraphicPanel extends JPanel {
		private static final long serialVersionUID = 1269940980827250133L;
		
		private final ImageIcon imageIcon_1 = new ImageIcon("image/backmap.jpg");
		private final ImageIcon imageIcon_2 = new ImageIcon("image/train.jpg");
		private final Image backImage = imageIcon_1.getImage();
		private final Image trainImage = imageIcon_2.getImage();
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backImage != null)	g.drawImage(backImage, 0, 0, null);
			if (trainImage != null) {
				int pos = control.getTrainPos();
				if (pos == 0)	g.drawImage(trainImage, 150, 32, null);
				else if (pos == 1)	g.drawImage(trainImage, 260, 32, null);
				else if (pos == 2)	g.drawImage(trainImage, 152, 90, null);
				else 			g.drawImage(trainImage, 50, 32, null);
			}
		}
	}
}
