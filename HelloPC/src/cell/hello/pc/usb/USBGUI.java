package cell.hello.pc.usb;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import cell.hello.pc.usb.USBSend;

public class USBGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel ButtonGroup = new JPanel();
	private static JTextArea text = new JTextArea();
	
	public static USBSend sender = new USBSend();

	public USBGUI() {
		JButton b1 = new JButton("Go to Platform 1");
		JButton b2 = new JButton("Stop");
		JButton b3 = new JButton("Go to Platform 2");
		ButtonGroup.add(b1,BorderLayout.WEST);
		ButtonGroup.add(b2,BorderLayout.CENTER);
		ButtonGroup.add(b3,BorderLayout.EAST);
		text.setEditable(false);
		add(text,BorderLayout.NORTH);
		add(ButtonGroup,BorderLayout.CENTER);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sender.SendMessage(1);
			}
		});
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sender.SendMessage(0);
			}
		});
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sender.SendMessage(2);
			}
		});
	}
	
	public static void UpdateText(String s) {
		text.setText("");
		text.append(s);
	}
	
	protected void processWindowEvent(WindowEvent e) {
		if(e.getID() == WindowEvent.WINDOW_CLOSING) {
			sender.SendMessage(-1);
		}
		super.processWindowEvent(e);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		USBGUI f = new USBGUI();
		f.setTitle(f.getClass().getSimpleName());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
		sender.ReceiveMessage();
		sender.QuitMessage();
	}

}
