package autoClicker;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import java.awt.TextField;

import javax.swing.JFormattedTextField;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class ClickerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private Queue<Point> actions;
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
		//sets up robot and the outer most frame
		Robot robot = new Robot();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		//count down title
		textField = new JTextField();
		textField.setBounds(51, 63, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.setText("Count Down:");
		textField.setEditable(false);

		//sets up count down display box
		textField_1 = new JTextField();
		textField_1.setBounds(143, 63, 61, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		textField_1.setEditable(false);
		textField_1.setText("2000");

		//sets up number of clicks input box
		TextField clicks = new TextField();
		clicks.setText("0");
		clicks.setBounds(143, 37, 61, 20);
		clicks.setFocusable(true);
		clicks.requestFocusInWindow();
		actions = new LinkedList<Point>();

		JButton clickButton = new JButton();
		clickButton.setText("Press to click");
		clickButton.setBounds(211, 37, 134, 20);
		
		//directions box
		JTextPane directions = new JTextPane();
		directions.setText("Press enter to record mouse position, be sure to select the \"number of times to click\" box, full sequence will be performed that many times");
		directions.setBounds(196, 134, 149, 128);
		contentPane.add(directions);

		//Delay label
		JTextPane delayLabel = new JTextPane();
		delayLabel.setEditable(false);
		delayLabel.setText("Delay:");
		delayLabel.setBounds(196, 273, 48, 27);
		contentPane.add(delayLabel);

		//delay input
		JTextPane delay = new JTextPane();
		delay.setBounds(254, 273, 61, 27);
		delay.setText("0");
		contentPane.add(delay);
		
		//logic for the "start clicking" button
		clickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				int numPresses = Integer.parseInt(clicks.getText());
				if(numPresses>5000){
					numPresses = 5000;
					clicks.setText("5000");
				}
				//simple loop to delay the start of clicking so the mouse can be 
				//moved into position while also doing a count down
				for(int x = 2000;x>=0;x=x-20){
					textField_1.setText(String.valueOf(x));
					robot.delay(20);
				}
				//gets delay
				int delayTime = Integer.parseInt(delay.getText());
				//minimum delay time is 28 ms
				if(delayTime<28){
					delayTime = 28;
				}
				//does all the clicks
				clicks.setEditable(false);
				for(int x = 0;x<numPresses;x++){
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					clicks.setText(String.valueOf(numPresses-x));
					robot.delay(delayTime);//minimum separation between clicks
				}
				clicks.setEditable(true);
				clicks.setText(String.valueOf(numPresses));
			}
		});

		//click box label
		JTextPane clickLabel = new JTextPane();
		clickLabel.setBounds(10, 37, 127, 20);
		clickLabel.setText("Number of Times to Click:");
		clickLabel.setEditable(false);
		contentPane.setLayout(null);

		//text box to display the list of recorded commands
		JTextArea actionQueue = new JTextArea();
		actionQueue.setBounds(10, 92, 174, 208);
		actionQueue.setEditable(false);
		contentPane.add(actionQueue);

		//sets up and initializes the mouse position listener and output
		JFormattedTextField mouseLocation = new JFormattedTextField();
		mouseLocation.setBounds(143, 10, 101, 20);
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

		//code to allow click order recording
		clicks.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\n'){
					String text = mouseLocation.getText();
					String[] split = text.split(" ");
					int x = Integer.parseInt(split[2]);
					int y = Integer.parseInt(split[5]);
					Point toRecord = new Point(x,y); 
					actions.add(toRecord);
					actionQueue.setText(actionQueue.getText() + "\nX = " + toRecord.getX() + " Y = " + toRecord.getY());
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// do nothing
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// do nothing

			}
		});

		//label for the mouse location
		JTextPane textPane = new JTextPane();
		textPane.setText("Mouse Location");
		textPane.setEditable(false);
		textPane.setBounds(53, 10, 80, 20);
		contentPane.add(textPane);
		
		//button to perform a recorded sequence
		JButton sequence = new JButton("Perform Sequence");
		sequence.setHorizontalAlignment(SwingConstants.LEFT);
		sequence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//too many clicks takes too long
				int numPresses = Integer.parseInt(clicks.getText());
				if(numPresses>5000){
					numPresses = 5000;
					clicks.setText("5000");
				}
				//get delay, delay is a minimum of 28 ms or clicks get skipped
				int delayTime = Integer.parseInt(delay.getText());
				if(delayTime<28){
					delayTime = 28;
				}
				//does all the clicks
				clicks.setEditable(false);
				for(int x = 0;x<numPresses;x++){
					for(int y=0;y<actions.size();y++){
						Point nextPoint = actions.poll();
						actions.add(nextPoint);
						robot.mouseMove((int) nextPoint.getX(),(int) nextPoint.getY());
						robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						clicks.setText(String.valueOf(numPresses-x));
						robot.delay(delayTime);//minimum separation between clicks
					}
				}
				clicks.setEditable(true);
				clicks.setText(String.valueOf(numPresses));
			}
		});
		sequence.setBounds(194, 94, 127, 35);
		contentPane.add(sequence);
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				while(!actions.isEmpty()){
					actions.poll();
				}
				actionQueue.setText("");
			}
		});
		clear.setBounds(355, 94, 69, 21);
		contentPane.add(clear);
		
		JButton undo = new JButton("Undo");
		undo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int x = 0; x < actions.size() -1;x++){
					actions.add(actions.poll());
				}
				actions.poll();
				actionQueue.setText(actionQueue.getText().substring(0, actionQueue.getText().lastIndexOf((int) '\n')));
			}
		});
		undo.setBounds(355, 127, 69, 23);
		contentPane.add(undo);

	}
}
