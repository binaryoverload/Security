package es.esy.williamoldham.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.plugin.Plugin;

public class SpigotUpdater
  extends Thread
{
  private final Plugin plugin;
  private final int id;
  private final boolean log;
  private URL url;
  private String currentVersion;
  private boolean needsUpdate = false;
  private Update update;
  
  public SpigotUpdater(Plugin plugin, int resourceID)
    throws IOException
  {
    this(plugin, resourceID, true);
  }
  
  public SpigotUpdater(Plugin plugin, int resourceID, boolean log)
    throws IOException
  {
    if (plugin == null) {
      throw new IllegalArgumentException("Plugin cannot be null");
    }
    if (resourceID == 0) {
      throw new IllegalArgumentException("Resource ID cannot be null (0)");
    }
    this.plugin = plugin;
    this.id = resourceID;
    this.log = log;
    this.url = new URL("http://www.spigotmc.org/api/general.php");
    
    super.start();
  }
  
  public synchronized void start() {}
  
  public void run()
  {
    if (this.log) {
      this.plugin.getLogger().info("[Updater] Searching for updates.");
    }
    HttpURLConnection con = null;
    try
    {
      con = (HttpURLConnection)this.url.openConnection();
      con.setDoOutput(true);
      con.setRequestMethod("POST");
      con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + id).getBytes("UTF-8"));
      
      String version = new BufferedReader(new InputStreamReader(
              con.getInputStream())).readLine();
      
      if (version.length() <= 7) {
          currentVersion = version;
      }
      
      
      if (currentVersion == null)
      {
        if (this.log)
        {
          this.plugin.getLogger().warning("[Updater] Invalid response received.");
          this.plugin.getLogger().warning("[updater] Either the author of this plugin has configured the updater wrong, or the API is experiencing some issues.");
        }
        return;
      }
      if (!currentVersion.equals(this.plugin.getDescription().getVersion()))
      {
        this.plugin.getLogger().info("[Updater] Found new version: " + currentVersion + "! (Your version is " + this.plugin.getDescription().getVersion() + ")");
        this.plugin.getLogger().info("[Updater] Download here: http://www.spigotmc.org/resources/" + this.id);
        needsUpdate = true;
        update = new Update(currentVersion, this.plugin.getDescription().getVersion(), "http://www.spigotmc.org/resources/" + this.id);
      }
      else if (this.log)
      {
        this.plugin.getLogger().info("[Updater] Plugin is up-to-date.");
      }
    }
    catch (IOException e)
    {
      if (this.log)
      {
        if (con != null) {
          try
          {
            int code = con.getResponseCode();
            this.plugin.getLogger().warning("[Updater] API connection returned response code " + code);
          }
          catch (IOException localIOException1) {}
        }
        e.printStackTrace();
      }
    }
  }
  
  public boolean needsUpdate(){
	  return needsUpdate;
  }
  
  public Update getUpdate(){
	  return update;
  }
}
