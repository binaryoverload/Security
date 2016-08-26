package es.esy.williamoldham.security;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import es.esy.williamoldham.security.listeners.SecurityLoginListener;
import es.esy.williamoldham.security.listeners.SecurityMoveListener;
import es.esy.williamoldham.security.utils.PlayerUtilities;
import es.esy.williamoldham.security.utils.Utilities;

public class Main extends JavaPlugin implements Listener{

	public List<Material> blackList = new ArrayList<Material>();

	public static File log;
	
	private Update update;
	private boolean needsUpdate;

	public void onEnable() {

		saveDefaultConfig();
		
		ConfigUpdater cU = new ConfigUpdater(this);
		
		cU.updateConfig();

		//getConfig().options().copyDefaults(true);
		
		SpigotUpdater su;
		
		try {
			su = new SpigotUpdater(this, 28397);
			if(su.needsUpdate()){
				update = su.getUpdate();
				needsUpdate = true;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		FileConfiguration config = getConfig();

		File pluginDir = getDataFolder();

		String timeStamp = new SimpleDateFormat("dd-MM-HH-mm").format(new Date());

		File logDir = new File(pluginDir, "logs");
		if(!logDir.exists()){
			logDir.mkdir();
		}

		log = new File(logDir, "log " + timeStamp + ".txt");

		try {
			FileWriter fw = new FileWriter(log);
			fw.write("[" + Utilities.getCurrTimeString() + "] Log Initilised\n" );
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		

		for(String material : config.getStringList("blacklist")){
			Material temp = Material.getMaterial(material);
			blackList.add(temp);
		}
		
		@SuppressWarnings("unused")
		Utilities utils = new Utilities(log, config);
		@SuppressWarnings("unused")
		PlayerUtilities pUtils = new PlayerUtilities(this, config);
		
		Bukkit.getServer().getPluginManager().registerEvents(new SecurityMoveListener(blackList, config, this), this);
		if(needsUpdate){
			Bukkit.getServer().getPluginManager().registerEvents(new SecurityLoginListener(needsUpdate, update), this);
		}
	}
	


}
