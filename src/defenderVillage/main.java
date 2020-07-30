package defenderVillage;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;



public class main extends JavaPlugin {

	static String tagPlugin = ChatColor.RESET + "[" + ChatColor.BLUE + "DefenderVillage" + ChatColor.RESET + "] ";
	static int waves = 0;
	static int maxWave = 5;
	
	public void onEnable() {
		
		File spawn = new File(this.getDataFolder() + File.separator + "spawn.yml");
		
		if(!spawn.exists()) {
			
			try {
				
				spawn.createNewFile();
				
			} catch (IOException e) {}
			
		}
		
		this.getCommand("dv").setExecutor((CommandExecutor)new commands(this));
		this.getLogger().info("Started!");
		
	}
	
}
