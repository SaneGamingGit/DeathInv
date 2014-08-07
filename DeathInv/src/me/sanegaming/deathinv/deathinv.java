package me.sanegaming.deathinv;

import java.util.List;
import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class deathinv extends JavaPlugin implements Listener {
	
	private HashMap<OfflinePlayer, deathinv> deathinv = new HashMap<OfflinePlayer, deathinv>();
	
	public static Economy econ = null;
    
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            Bukkit.getServer().getPluginManager().registerEvents(this,this);
            return;
	
        }
    }
 
            private boolean setupEconomy() {
                if (getServer().getPluginManager().getPlugin("Vault") == null) {
                    return false;
                }
                RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
                if (rsp == null) {
                    return false;
                }
                econ = rsp.getProvider();
                return econ != null;
            }
            
            
            
            @EventHandler //the place that gets their inventory stored and then removes the drops from the ground.
        	public void onPlayerDeath(PlayerDeathEvent e) {
            	List<String> dinv = getConfig().getStringList("deathinv");
            	
            	for (OfflinePlayer p : deathinv.keySet()){    		
            		dinv.add(p.getName() + ":" + deathinv.get(p).get);
            	}
            	getConfig().set("DeathInv", dinv);
            	saveConfig();
        	}     
    
            
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You cannot perform this command from the console!");
			return true;
		}
		Player p = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("deathinv")){
			EconomyResponse r = econ.withdrawPlayer(p.getName(), 500); //want to set the 500 to be set in a config file
			if (r.transactionSuccess()){
				
				
				return true;
			}
			else {
				p.sendMessage(ChatColor.RED + "You do not have enough money to perform this command.");
			}
		}
		
		
		
		
		
		
		
		return false;
	}
	
}

