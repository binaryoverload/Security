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

import es.esy.williamoldham.security.utils.PlayerUtilities;
import es.esy.williamoldham.security.utils.Utilities;

public class Main extends JavaPlugin implements Listener{

	public List<Material> blackList = new ArrayList<Material>();

	private FileConfiguration config;

	public static File log;

	public void onEnable() {

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
		
		Bukkit.getServer().getPluginManager().registerEvents(new SecurityListener(blackList, config, this), this);
	}

}
