import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread{
	private ServerSocket serverSocket;
	private Set<ServerThreadThread> setThreads = new HashSet<ServerThreadThread>();
	
	public ServerThread(String portNum) throws IOException{
	serverSocket=new ServerSocket(Integer.valueOf(portNum));
	}
	
	public void run() {
		System.out.println("Entered in server thread");
		try {
			while(true) {
				ServerThreadThread newThread=new ServerThreadThread(serverSocket.accept(),this);
				setThreads.add(newThread);
				newThread.start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void sendMessage(String message) {
		try {
			setThreads.forEach(t -> t.getPrintWriter().println(message));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Set<ServerThreadThread> getsetThreads() {
		return setThreads;
	}

	
	

}
