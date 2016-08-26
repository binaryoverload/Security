package es.esy.williamoldham.security;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import es.esy.williamoldham.security.utils.PlayerUtilities;
import es.esy.williamoldham.security.utils.Utilities;

public class SecurityListener implements Listener{
	
	private HashMap<Player, Integer> messageDelays = new HashMap<Player, Integer>();

	private List<Material> blackList;
	
	private FileConfiguration config;
	
	public SecurityListener(List<Material> blackList, FileConfiguration config, Plugin p) {
		this.blackList = blackList;
		this.config = config;
		
		int messageDelay = config.getInt("message-delay");

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {

			public void run() {

				for(Player p : messageDelays.keySet()){
					messageDelays.remove(p);
				}

			}

		}, 0, 20 * messageDelay);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Location fromLoc = e.getFrom();
		Location toLoc = e.getTo();

		Player player = e.getPlayer();

		if (player.hasPermission("security.bypass") || player.hasPermission("security.*") || PlayerUtilities.hasOP(player)) {
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

						PlayerUtilities.warn(player);
						player.sendMessage(Utilities.color(config.getString("prefix") + " " + config.getString("disallow-message")));

						PlayerUtilities.notify(player.getName());
						PlayerUtilities.log(player.getName(), player.getLocation());

						messageDelays.put(player, 1);
					}
				} else {
					PlayerUtilities.warn(player);

					player.sendMessage(Utilities.prefixAndColor(config.getString("disallow-message")));

					PlayerUtilities.notify(player.getName());
					PlayerUtilities.log(player.getName(), player.getLocation());

					messageDelays.put(player, 1);
				}
				return;
			}
		}
	}
	
}
