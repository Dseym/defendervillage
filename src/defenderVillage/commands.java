package defenderVillage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;



public class commands implements CommandExecutor {
	
	String tagPlugin = main.tagPlugin;
	static main plugin;
	static ArrayList<LivingEntity> villagers = new ArrayList<LivingEntity>();
	static ArrayList<Player> deadPlayers = new ArrayList<Player>();
	static scoreboardManager scoreboard;
	static int maxWave = 5;
	
	public commands(main main) {
		
		plugin = main;
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args[0].equalsIgnoreCase("start")) {
			
			start(sender);
			
		} else if(args[0].equalsIgnoreCase("addSpawnMob")) {
			
			addSpawnMob(sender);
			
		} else if(args[0].equalsIgnoreCase("addSpawnVillager")) {
			
			addSpawnVillager(sender);
			
		} else if(args[0].equalsIgnoreCase("setSpawn")) {
			
			setSpawn(sender);
			
		}
		
		return true;
		
	}
	
	
	void addSpawnVillager(CommandSender sender) {
		
		File fileSpawn = new File(plugin.getDataFolder() + File.separator + "spawn.yml");
		FileConfiguration configSpawn = (FileConfiguration)YamlConfiguration.loadConfiguration(fileSpawn);
		List<String> spawnsVillagers = (List<String>)configSpawn.getStringList("villager");
		Location loc = ((Player)sender).getLocation();
		spawnsVillagers.add(loc.getWorld().getName() + ":" + loc.getX() + "," + loc.getY() + "," + loc.getZ());
		
        configSpawn.set("villager", spawnsVillagers);
        try {
        	
			configSpawn.save(fileSpawn);
			
		} catch (IOException e) {}
		
	}

	void addSpawnMob(CommandSender sender) {
		
		File fileSpawn = new File(plugin.getDataFolder() + File.separator + "spawn.yml");
		FileConfiguration configSpawn = (FileConfiguration)YamlConfiguration.loadConfiguration(fileSpawn);
        List<String> spawnsMobs = (List<String>)configSpawn.getStringList("mob");
        Location loc = ((Player)sender).getLocation();
        spawnsMobs.add(loc.getWorld().getName() + ":" + loc.getX() + "," + loc.getY() + "," + loc.getZ());
		
        configSpawn.set("mob", spawnsMobs);
        try {
        	
			configSpawn.save(fileSpawn);
			
		} catch (IOException e) {}
        
	}
	
	void setSpawn(CommandSender sender) {
		
		File fileSpawn = new File(plugin.getDataFolder() + File.separator + "spawn.yml");
		FileConfiguration configSpawn = (FileConfiguration)YamlConfiguration.loadConfiguration(fileSpawn);
        String spawnPlayer = configSpawn.getString("spawnPlayer");
        Location loc = ((Player)sender).getLocation();
        spawnPlayer = loc.getWorld().getName() + ":" + loc.getX() + "," + loc.getY() + "," + loc.getZ();
		
        configSpawn.set("spawnPlayer", spawnPlayer);
        try {
        	
			configSpawn.save(fileSpawn);
			
		} catch (IOException e) {}
        
	}


	@SuppressWarnings("deprecation")
	void start(CommandSender sender) {
	
		sender.sendMessage(tagPlugin + "Игра началась!");
		
		File fileSpawn = new File(plugin.getDataFolder() + File.separator + "spawn.yml");
		FileConfiguration configSpawn = (FileConfiguration)YamlConfiguration.loadConfiguration(fileSpawn);
        List<String> spawnsVillagers = (List<String>)configSpawn.getStringList("villager");
        String spawnPlayer = configSpawn.getString("spawnPlayer");
        World world = null;
        for(int i = 0; i < spawnsVillagers.toArray().length; i++) {
        	
	        String locString = spawnsVillagers.get(i);
			world = Bukkit.getWorld(locString.split(":")[0]);
			String[] coord = locString.split(":")[1].split(",");
			Location loc = new Location(world, Float.parseFloat(coord[0]), Float.parseFloat(coord[1]), Float.parseFloat(coord[2]));
			
			villagers.add((LivingEntity)world.spawnEntity(loc, EntityType.VILLAGER));
			
        }
        
		World worldSpawnPlayer = Bukkit.getWorld(spawnPlayer.split(":")[0]);
		String[] coord = spawnPlayer.split(":")[1].split(",");
		Location locSpawnPlayer = new Location(worldSpawnPlayer, Float.parseFloat(coord[0]), Float.parseFloat(coord[1]), Float.parseFloat(coord[2]));
		
		scoreboard = new scoreboardManager();
		
		scoreboard.init();
		
		Bukkit.getScheduler().runTaskTimer(plugin, new mobs(plugin), 20, 300);
		Bukkit.getPluginManager().registerEvents(new event(world, locSpawnPlayer), plugin);
		new waves(plugin, world).run();
		for(Player p: Bukkit.getOnlinePlayers()) {
			
			p.teleport(locSpawnPlayer);
			p.setGameMode(GameMode.SURVIVAL);
			p.setHealth(p.getMaxHealth());
			p.setFoodLevel(20);
			scoreboard.givePlayer(p);
			
		}
		
		scoreboard.reload();
		
	}
	
}
