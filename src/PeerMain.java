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
	JFrame loginFrame;
	JTextField usernameTextField;
	JTextField passwordTextField;
	String username;
	String password;
	BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
	



	public static void main(String[] args) throws Exception{
		try {
			UIManager.getSystemLookAndFeelClassName();
			}catch(Exception e) {
				e.printStackTrace();
			}
		PeerMain mainGui =new PeerMain();
		mainGui.displayLogin();

			}
	
	
	
			private void displayLogin() {
				loginFrame=new JFrame("Login Chat");
				JLabel EnterUsernameLabel=new JLabel("Enter username:");
				JLabel EnterPasswordLabel=new JLabel("Enter password:");
				
				usernameTextField = new JTextField();
				passwordTextField = new JTextField();
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
				loginPanel.add(EnterPasswordLabel, leftBox);
				loginPanel.add(passwordTextField, rightBox);
				loginFrame.add(BorderLayout.CENTER, loginPanel);
				loginFrame.add(BorderLayout.SOUTH, loginButton);
				loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				loginFrame.setVisible(true);
				loginFrame.setSize(700,400);
				loginButton.addActionListener(new enterLoginButtonListener());
				
			
			}	
			
			
		
			
			class enterLoginButtonListener implements ActionListener{
				
				String[] usernamePort;
				public void actionPerformed(ActionEvent event) {
					BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
					System.out.println("Unesite username i port");
					
					username = usernameTextField.getText();
					password = passwordTextField.getText();
					loginFrame.dispose();
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
						
						ServerThread serverThread;
						try {
								serverThread = new ServerThread(usernamePort[1]);
							
								serverThread.start();
								new PeerMain().updateListenToPeers(bufferedReader, usernamePort[0], serverThread);
							} catch (IOException e) {
							
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						
					}
				}
				
				

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
			

			
			
		

	}


