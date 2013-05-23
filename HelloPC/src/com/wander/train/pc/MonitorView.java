package com.wander.train.pc;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.wander.train.pc.state.TrainLeaveState;

public class MonitorView extends JFrame {

	private static final long serialVersionUID = 3798395486791804473L;
	private GraphicPanel graphicPanel;
	private JPanel buttonsPanel;
	private JButton btSwitchMain, btSwitchBranch, btStart, btForwardB, btBackwardB, btStop,
			btSpeedUp, btSpeedDown;
	private TextArea infoArea;
	private MonitorModel control;

	private Timer timer;
	private int UPDATE_PERIOD = 200;
	
	public static void main(String[] args) {
		MonitorView pcGUI = new MonitorView();
		pcGUI.setSize(360, 500);
		pcGUI.setLocationRelativeTo(null);
		pcGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pcGUI.setVisible(true);
	}

	public MonitorView() {
		graphicPanel = new GraphicPanel();
		graphicPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		buttonsPanel = new JPanel(new GridLayout(3, 3, 5, 5));
		buttonsPanel.setBorder(new TitledBorder("command buttons"));
		btSwitchMain = new JButton("Switch to Main");
		btSwitchBranch = new JButton("Switch to Branch");
		btStart = new JButton("Start");
		btStop = new JButton("Stop");
		btForwardB = new JButton("forward(B)");
		btBackwardB = new JButton("backward(B)");
		btSpeedUp = new JButton("speed up");
		btSpeedDown = new JButton("speed down");
		buttonsPanel.add(btSwitchMain);
		buttonsPanel.add(btForwardB);
		buttonsPanel.add(btSpeedUp);
		buttonsPanel.add(btSwitchBranch);
		buttonsPanel.add(btBackwardB);
		buttonsPanel.add(btSpeedDown);
		buttonsPanel.add(btStart);
		buttonsPanel.add(btStop);
		
		infoArea = new TextArea();
		setLayout(new GridLayout(3, 1));
		add(graphicPanel);
		add(infoArea);
		add(buttonsPanel);

		addListener();
		control = new MonitorModel();

		timer = new Timer(UPDATE_PERIOD, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.push();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "state error.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
				updateGUI();
			}
		});

		//timer.start();
		updateGUI();
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try {
				control.commandExit();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "exit error.", "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
			control.clean();
		}
		super.processWindowEvent(e);
	}

	private void updateGUI() {
		int pos = control.getTrainPos(0);
		// update text field
		infoArea.setText("train position : ");
		if (pos == 0) {
			infoArea.append("stationA -> stationB");
		} else if (pos == 1) {
			infoArea.append("stationB");
		} else if (pos == 2) {
			infoArea.append("stationB -> stationA");
		} else {
			infoArea.append("stationA");
		}
		// update graphic panel
		graphicPanel.repaint();
	}

	private void addListener() {
		btSwitchMain.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandSwitchMain(true);
					//control.commandForward(0,3);
					infoArea.setText("switch to Main.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "forward(A) error.",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btSwitchBranch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandSwitchMain(false);
					infoArea.setText("switch to Branch.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "backward(A) error.",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.start();
				try {
					control.commandStart();
					control.commandBackward(0, 1);
					infoArea.setText("start");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "start error.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btStop.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				timer.stop();
				try{
					control.commandStop(0);
					infoArea.setText("stop");
				}
				catch(IOException e1){
					JOptionPane.showMessageDialog(null, "stop error.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		/*
		btForwardB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandBackward(0,1);
					infoArea.setText("train is forward to B.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "forward(B) error.",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btBackwardB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandForward(0,2);
					infoArea.setText("train is backward to A.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "backward(B) error.",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btSpeedUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandSpeedUp(0);
					infoArea.setText("speed up.\n current speed : "
							+ control.getTrainSpeed(0));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "speed up error.",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btSpeedDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					control.commandSpeedDown(0);
					infoArea.setText("speed down.\n current speed : "
							+ control.getTrainSpeed(0));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "speed down error.",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		*/
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
			if (backImage != null)
				g.drawImage(backImage, 0, 0, null);
			if (trainImage != null) {
				int pos = control.getTrainPos(0);
				if (pos == 0) {
					g.drawImage(trainImage, 150, 32, null);
				} else if (pos == 1) {
					g.drawImage(trainImage, 260, 32, null);
				} else if (pos == 2) {
					g.drawImage(trainImage, 152, 90, null);
				} else {
					g.drawImage(trainImage, 50, 32, null);
				}
			}
		}
	}
}
