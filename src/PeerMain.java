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
						
						e1.printStackTrace();
					}
					System.out.println("username: " + username + ", password: "+ password);
					if(!usernamePort[0].equals(username)) {
						System.out.println("Pogresili ste username, pokusajte ponovo!");
					
					}else if(usernamePort[1].length()<4){
						System.out.println("Unesite port, ne manji od 4 cifre.");
					}else{
						//necemo da zausmemo port ako nismo uneli ispravne kredencijale
						//zato ovde tek uzimamo port
						ServerThread serverThread;
						preFrame.setVisible(false);
						display();
						try {
								serverThread = new ServerThread(usernamePort[1]);
							
								serverThread.start();
								
								try {
									
									new PeerMain().updateListenToPeers(bufferedReader, username, serverThread);
									
								} catch (Exception e) {
									
									e.printStackTrace();
								}
								
							} catch (IOException e) {
							
								e.printStackTrace();
							}
							
							
						
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
			
			
			public void updateListenToPeers(BufferedReader bufferedReader,String username,ServerThread serverThread )throws Exception {
				System.out.println(" >enter(space separated) hostname:port ");
				System.out.println("peers to receive message from(s to skip)");
				String input=bufferedReader.readLine();
				String[] inputValues=input.split(" ");
				if(!input.equals("s")) {
					for(int i=0;i<inputValues.length;i++) {
						//hostname:port
						String[] address=inputValues[i].split(":");
						System.out.println(address[1]);
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
				}
				communicate(bufferedReader,username,serverThread);
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
						}else if(message.equals("c")) {
							updateListenToPeers(bufferedReader,username,serverThread);
							
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


