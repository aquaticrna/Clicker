package autoClicker;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import java.awt.TextField;

import javax.swing.JFormattedTextField;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.Action;
import javax.swing.JTextArea;

public class ClickerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClickerWindow frame = new ClickerWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		
	}

	/**
	 * Create the frame.
	 * @throws AWTException 
	 */
	public ClickerWindow() throws AWTException {
//		Random random = new Random();
		//
		Robot robot = new Robot();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//
		textField = new JTextField();
		textField.setBounds(51, 63, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.setText("Count Down:");
		textField.setEditable(false);
		
		//
		textField_1 = new JTextField();
		textField_1.setBounds(143, 63, 119, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		textField_1.setEditable(false);
		textField_1.setText("2000");
		
		//
		TextField clicks = new TextField();
		clicks.setText("0");
		clicks.setBounds(143, 37, 119, 20);
		JButton clickButton = new JButton();
		clickButton.setText("Press to click");
		clickButton.setBounds(268, 37, 111, 20);
		
		//
		clickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				int numPresses = Integer.parseInt(clicks.getText());
				if(numPresses>10000){
					numPresses = 5000;
					clicks.setText("5000");
				}
				//simple loop to delay the start of clicking so the mouse can be 
				//moved into position while also doing a count down
				for(int x = 2000;x>=0;x=x-20){
					textField_1.setText(String.valueOf(x));
					robot.delay(20);
				}
				
				//does all the clicks
				clicks.setEditable(false);
				for(int x = 0;x<numPresses;x++){
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					clicks.setText(String.valueOf(numPresses-x));
					robot.delay(28);//minimum separation between clicks
				}
				clicks.setEditable(true);
				clicks.setText("0");
			}
		});
		
		//click box label
		JTextPane clickLabel = new JTextPane();
		clickLabel.setBounds(10, 37, 127, 20);
		clickLabel.setText("Number of Times to Click:");
		clickLabel.setEditable(false);
		contentPane.setLayout(null);
		
		//sets up and initializes the mouse position listener and output
		JFormattedTextField mouseLocation = new JFormattedTextField();
		mouseLocation.setBounds(143, 10, 119, 20);
		mouseLocation.setEditable(false);
		contentPane.add(mouseLocation);
		contentPane.add(clickLabel);
		contentPane.add(clicks);
		contentPane.add(clickButton);
		MouseMotionListener thisListener = new MouseMotionListener() {
			public void mouseMoved(MouseEvent e){
				mouseLocation.setText("X = " + e.getX() + " Y = " + e.getY());
			}
			
			public void mouseDragged(MouseEvent e){
				
			}
		};
		MyMouseListener listen = new MyMouseListener(mouseLocation);
		listen.addMouseMotionListener(thisListener);
		listen.start();
		
		//label for the mouse location
		JTextPane textPane = new JTextPane();
		textPane.setText("Mouse Location");
		textPane.setEditable(false);
		textPane.setBounds(53, 10, 80, 20);
		contentPane.add(textPane);
		
		//text box to display the list of recorded commands
		JTextArea actionQueue = new JTextArea();
		actionQueue.setBounds(10, 92, 255, 158);
		actionQueue.setEditable(false);
		contentPane.add(actionQueue);
		
	}
}
