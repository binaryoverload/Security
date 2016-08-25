package es.esy.williamoldham.security.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Utilities {
	
	static File log;
	static FileConfiguration config;
	
	public Utilities(File log, FileConfiguration config) {
		Utilities.log = log;
		Utilities.config = config;
	}

	public static String getCurrTimeString(){
		SimpleDateFormat currTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return currTime.format(new Date());
	}

	public static void writeTimeStampedMessage(String message){
		String timeStamp = getCurrTimeString();

		try {
			FileWriter fw = new FileWriter(log, true);
			fw.write("[" + timeStamp + "] " + message + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String convertLocToString(Location loc){
		String location = "";
		location += "X:" + Math.round(loc.getX()) + " ";
		location += "Y:" + Math.round(loc.getY()) + " ";
		location += "Z:" + Math.round(loc.getZ()) + " ";
		location += "World:" + loc.getWorld().getName() + " ";
		return location;
	}
	
	

	public static String color(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	

	public static String prefixAndColor(String string){
		return color(config.getString("prefix") + "&r " + string);
	}
	
}
