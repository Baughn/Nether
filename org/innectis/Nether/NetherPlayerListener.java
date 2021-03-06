package org.innectis.Nether;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;

public class NetherPlayerListener extends PlayerListener
{

	private NetherMain main;
	
	public NetherPlayerListener(NetherMain plugin)
	{
		main = plugin;
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Location loc = event.getTo();
		World world = loc.getWorld();
		Block b = world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		if (!b.getType().equals(Material.PORTAL)) {
			// Not a portal.
			return;
		}
		
		if (world.getEnvironment().equals(Environment.NORMAL)) {
			// First of all see if there IS a nether yet
			// Here we use "netherworld"
			World nether = main.getServer().getWorld("netherworld");
			if (nether == null) {
				nether = main.getServer().createWorld("netherworld", Environment.NETHER);
			}
			
			if (!nether.getEnvironment().equals(Environment.NETHER)) {
				// Don't teleport to a non-nether world
				return;
			}
			
			
			// Try to find a portal near where the player should land
			Block dest = nether.getBlockAt(b.getX() / 8, b.getY(), b.getZ() / 8);
			NetherPortal portal = NetherPortal.findPortal(dest);
			if (portal == null) {
				portal = NetherPortal.createPortal(dest);
				main.log(event.getPlayer().getName() + " portals to Nether [NEW]");
			} else {
				main.log(event.getPlayer().getName() + " portals to Nether");
			}
			
			// Go!
			Location spawn = portal.getSpawn();
			event.getPlayer().teleportTo(spawn);
			event.setTo(spawn);
		} else if (world.getEnvironment().equals(Environment.NETHER)) {
			// For now just head to the first world there.
			World normal = main.getServer().getWorlds().get(0);
			
			if (!normal.getEnvironment().equals(Environment.NORMAL)) {
				// Don't teleport to a non-normal world
				return;
			}
			
			// Try to find a portal near where the player should land
			Block dest = normal.getBlockAt(b.getX() * 8, b.getY(), b.getZ() * 8);
			NetherPortal portal = NetherPortal.findPortal(dest);
			if (portal == null) {
				portal = NetherPortal.createPortal(dest);
				main.log(event.getPlayer().getName() + " portals to normal world [NEW]");
			} else {
				main.log(event.getPlayer().getName() + " portals to normal world");
			}
			
			// Go!
			Location spawn = portal.getSpawn();
			event.getPlayer().teleportTo(spawn);
			event.setTo(spawn);
		}
	}
	
}
