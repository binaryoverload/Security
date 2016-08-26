package es.esy.williamoldham.security;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigUpdater {
	
	final String configVersion = "1.7";
	
	private Plugin p;

	public ConfigUpdater(Plugin p) {
		this.p = p;
	}
	
	
	public void updateConfig() {
        HashMap<String, Object> newConfig = getConfigVals();
        FileConfiguration c = p.getConfig();
        for (String var : c.getKeys(false)) {
            newConfig.remove(var);
        }
        if (newConfig.size()!=0) {
            for (String key : newConfig.keySet()) {
                c.set(key, newConfig.get(key));
            }
            try {
                c.set("version", configVersion);
                c.save(new File(p.getDataFolder(), "config.yml"));
            } catch (IOException e) {}
        }
    }
    public HashMap<String, Object> getConfigVals() {
        HashMap<String, Object> var = new HashMap<>();
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringFromInputStream(p.getResource("/config.yml")));
        } catch (InvalidConfigurationException e) {}
        for (String key : config.getKeys(false)) {
            var.put(key, config.get(key));
        }
        return var;
    }
    public String stringFromInputStream(InputStream in) {
    	
    	Scanner scanner = new Scanner(in);
    	
    	String temp = scanner.useDelimiter("\\A").next();
    	
    	scanner.close();
    	
        return temp;
    }
	
	
}
