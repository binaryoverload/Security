package es.esy.williamoldham.security;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{

	List<Material> blackList = new ArrayList<Material>();

	HashMap<Player, Integer> messageDelays = new HashMap<Player, Integer>();

	HashMap<Player, Integer> warnings = new HashMap<Player, Integer>();

	FileConfiguration config;

	File log;

	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		saveDefaultConfig();

		//getConfig().options().copyDefaults(true);

		config = getConfig();

		File pluginDir = getDataFolder();

		String timeStamp = new SimpleDateFormat("dd-MM-HH-mm").format(new Date());

		File logDir = new File(pluginDir, "logs");
		if(!logDir.exists()){
			logDir.mkdir();
		}

		log = new File(logDir, "log " + timeStamp + ".txt");

		try {
			FileWriter fw = new FileWriter(log);
			fw.write("[" + getCurrTimeString() + "] Log Initilised\n" );
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int messageDelay = config.getInt("message-delay");

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			public void run() {

				for(Player p : messageDelays.keySet()){
					messageDelays.remove(p);
				}

			}

		}, 0, 20 * messageDelay);

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			public void run() {

				warnings.clear();

			}

		}, 0, 20 * 30);

		for(String material : config.getStringList("blacklist")){
			Material temp = Material.getMaterial(material);
			blackList.add(temp);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Location fromLoc = e.getFrom();
		Location toLoc = e.getTo();

		Player player = e.getPlayer();

		if (player.hasPermission("security.bypass") || player.hasPermission("security.*") || hasOP(player)) {
			return;
		}
		if (player.getLocation().getBlock().getType() == Material.AIR){
			if (blackList.contains(toLoc.getBlock().getRelative(BlockFace.DOWN).getType())) {
				
				Location moveTo = fromLoc;
				
				moveTo.setPitch(toLoc.getPitch());
				moveTo.setYaw(toLoc.getYaw());
				moveTo.setDirection(toLoc.getDirection());
				
				
				player.teleport(moveTo);


				if(messageDelays.containsKey(player)){
					if(messageDelays.get(player) == 0){

						warn(player);
						player.sendMessage(color(config.getString("prefix") + " " + config.getString("disallow-message")));

						notify(player.getName());
						log(player.getName(), player.getLocation());

						messageDelays.put(player, 1);
					}
				} else {
					warn(player);

					player.sendMessage(prefixAndColor(config.getString("disallow-message")));

					notify(player.getName());
					log(player.getName(), player.getLocation());

					messageDelays.put(player, 1);
				}
				return;
			}
		}
	}

	public String color(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public void notify(String playerName){
		if(config.getBoolean("notify-admins")){
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				if(p.hasPermission("security.notify") || p.hasPermission("security.*") || hasOP(p)) {
					String message = config.getString("notify-message");

					p.sendMessage(prefixAndColor(message.replace("$PLAYER", playerName)));
				}
			}
		}
	}

	public void log(String playerName, Location loc){
		if(config.getBoolean("log")){
			String CoOrds = convertLocToString(loc);
			String logMessage = config.getString("log-message");
			logMessage = logMessage.replace("$COORDS", CoOrds);
			logMessage = logMessage.replace("$PLAYER", playerName);
			writeTimeStampedMessage(logMessage);
		}
	}

	public String getCurrTimeString(){
		SimpleDateFormat currTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return currTime.format(new Date());
	}

	public void writeTimeStampedMessage(String message){
		String timeStamp = getCurrTimeString();

		try {
			FileWriter fw = new FileWriter(log, true);
			fw.write("[" + timeStamp + "] " + message + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String convertLocToString(Location loc){
		String location = "";
		location += "X:" + Math.round(loc.getX()) + " ";
		location += "Y:" + Math.round(loc.getY()) + " ";
		location += "Z:" + Math.round(loc.getZ()) + " ";
		location += "World:" + loc.getWorld().getName() + " ";
		return location;
	}

	public boolean hasOP(Player p){
		if(config.getBoolean("op-bypass")){
			return p.isOp();
		} else {
			return false;
		}
	}

	public void warn(Player p){
		if(config.getBoolean("warnings-enabled")){
			if(warnings.containsKey(p)){
				int numWarnings = warnings.get(p);
				int maxWarnings = config.getInt("warnings-max");

				if(numWarnings == maxWarnings){
					p.sendMessage(prefixAndColor(config.getString("warnings-message")));
					String command = config.getString("warnings-command").replace("$PLAYER", p.getName());
					command = command.replace("/", "");

					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				} else {
					warnings.put(p, numWarnings + 1);

					if(config.getBoolean("warnings-warn-msg-enabled")){
						String message = prefixAndColor(config.getString("warnings-warn-message").replace("$WARNINGS", String.valueOf(maxWarnings - numWarnings)));
						p.sendMessage(message);
					}
				}
			} else {
				warnings.put(p, 0);
			}
		}
	}

	public String prefixAndColor(String string){
		return color(config.getString("prefix") + "&r " + string);
	}
}
