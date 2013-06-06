package com.wander.train.pc.common;

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

public class MonitorView extends JFrame {

	private static final long serialVersionUID = 3798395486791804473L;
	private GraphicPanel graphicPanel;
	private JPanel buttonsPanel;
	private JButton btSwitchMain, btSwitchBranch, btStartA, btForwardB, btBackwardB, btStop,
			btStartB, btSpeedDown;
	private TextArea infoArea;
	private MonitorModel control;

	private Timer timer;
	/**
	 * 更新周期
	 */
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
		btStartA = new JButton("Start");
		btStop = new JButton("Stop");
		btForwardB = new JButton("forward(B)");
		btBackwardB = new JButton("backward(B)");
		btStartB = new JButton("startB");
		btSpeedDown = new JButton("speed down");
		buttonsPanel.add(btStartA);
		buttonsPanel.add(btSwitchMain);
		buttonsPanel.add(btForwardB);
		buttonsPanel.add(btStartB);
		buttonsPanel.add(btSwitchBranch);
		buttonsPanel.add(btBackwardB);
		buttonsPanel.add(btStop);
		buttonsPanel.add(btSpeedDown);
		
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
				control.push();
				updateGUI();
			}
		});
		//timer.start();
		updateGUI();
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			control.commandExit();
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
					control.commandStationSwitch(true);
					infoArea.setText("switch to Main.");
			}
		});

		btSwitchBranch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					control.commandStationSwitch(false);
					infoArea.setText("switch to Branch.");
			}
		});

		btStartA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.start();
				control.commandStart();
				control.commandTrainBackward(0, 1);
				infoArea.setText("start");
			}
		});
		btStartB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO 先注释掉
				/*	
				control.commandTrainForward(0, 2);
				infoArea.setText("start");
				*/
			}
			
		});
		btStop.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				timer.stop();
				control.commandTrainStop(0);
				//control.commandTrainStop(1);
				infoArea.setText("stop");
			}
			
		});
		/*

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
		
		private String back = "image/backmap.jpg";
		private String aForward = "image/AForward.jpg";
		private String aBackward = "image/ABackward.jpg";
		private String bForward = "image/BForward.jpg";
		private String bBackward = "image/BBackward.jpg";
		private final Image backImage = new ImageIcon(back).getImage();
		private final Image trainAForward = new ImageIcon(aForward).getImage();
		private final Image trainABackward = new ImageIcon(aBackward).getImage();
		private final Image trainBForward = new ImageIcon(bForward).getImage();
		private final Image trainBBackward = new ImageIcon(bBackward).getImage();
		
		private int forwardY = 32;
		private int backwardY = 90;
		//TODO untested
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backImage != null)
				g.drawImage(backImage, 0, 0, null);
				//1 在0前面
				/*
				int posB = control.getTrainPos(1);
				if(posB == 0){
					g.drawImage(trainBForward, 180, forwardY, null);
				}else if(posB == 1){
					g.drawImage(trainBBackward, 260, backwardY, null);
				}else if(posB == 2){
					g.drawImage(trainBBackward, 120, backwardY, null);
				}
				else{
					g.drawImage(trainBForward, 50, forwardY, null);
				}
				*/
				int posA = control.getTrainPos(0);
				if (posA == 0) {
					g.drawImage(trainAForward, 120, forwardY, null);
				} else if (posA == 1) {
					g.drawImage(trainABackward, 260, backwardY, null);
				} else if (posA == 2) {
					g.drawImage(trainABackward, 180, backwardY, null);
				} else {
					g.drawImage(trainAForward, 50, forwardY, null);
				}
		}
	}
}
