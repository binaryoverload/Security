package es.esy.williamoldham.security.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerUtilities {
	
	private static HashMap<Player, Integer> warnings = new HashMap<Player, Integer>();
	
	private static FileConfiguration config;
	
	public PlayerUtilities(Plugin p, FileConfiguration config){
		setup(p);
		PlayerUtilities.config = config;
	}

	private void setup(Plugin p) {
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {

			public void run() {

				warnings.clear();

			}

		}, 0, 20 * 30);
		
	}

	public static void notify(String playerName){
		if(config.getBoolean("notify-admins")){
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				if(p.hasPermission("security.notify") || p.hasPermission("security.*") || hasOP(p)) {
					String message = config.getString("notify-message");

					p.sendMessage(Utilities.prefixAndColor(message.replace("$PLAYER", playerName)));
				}
			}
		}
	}

	public static void log(String playerName, Location loc){
		if(config.getBoolean("log")){
			String CoOrds = Utilities.convertLocToString(loc);
			String logMessage = config.getString("log-message");
			logMessage = logMessage.replace("$COORDS", CoOrds);
			logMessage = logMessage.replace("$PLAYER", playerName);
			Utilities.writeTimeStampedMessage(logMessage);
		}
	}

	

	public static boolean hasOP(Player p){
		if(config.getBoolean("op-bypass")){
			return p.isOp();
		} else {
			return false;
		}
	}

	public static void warn(Player p){
		if(config.getBoolean("warnings-enabled")){
			if(warnings.containsKey(p)){
				int numWarnings = warnings.get(p);
				int maxWarnings = config.getInt("warnings-max");

				if(numWarnings == maxWarnings){
					p.sendMessage(Utilities.prefixAndColor(config.getString("warnings-message")));
					String command = config.getString("warnings-command").replace("$PLAYER", p.getName());
					command = command.replace("/", "");

					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				} else {
					warnings.put(p, numWarnings + 1);

					if(config.getBoolean("warnings-warn-msg-enabled")){
						String message = Utilities.prefixAndColor(config.getString("warnings-warn-message").replace("$WARNINGS", String.valueOf(maxWarnings - numWarnings)));
						p.sendMessage(message);
					}
				}
			} else {
				warnings.put(p, 0);
			}
		}
	}
	
}
