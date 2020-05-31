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
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
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
	JFrame loginFrame;
	JFrame usersFrame = new JFrame("Users");
	JTextField usernameTextField;
	JTextField portTextField;
	JTextField usersPortField;
	//ovo je potrebno za prikazivanje chat
	JFrame chatFrame=new JFrame("Chat ");
	JButton sendMessage;
	JButton addUsersButton;
	JTextField messageBox;
	JTextArea chatBox;
	String username;
	String myPort;
	String port;
	String usersPort;
	ServerThread serverThread;
	BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
	
	
	
	public String getUsersPort() {
		return usersPort;
	}



	public void setUsersPort(String usersPort) {
		this.usersPort = usersPort;
	}



	public static void main(String[] args) throws Exception{
	
		
		try {
			UIManager.getSystemLookAndFeelClassName();
			}catch(Exception e) {
				e.printStackTrace();
			}
		PeerMain mainGui =new PeerMain();
		mainGui.displayLogin();

			}
	
	
	
		//predisplay je potreban za logovanje,nakon toga pravimo display da bi prikazali  chat u kom mozemo da saljemo poruke
			private void displayLogin() {
				usersFrame.setVisible(false);
				chatFrame.setVisible(false);
				loginFrame=new JFrame("Login Chat");
				JLabel EnterUsernameLabel=new JLabel("Enter username:");
				JLabel EnterPortLabel=new JLabel("Enter port:");
				
				usernameTextField = new JTextField();
				portTextField = new JTextField();
				JButton loginButton = new JButton("Login");
				JPanel loginPanel = new JPanel(new GridBagLayout());
				GridBagConstraints rightBox = new GridBagConstraints();
				rightBox.anchor = GridBagConstraints.EAST;
				GridBagConstraints leftBox = new GridBagConstraints();
				leftBox.anchor = GridBagConstraints.WEST;
				
				rightBox.weightx = 2.0;
				rightBox.fill = GridBagConstraints.HORIZONTAL;
				rightBox.gridwidth = GridBagConstraints.REMAINDER;
				loginPanel.add(EnterUsernameLabel, leftBox);
				loginPanel.add(usernameTextField, rightBox);
				loginPanel.add(EnterPortLabel, leftBox);
				loginPanel.add(portTextField, rightBox);
				loginFrame.add(BorderLayout.CENTER, loginPanel);
				loginFrame.add(BorderLayout.SOUTH, loginButton);
				
				loginFrame.setVisible(true);
				loginFrame.setSize(700,400);
				loginButton.addActionListener(new enterServerButtonListener());
				
			}
			
			private void displayUsers() {
				chatFrame.setVisible(false);
				JLabel EnterOtherUsersPorts=new JLabel("Enter port of users you want to talk:");
				usersPortField = new JTextField();
				JPanel usersPanel = new JPanel(new GridBagLayout());
				GridBagConstraints rightBox = new GridBagConstraints();
				rightBox.anchor = GridBagConstraints.EAST;
				GridBagConstraints leftBox = new GridBagConstraints();
				leftBox.anchor = GridBagConstraints.WEST;
				
				rightBox.weightx = 2.0;
				rightBox.fill = GridBagConstraints.HORIZONTAL;
				rightBox.gridwidth = GridBagConstraints.REMAINDER;
			
				usersPanel.add(EnterOtherUsersPorts, leftBox);
				usersPanel.add(usersPortField, rightBox);
				addUsersButton=new JButton("Start chat with");
				usersFrame.add(BorderLayout.CENTER, usersPanel);
				usersFrame.add(BorderLayout.SOUTH,addUsersButton);
				usersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				usersFrame.setSize(400, 400);
				usersFrame.setVisible(true);
				
				addUsersButton.addActionListener(new addUsersButtonListener());
	
			}
			
			
			private void displayChat() {
				chatFrame.setVisible(true);
				chatFrame.setSize(470,300);
				chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				JPanel southPanel=new JPanel();
				chatFrame.add(BorderLayout.SOUTH,southPanel);
				
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
				
				chatFrame.add(new JScrollPane(chatBox),BorderLayout.CENTER);

				sendMessage.addActionListener(new sendMessageButtonListener());
				
			}
			
		
			
			public JTextField getMessageBox() {
				return messageBox;
			}



			public void setMessageBox(JTextField messageBox) {
				this.messageBox = messageBox;
			}



			class enterServerButtonListener implements ActionListener{
				public void actionPerformed(ActionEvent event) {
					username = usernameTextField.getText();
					myPort = portTextField.getText();
					
					System.out.println("username: " + username + ", my port: "+ myPort);
				
					
					
					
			
					try {
							serverThread = new ServerThread(myPort);
						
							serverThread.start();
							
						} catch (IOException e) {
						
							e.printStackTrace();
						}
					
					loginFrame.setVisible(false);
					displayUsers();
					
							
				}

				
			}
			
			class addUsersButtonListener implements ActionListener{
				public void actionPerformed(ActionEvent event) {
					usersPort = usersPortField.getText();
					
					System.out.println("Dodajem Useree" + usersPort);
							
							
					try {
						
						new PeerMain().updateListenToPeers(usersPort, username);
						System.out.println("Gotovo");
						
				
					} catch (Exception e) {
						
						e.printStackTrace();
					}
							
		
					usersFrame.setVisible(false);
					displayChat();
					
							
				}

				
			}
			
			
			public void updateListenToPeers(String usersPort,String username )throws Exception {
				System.out.println(" >enter(space separated) hostname:port ");
				System.out.println("peers to receive message from(s to skip)");
				String[] inputValues=usersPort.split(" ");
	
				for(int i=0;i<inputValues.length;i++) {
					//hostname:port
					String[] address=inputValues[i].split(":");
					Socket socket=null;
					try {
						socket=new Socket(address[0],Integer.valueOf(address[1]));
						new PeerThread(socket).start();
						//hocemo da otvorimo chat
						
					}catch(Exception e) {
						e.printStackTrace();
						
						if(socket !=null)
							socket.close();
						else 
							System.out.println("Invalid input,skipping to next step");
					}
			
				}
				
				communicate(bufferedReader,username,serverThread);
				

			}
			
			public void printMessage(String message) {
				chatBox.append(message);
			}
			
			public void communicate(BufferedReader bufferedReader,String username,ServerThread serverThread) {
				try {
					System.out.println("> you can now communicate(e to exit,c to change)");
					boolean flag=true;
					while(flag) {
						String message=bufferedReader.readLine();
						if(message.equals("e")) {
							flag=false;
							break;
						}else {
							StringWriter stringWriter=new StringWriter();
							Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder().add("username",username).add("message",message).build());
							serverThread.sendMessage(stringWriter.toString());
							
						}
					}
					System.exit(0);
					
				}catch(Exception e) {
					
				}
			}
			
			class sendMessageButtonListener implements ActionListener{
				public void actionPerformed(ActionEvent event) {
					if(messageBox.getText().length()<1) {
						return;
						
					}else if(messageBox.getText().equals(".clear")) {
						chatBox.setText("Cleared all messages\n");
						messageBox.setText("");
					}else {
						
							try {
								
								messageBox.setText("");
							}catch(Exception e) {
								e.printStackTrace();
						
				
						
							}}
				}
			}
		

	}


