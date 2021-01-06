package fr.necrosio.rayzfight.launcher;

import java.io.File;
import java.util.Arrays;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;

public class Launcher {

	public static final GameVersion RF_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
	public static final GameInfos RF_INFOS = new GameInfos("Rayzfightv1", RF_VERSION, new GameTweak[] {});
	public static final File RF_DIR = RF_INFOS.getGameDir();

	private static AuthInfos authInfos;
	private static Thread updateThread;
	public static void auth(String username) throws AuthenticationException {
		authInfos = new AuthInfos(username, "sry", "nope");
	}

	public static void update() throws Exception {
		SUpdate su = new SUpdate("https://site.fr", RF_DIR);
		su.getServerRequester().setRewriteEnabled(true);
	updateThread = new Thread() {
			private int val;
			private int max;
			
			@Override
			public void run() {
				while(!this.isInterrupted()) {
					if(BarAPI.getNumberOfFileToDownload() == 0) {
						LauncherFrame.getInstance().getlauncherPanel().setInfoLabel("verification des fichiers");
						continue;
					}
					val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
					max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
					
					LauncherFrame.getInstance().getlauncherPanel().getprogressbar().setMaximum(max);
					LauncherFrame.getInstance().getlauncherPanel().getprogressbar().setValue(val);
					
					LauncherFrame.getInstance().getlauncherPanel().setInfoLabel("Telechargement des fichiers " +
						BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() + " " +
							Swinger.percentage(val, max) + "%");
				}
			}
		};
		updateThread.start();
		su.start();
		updateThread.interrupt();
	}
	
	
	
	public static void launch() throws LaunchException
	{
		ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(RF_INFOS, GameFolder.BASIC, authInfos);
		profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getInstance().getlauncherPanel().getRamSelector().getRamArguments()));
		ExternalLauncher launcher = new ExternalLauncher(profile);
		

		
		Process p = launcher.launch();
		LauncherFrame.getInstance().setVisible(false);
		
		try
		{
			Thread.sleep(5000L);
			p.waitFor();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.exit(0);
	    try {
	        p.waitFor();
	      } catch (InterruptedException interruptedException) {}
	      System.exit(0);
	}
	
	
	
	
	
	public static void interruptThread( ) {
		updateThread.interrupt();
	}
}



