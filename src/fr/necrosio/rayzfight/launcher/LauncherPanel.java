
package fr.necrosio.rayzfight.launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener 
{
	
	private Image background = Swinger.getResource("Launcher.png");
	private Saver saver = new Saver(new File(Launcher.RF_DIR, "launcher.properties"));
	private JTextField usernameField = new JTextField(saver.get("username"));
	private JPasswordField passwordField = new JPasswordField();
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("inv.png"));
	private STexturedButton quitButton = new STexturedButton(Swinger.getResource("inv.png"), Swinger.getResource("x.png"));
	private STexturedButton hideButton = new STexturedButton(Swinger.getResource("inv.png"), Swinger.getResource("inv.png"));
	private STexturedButton ramButton = new STexturedButton(Swinger.getResource("inv.png"), Swinger.getResource("w.png"));

	
	private SColoredBar progressBar = new SColoredBar(Color.DARK_GRAY,Color.BLACK);
	private JLabel infoLabel = new JLabel("Clique sur Jouer !", SwingConstants.CENTER);
	
	private RamSelector ramSelector = new RamSelector(new File(Launcher.RF_DIR, "ram.txt"));

	
	public LauncherPanel() { 
		this.setLayout(null);
		this.setBackground(Swinger.TRANSPARENT);
		
		usernameField.setForeground(Color.DARK_GRAY);
		usernameField.setFont(usernameField.getFont().deriveFont(42F));
		usernameField.setCaretColor(Color.black);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(300, 340, 320, 80);
		this.add(usernameField);
		
		
		playButton.setBounds (370, 473, 305, 100);
		playButton.addEventListener(this);
		this.add(playButton);
		
		quitButton.setBounds (970, 4, 27, 27);
		quitButton.addEventListener(this);
		this.add(quitButton);
		

		
		
		hideButton.setBounds (240, 192, 42, 15);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setBounds (25, 576, 950, 20);
		this.add(progressBar);
		
		infoLabel.setForeground(Color.GRAY);
		infoLabel.setBounds(-70, 780, 784, 10);
		this.add(infoLabel);
		
		this.ramButton.addEventListener(this);
		this.ramButton.setBounds(929, 2, 30, 30);
		this.add(ramButton);
	}
	
	@Override
	public void onEvent (SwingerEvent e) {
		if (e.getSource() == playButton) {
			setFieldsEnabled(false);
			
			if(usernameField.getText().replaceAll(" ", "").length()== 0 ) {
				JOptionPane.showMessageDialog(this, "Erreur, veuilliez entrée un Pseudo  valides", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText());
					} catch (AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur impossible de se conecter :" + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}
					
					try {
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptThread();
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur impossible de mettre le jeu a jour :" + e, "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}
					try {
						Launcher.launch();
					} catch (LaunchException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur impossible de lancer le jeu :" + e, "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
					}
					
				}
			};
			t.start();
			saver.set("username", usernameField.getText());
			
			
		} else if(e.getSource() == quitButton)
			System.exit(0);
		else if(e.getSource() == hideButton)
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);	
		else if (e.getSource() == this.ramButton)
			ramSelector.display();
		}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this );
	}

	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);	
		passwordField.setEnabled(enabled);	
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getprogressbar() {
		return progressBar;
	}
	
	public void setInfoLabel(String text){
		infoLabel.setText(text);
	}
	
	
	public RamSelector getRamSelector() 
	{
		return ramSelector;
	}
	
	
	
	
	
	
	
	
}
