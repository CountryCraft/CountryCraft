package io.github.fifcostyle.CraftManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

import io.github.fifcostyle.CraftManager.events.ClearInvEvent;
import io.github.fifcostyle.CraftManager.events.GetDebugEvent;
import io.github.fifcostyle.CraftManager.events.GiveItemEvent;
import io.github.fifcostyle.CraftManager.events.OpenInvEvent;
import io.github.fifcostyle.CraftManager.events.SetDebugEvent;
import io.github.fifcostyle.CraftManager.events.SetFlyEvent;
import io.github.fifcostyle.CraftManager.events.SetFoodEvent;
import io.github.fifcostyle.CraftManager.events.SetGamemodeEvent;
import io.github.fifcostyle.CraftManager.events.SetHealthEvent;
import io.github.fifcostyle.CraftManager.events.StaffChatEvent;
import io.github.fifcostyle.CraftManager.events.SudoEvent;
import io.github.fifcostyle.CraftManager.events.TeleportEvent;
import io.github.fifcostyle.CraftManager.events.VanishEvent;

public class Executor implements Listener {
	CraftManager craft;
	public Executor(CraftManager craft) {
		this.craft = craft;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'PlayerJoinEvent'");
		e.setJoinMessage("");
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasMetadata("vanished")) e.getPlayer().hidePlayer(craft, player);
		}
		craft.getOnlinePlayers().add(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'PlayerQuitEvent'");
		e.setQuitMessage("");
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!e.getPlayer().canSee(player)) e.getPlayer().showPlayer(craft, player);
		}
		if (e.getPlayer().hasMetadata("vanished")) Bukkit.getPluginManager().callEvent(new VanishEvent(e.getPlayer(), false));
		craft.getOnlinePlayers().remove(e.getPlayer());
	}
	
	@EventHandler
	public void SetFly(SetFlyEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'SetFlyEvent'");
		e.getTarget().setAllowFlight(e.getState());
		e.getTarget().sendMessage(craft.getMessager().format("fly.player." + e.getState().toString()));
		//add broadcast
	}
	
	@EventHandler
	public void SetHealth(SetHealthEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'SetHealthEvent'");
		e.getTarget().setHealth(e.getHealth());
		e.getTarget().sendMessage(craft.getMessager().format("sethealth.player", e.getHealth()));
		//add broadcast
	}
	
	@EventHandler
	public void SetFood(SetFoodEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'SetFoodEvent'");
		e.getTarget().setFoodLevel(e.getFood());
		e.getTarget().sendMessage(craft.getMessager().format("setfood.player", e.getFood()));
		//add broadcast
	}
	
	@EventHandler
	public void SetDebugMode(SetDebugEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'SetDebugEvent'");
		craft.setDebugMode(e.getState());
		e.getSender().sendMessage(craft.getMessager().format("debugmode.set.player", e.getState()));
		//add broadcast
	}
	
	@EventHandler
	public void GetDebugMode(GetDebugEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'GetDebugEvent'");
		e.getSender().sendMessage(craft.getMessager().format("debugmode.get", craft.getDebugMode()));
	}
	
	@EventHandler
	public void onStaffChatMessageSent(StaffChatEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'StaffChatEvent'");
		Bukkit.broadcast(craft.getMessager().format("staffchat", e.getSender(), e.getMessage()), "countrycraft.staffchat.receive");
	}
	
	@EventHandler
	public void ClearInv(ClearInvEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'ClearInvEvent'");
		PlayerInventory inv = e.getTarget().getInventory();
		inv.clear();
		e.getTarget().sendMessage(craft.getMessager().format("clearinv.player"));
		//add broadcast
	}
	
	@EventHandler
	public void Teleport(TeleportEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'TeleportEvent'");
		switch (e.getMode()) {
		case "PlayerToPlayer":
			e.getToTp().teleport(e.getTpTo());
			e.getToTp().sendMessage(craft.getMessager().format("teleport.player.generic", e.getTpTo().getName()));
			//add broadcast
			break;
		case "PlayerToLoc":
			e.getToTp().teleport(e.getTpLoc());
			String loc = new String(e.getTpLoc().getWorld().getName() + " " + e.getTpLoc().getX() + " " + e.getTpLoc().getY() + " " + e.getTpLoc().getZ());
			e.getToTp().sendMessage(craft.getMessager().format("teleport.player.generic", loc));
			//add broadcast
			break;
		case "PlayerToLobby":
			e.getToTp().teleport(craft.getLobbyLocation());
			e.getToTp().sendMessage(craft.getMessager().format("teleport.player.lobby"));
			//add broadcast
			break;
		}
	}
	
	@EventHandler
	public void Sudo(SudoEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'SudoEvent'");
		e.getTarget().performCommand(e.getCommand());
		e.getSender().sendMessage(craft.getMessager().prefix(""));
	}
	
	@EventHandler
	public void SetGamemode(SetGamemodeEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'SetGamemodeEvent'");
		e.getTarget().setGameMode(e.getGM());
		e.getTarget().sendMessage(craft.getMessager().format("gamemode.set.player", e.getGM().toString()));
		//add broadcast
	}
	
	@EventHandler
	public void OpenInventory(OpenInvEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'OpenInvEvent'");
		e.getTarget().openInventory(e.getInv());
		e.getTarget().sendMessage(craft.getMessager().format("openinventory.player", e.getInv().getName()));
		//add broadcast
	}
	
	@EventHandler
	public void GiveItem(GiveItemEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'GiveItemEvent'");
		e.getTarget().getInventory().addItem(e.getItem());
		e.getTarget().sendMessage(craft.getMessager().format("giveitem.player", e.getItem().getType().toString(), e.getItem().getAmount()));
		//add broadcast
	}
	
	@EventHandler
	public void Vanish(VanishEvent e) {
		if (craft.getDebugMode()) craft.getLogger().info("Caught 'VanishEvent'");
		if (e.getState()) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!player.hasPermission("countrycraft.vanish.see")) {
					if (player.getName() != e.getSender().getName()) player.hidePlayer(craft, e.getTarget());
				}
			}
			craft.getOnlinePlayers().remove(e.getTarget());
			craft.getVanishedPlayers().add(e.getTarget());
		}
		else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!player.canSee(e.getTarget())) player.showPlayer(craft, e.getTarget());
			}
			craft.getVanishedPlayers().remove(e.getTarget());
			craft.getOnlinePlayers().add(e.getTarget());
		}
		e.getTarget().sendMessage(craft.getMessager().format("vanish.player", "TRUE"));
	}
	
	
}
