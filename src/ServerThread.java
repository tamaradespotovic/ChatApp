import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread{
	private ServerSocket serverSocket;//pravimo soket
	private Set<ServerThreadThread> skupNiti=new HashSet<ServerThreadThread>();
	//konstruktor sa portom 
	public ServerThread(String portNum) throws IOException{
		serverSocket=new ServerSocket(Integer.valueOf(portNum));//inicijalizujemo sa tim portom
	}

}
