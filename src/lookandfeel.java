import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class lookandfeel {

	public static void main(String[] args) {
		JFrame frame=new JFrame();
		frame.setSize(250,250);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle("Chat");
		
		try {
			//use the system look and feel for the swing application
			String className=UIManager.getSystemLookAndFeelClassName();
			System.out.println("Class name=" + className);
			UIManager.setLookAndFeel(className);
	}catch(Exception e) {
		e.printStackTrace();
	}
		SwingUtilities.updateComponentTreeUI(frame);
		frame.setVisible(true);
}
}