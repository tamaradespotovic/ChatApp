import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;



public class PeerMain   {
	PeerMain mainGUI;
	JFrame preFrame;
	JTextField usernameTextField;
	JTextField passwordTextField;
	//ovo je potrebno za prikazivanje chat
	JFrame newFrame=new JFrame("Chat ");
	JButton sendMessage;
	JTextField messageBox;
	JTextArea chatBox;
	String username;
	String password;
	String port;
	
	public static void main(String[] args) throws Exception{
	
		
		try {
			UIManager.getSystemLookAndFeelClassName();
			}catch(Exception e) {
				e.printStackTrace();
			}
		PeerMain mainGui =new PeerMain();
		mainGui.preDisplay();

			}
	
	
	
		//predisplay je potreban za logovanje,nakon toga pravimo display da bi prikazali  chat u kom mozemo da saljemo poruke
			private void preDisplay() {
				newFrame.setVisible(false);
				preFrame=new JFrame("Login Chat");
				JLabel EnterUsernameLabel=new JLabel("Enter username:");
				JLabel EnterPasswordLabel=new JLabel("Enter password:");
				usernameTextField = new JTextField();
				passwordTextField = new JTextField();
				JButton loginButton = new JButton("Login");
				JPanel prePanel = new JPanel(new GridBagLayout());
				GridBagConstraints rightBox = new GridBagConstraints();
				rightBox.anchor = GridBagConstraints.EAST;
				GridBagConstraints leftBox = new GridBagConstraints();
				leftBox.anchor = GridBagConstraints.WEST;
				
				rightBox.weightx = 2.0;
				rightBox.fill = GridBagConstraints.HORIZONTAL;
				rightBox.gridwidth = GridBagConstraints.REMAINDER;
				prePanel.add(EnterUsernameLabel, leftBox);
				prePanel.add(usernameTextField, rightBox);
				prePanel.add(EnterPasswordLabel, leftBox);
				prePanel.add(passwordTextField, rightBox);
				preFrame.add(BorderLayout.CENTER, prePanel);
				preFrame.add(BorderLayout.SOUTH, loginButton);
				
				preFrame.setVisible(true);
				preFrame.setSize(300,300);
				loginButton.addActionListener(new enterServerButtonListener());
				
			}
			
			
			
			private void display() {
				newFrame.setVisible(true);
				newFrame.setSize(470,300);
				newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				JPanel southPanel=new JPanel();
				newFrame.add(BorderLayout.SOUTH,southPanel);
				
				southPanel.setLayout(new GridBagLayout());
				southPanel.setBackground(Color.ORANGE);
				GridBagConstraints left = new GridBagConstraints();
				left.anchor = GridBagConstraints.WEST;
				GridBagConstraints right = new GridBagConstraints();
				right.anchor = GridBagConstraints.EAST;
				right.weightx = 2.0;
				
				messageBox = new JTextField(30);
				sendMessage = new JButton("Send Message");
				
				southPanel.add(messageBox,left);
				southPanel.add(sendMessage,right);
				
				
				chatBox = new JTextArea();
				chatBox.setEditable(false);
				chatBox.setLineWrap(true);
				chatBox.setFont(new Font("Serif",Font.PLAIN,15));
				
				newFrame.add(new JScrollPane(chatBox),BorderLayout.CENTER);

				sendMessage.addActionListener(new sendMessageButtonListener());
				
			}
			
		
			
			
			
			class enterServerButtonListener implements ActionListener{
				
				String[] usernamePort;
				public void actionPerformed(ActionEvent event) {
					BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
					System.out.println("Unesite username i port");
					
					username = usernameTextField.getText();
					password = passwordTextField.getText();
					try {
						usernamePort = bufferedReader.readLine().split(" ");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("username: " + username + ", password: "+ password);
					if(!usernamePort[0].equals(username)) {
						System.out.println("Pogresili ste username, pokusajte ponovo!");
					
					}else if(usernamePort[1].length()<4){
						System.out.println("Unesite port, ne manji od 4 cifre.");
					}else{
						//necemo da zausmemo port ako nismo uneli ispravne kredencijale
						ServerThread serverThread;
						try {
								serverThread = new ServerThread(usernamePort[1]);
								serverThread.start();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						preFrame.setVisible(false);
						display();
					}
				}
//				public String getUsername() {
//					return username;
//				}
//				public String getPassword() {
//					return password;
//				}
//				public String getPort() {
//					return usernamePort[1];
//				}
				
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


