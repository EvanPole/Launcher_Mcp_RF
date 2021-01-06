package fr.necrosio.rayzfight.launcher;

import javax.swing.JFrame;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame {
	
	private static LauncherFrame instance;
	private LauncherPanel launcherPanel;
	
	public LauncherFrame() {
		this.setTitle("RayzFight v1.0 Launcher");
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setBackground(Swinger.TRANSPARENT);
		this.setIconImage (Swinger.getResource("logo.png"));
		this.setContentPane(launcherPanel = new LauncherPanel());
		
		
		WindowMover mover = new WindowMover(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
		
		this.setVisible(true);
	}
	
	
	
	
	public static void main(String[] args) {
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/fr/necrosio/rayzfight/launcher/resources/");
	
		instance = new LauncherFrame();
	}

	public static LauncherFrame getInstance() {
		return instance;
	}
	
	public LauncherPanel getlauncherPanel () {
		return this.launcherPanel;
	}
	
}