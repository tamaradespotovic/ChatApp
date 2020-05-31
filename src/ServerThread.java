import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread{
	private ServerSocket serverSocket;//deklarise soket
	private Set<ServerThreadThread> skupNiti = new HashSet<ServerThreadThread>();//instancira
	
//konstruktor sa portom 
	public ServerThread(String portNum) throws IOException{
	serverSocket=new ServerSocket(Integer.valueOf(portNum));//inicijalizujemo sa tim portom
	}
	//posto smo pokrenuli nit ServerThread ovde mora run metod
	public void run() {
		System.out.println("Uslo u server thread");
		try {
			while(true) {
				ServerThreadThread novaNit=new ServerThreadThread(serverSocket.accept(),this);
				skupNiti.add(novaNit);
				novaNit.start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//metod za slanje poruka
	void sendMessage(String message) {
		try {
			skupNiti.forEach(t -> t.getPrintWriter().println(message));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Set<ServerThreadThread> getSkupNiti() {
		return skupNiti;
	}

	
	

}
