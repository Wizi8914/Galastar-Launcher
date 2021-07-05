package fr.wizi.galastar.launcher;

import static fr.theshark34.swinger.Swinger.drawFullsizedImage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.launcher.util.UsernameSaver;
import static fr.theshark34.swinger.Swinger.*;

import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
	
	private Image background = getResource("background.png");
	
	private UsernameSaver saver = new UsernameSaver(Launcher.GS_INFOS);
	
	private JTextField usernameField = new JTextField(saver.getUsername(""));
	private JTextField passwordField = new JPasswordField();
	
	private STexturedButton connectButton = new STexturedButton(getResource("connectbutton.png"));
	private STexturedButton settingsButton = new STexturedButton(getResource("setting.png"));
	private STexturedButton quitButton = new STexturedButton(getResource("quit.png"));
	private STexturedButton hideButton = new STexturedButton(getResource("hide.png"));
	
	private SColoredBar progressBar = new SColoredBar(getTransparentWhite(100), getTransparentWhite(175));
	private JLabel infoLabel = new JLabel("Connectez vous pour commencer a jouer !", SwingConstants.CENTER);
	
	public LauncherPanel() {
		this.setLayout(null);
		
		usernameField.setForeground(Color.WHITE);
		usernameField.setFont(usernameField.getFont().deriveFont(25F));
		usernameField.setCaretColor(Color.WHITE);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(580, 193, 290, 52);
		this.add(usernameField);
		
		passwordField.setForeground(Color.WHITE);
		passwordField.setFont(usernameField.getFont());
		passwordField.setCaretColor(Color.WHITE);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setBounds(580, 327, 290, 52);
		this.add(passwordField);
		
		connectButton.setBounds(643, 400);
		connectButton.addEventListener(this);
		this.add(connectButton);
		
		settingsButton.setBounds(574, 400);
		this.add(settingsButton);
		
		quitButton.setBounds(950, 0);
		quitButton.addEventListener(this);
		this.add(quitButton);
		
		hideButton.setBounds(932, 0);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		progressBar.setBounds(0, 605, 975, 20);
		this.add(progressBar);
		
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(infoLabel.getFont().deriveFont(15F));
		infoLabel.setBounds(0, 601, 980, 25);
		this.add(infoLabel);
	}
	
	@Override
	public void onEvent(SwingerEvent event) {
		if(event.getSource() == connectButton) {
			setFieldsEnabled(false);
			
			if(usernameField.getText().replaceAll(" ", " ").length() == 0 || passwordField.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo ou un mot de passe valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptThread();
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de mettre le jeu a jour : " + e, "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}
					
					System.out.println("sa marche");
				}
				
			};
			t.start();
		} else if(event.getSource() == quitButton)
			System.exit(0);
		else if(event.getSource() == hideButton)
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		drawFullsizedImage(graphics, this, background);
	}
	
	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		connectButton.setEnabled(enabled);
	}

	public SColoredBar getProgressBar() {
		return progressBar;
	}
	
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}
	
}
