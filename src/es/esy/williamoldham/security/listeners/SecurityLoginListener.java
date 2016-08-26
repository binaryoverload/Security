package es.esy.williamoldham.security.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import es.esy.williamoldham.security.Update;
import es.esy.williamoldham.security.utils.PlayerUtilities;

public class SecurityLoginListener implements Listener{

	private boolean needsUpdate;
	private Update update;

	public SecurityLoginListener(boolean needsUpdate, Update update) {
		this.needsUpdate = needsUpdate;
		this.update = update;
	}


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){

		Player player = e.getPlayer();

		if ((player.hasPermission("security.updatenotify") || player.hasPermission("security.*") || PlayerUtilities.hasOP(player)) && needsUpdate) {
			player.sendMessage("[Security Updater] Found new version: " + update.getNewVer() + "! (Your version is " + update.getOldVer() + ")");
			player.sendMessage("[Security Updater] Download here: " + update.getURL());
		}

	}


}


