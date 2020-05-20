import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class MainGui {
	MainGui mainGUI;
	JFrame preFrame;
	JTextField usernameTextField;
	JTextField passwordTextField;
	//ovo je potrebno za prikazivanje chat
	JFrame newFrame=new JFrame("Chat ");
	JButton sendMessage;
	JTextField messageBox;
	JTextArea chatBox;
	
	public static void main(String[] args) {
try {
	UIManager.getSystemLookAndFeelClassName();
	}catch(Exception e) {
		e.printStackTrace();
	}
MainGui mainGUI =new MainGui();
mainGUI.preDisplay();

	}
//predisplay je potreban za logovanje,nakon toga pravimo display da bi prikazali  chat u kom mozemo da saljemo poruke
	private void preDisplay() {
		newFrame.setVisible(false);
		 preFrame=new JFrame("Login Chat");
		JLabel EnterUsernameLabel=new JLabel("Enter username:");
		JLabel EnterPasswordLabel=new JLabel("Enter password:");
		 usernameTextField=new JTextField();
		 passwordTextField=new JTextField();
		JButton enterServer=new JButton("Enter Chat Server");
		JPanel prePanel=new JPanel(new GridBagLayout());
		GridBagConstraints preRight=new GridBagConstraints();
		preRight.anchor=GridBagConstraints.EAST;
		GridBagConstraints preLeft=new GridBagConstraints();
		preLeft.anchor=GridBagConstraints.WEST;
		
		preRight.weightx=2.0;
		preRight.fill=GridBagConstraints.HORIZONTAL;
		preRight.gridwidth=GridBagConstraints.REMAINDER;
		prePanel.add(EnterUsernameLabel,preLeft);
		prePanel.add(usernameTextField,preRight);
		prePanel.add(EnterPasswordLabel,preLeft);
		prePanel.add(passwordTextField,preRight);
		preFrame.add(BorderLayout.CENTER,prePanel);
		preFrame.add(BorderLayout.SOUTH,enterServer);
		
		preFrame.setVisible(true);
		preFrame.setSize(300,300);
		enterServer.addActionListener(new enterServerButtonListener());
		
	}
	
	
	
	private void display() {
		newFrame.setVisible(true);
		newFrame.setSize(470,300);
		newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel southPanel=new JPanel();
		newFrame.add(BorderLayout.SOUTH,southPanel);
		southPanel.setLayout(new GridBagLayout());
		southPanel.setBackground(Color.ORANGE);
		messageBox=new JTextField(30);
		sendMessage=new JButton("Send Message");
		chatBox=new JTextArea();
		chatBox.setEditable(false);
		newFrame.add(new JScrollPane(chatBox),BorderLayout.CENTER);
		chatBox.setLineWrap(true);
		chatBox.setFont(new Font("Serif",Font.PLAIN,15));
		GridBagConstraints left=new GridBagConstraints();
		left.anchor=GridBagConstraints.WEST;
		GridBagConstraints right=new GridBagConstraints();
		right.anchor=GridBagConstraints.EAST;
		right.weightx=2.0;
		southPanel.add(messageBox,left);
		southPanel.add(sendMessage,right);
		
		 sendMessage.addActionListener(new sendMessageButtonListener());
		
	}
	
	
	
	
	String username;
	String password;
	class enterServerButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			username=usernameTextField.getText();
			password=passwordTextField.getText();
			if(username.length()<1 || password.length()<1) {
				System.out.println("Pogresili ste username ili lozinku,pokusajte ponovo!");
			}else {
				preFrame.setVisible(false);
				display();
			}
		}
	}
	
	class sendMessageButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			if(messageBox.getText().length()<1) {
				//ne radi nista
				
			}else if(messageBox.getText().equals(".clear")) {
				chatBox.setText("Cleared all messages\n");
				messageBox.setText("");
			}else {
				chatBox.append("<" + username + ">:" + messageBox.getText()+"\n");
				messageBox.setText("");
				
			}
		}
	}

}
