import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadThread extends Thread {
private ServerThread serverThread;
private Socket socket;
private PrintWriter printWriter;

public ServerThreadThread(Socket socket,ServerThread serverThread) {
	this.socket=socket;
	this.serverThread=serverThread;
}
public void run() {
	System.out.println("Call from  ServerThread");
	try {
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.printWriter=new PrintWriter(socket.getOutputStream(),true);
		while(true)
			serverThread.sendMessage(bufferedReader.readLine());
		
	} catch (Exception e) {
		serverThread.getsetThreads().remove(this);
		
	}
	
}

public PrintWriter getPrintWriter() {
	return printWriter;
}


}
