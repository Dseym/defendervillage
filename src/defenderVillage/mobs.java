package defenderVillage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class mobs implements Runnable {

	static ArrayList<LivingEntity> mobs = new ArrayList<LivingEntity>();
	main plugin;
	static LivingEntity target = null;
	static boolean blood = false;
	static int difficult = 1;
	boolean isSuperMob = false;
	
	File fileSpawn;
	FileConfiguration configSpawn;
	List<String> spawnsMobs;
	
	
	public mobs(main plugin) {
		
		this.plugin = plugin;
		fileSpawn = new File(plugin.getDataFolder() + File.separator + "spawn.yml");
		configSpawn = (FileConfiguration)YamlConfiguration.loadConfiguration(fileSpawn);
		spawnsMobs = (List<String>)configSpawn.getStringList("mob");
		
	}


	@Override
	public void run() {
		
		difficult = ((int)Math.ceil(Bukkit.getOnlinePlayers().toArray().length / 2)) + ((int)Math.floor(main.waves / 2));
		commands.scoreboard.reload();
		
		String locString = spawnsMobs.get((int)Math.floor(Math.random() * spawnsMobs.toArray().length));
		World world = Bukkit.getWorld(locString.split(":")[0]);
		
		controlMobs(world);
		if(waves.isNight) {
			for(int i = 0; i < 2 * difficult; i++) {
				
				locString = spawnsMobs.get((int)Math.floor(Math.random() * spawnsMobs.toArray().length));
				world = Bukkit.getWorld(locString.split(":")[0]);
				String[] coord = locString.split(":")[1].split(",");
				Location loc = new Location(world, Float.parseFloat(coord[0]), Float.parseFloat(coord[1]), Float.parseFloat(coord[2]));
				
				spawnMobs(loc);
				
			}
			
		}

	}

	
	static void targetMob(LivingEntity mob) {
		
		if(target == null) {
			
			target = commands.villagers.get((int)Math.floor(Math.random() * commands.villagers.toArray().length));
			Location l = target.getLocation();
			for(Player pls: Bukkit.getOnlinePlayers()) {
				
				pls.sendMessage(main.tagPlugin + "Мобы заагрились куда то на " + (int)l.getX() + " " + (int)l.getY() + " " + (int)l.getZ());
				
			}
			
		} else if(target.isDead()) {
			
			commands.villagers.remove(target);
			target = commands.villagers.get((int)Math.floor(Math.random() * commands.villagers.toArray().length));
			Location l = target.getLocation();
			for(Player pls: Bukkit.getOnlinePlayers()) {
				
				pls.sendMessage(main.tagPlugin + "Мобы заагрились куда то на " + (int)l.getX() + " " + (int)l.getY() + " " + (int)l.getZ());
				
			}
			
		}
		
		((Creature)mob).setTarget(target);
		
	}

	@SuppressWarnings("deprecation")
	void controlMobs(World world) {
		
		for(LivingEntity mob: mobs) {
			
			mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3, 10));
				
			if(difficult > 2) {
				
				mob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 14*20, 0));
				
			}
			
			if(difficult > 4) {
				
				mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 14*20, 1));
				
			}
			
			if(difficult > 6) {
				
				mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10*20, 0));
				
			}
			
			if(difficult > 7) {
				
				mob.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 3*20, 0));
				
			}
			
			if(difficult > 8) {
				
				mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 14*20, 0));
				blood = true;
				
			}
			
			
			
			targetMob(mob);
			
			if(mob.getTargetBlock(null, 1).getType() != Material.AIR) {
				
				new Location(world, mob.getTargetBlock(null, 1).getX(), mob.getTargetBlock(null, 1).getY(), mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
				if(mob.getType() == EntityType.ENDERMAN) {
					
					new Location(world, mob.getTargetBlock(null, 1).getX(), mob.getTargetBlock(null, 1).getY()-1, mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					
				}
				
			}
			
			switch(mob.getType()) {
			
				case ZOMBIE:
					try {
						
						mob.setHealth(mob.getHealth() + 2);
						
					} catch (Exception e) {}
					mob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20, 0));
					Location l = mob.getLocation();
					Block bl = new Location(l.getWorld(), l.getX()+1, l.getY(), l.getZ()).getBlock();
					Block bl2 = new Location(l.getWorld(), l.getX()-1, l.getY(), l.getZ()).getBlock();
					Block bl3 = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()+1).getBlock();
					Block bl4 = new Location(l.getWorld(), l.getX()+1, l.getY(), l.getZ()-1).getBlock();
					if(bl.getType() == Material.AIR)
						bl.setType(Material.DIRT);
					if(bl2.getType() == Material.AIR)
						bl2.setType(Material.DIRT);
					if(bl3.getType() == Material.AIR)
						bl3.setType(Material.DIRT);
					if(bl4.getType() == Material.AIR)
						bl4.setType(Material.DIRT);
					break;
				
				case SKELETON:
					mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20, 1));
					mob.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5*20, 1));
					break;
					
				case CREEPER:
					mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2*20, 10));
					world.createExplosion(mob.getLocation(), 2);
					break;
					
				case SPIDER:
					try {
						
						mob.getLocation().getBlock().setType(Material.COBWEB);
						
					} catch (Exception e) {}
					mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2*20, 3));
					break;
					
				case ENDERMAN:
					mob.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5*20, 1));
					break;
					
				case HUSK:
					for(Player p: Bukkit.getOnlinePlayers()) {
						
						p.setFoodLevel(p.getFoodLevel() - 1);
						
					}
					break;
					
				case RAVAGER:
					new Location(world, mob.getTargetBlock(null, 1).getX(), mob.getTargetBlock(null, 1).getY()+1, mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					new Location(world, mob.getTargetBlock(null, 1).getX(), mob.getTargetBlock(null, 1).getY()-1, mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					new Location(world, mob.getTargetBlock(null, 1).getX()+1, mob.getTargetBlock(null, 1).getY(), mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					new Location(world, mob.getTargetBlock(null, 1).getX()-1, mob.getTargetBlock(null, 1).getY(), mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					new Location(world, mob.getTargetBlock(null, 1).getX(), mob.getTargetBlock(null, 1).getY(), mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					new Location(world, mob.getTargetBlock(null, 1).getX()+1, mob.getTargetBlock(null, 1).getY()+1, mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					new Location(world, mob.getTargetBlock(null, 1).getX()+1, mob.getTargetBlock(null, 1).getY()-1, mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					new Location(world, mob.getTargetBlock(null, 1).getX()-1, mob.getTargetBlock(null, 1).getY()-1, mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					new Location(world, mob.getTargetBlock(null, 1).getX()-1, mob.getTargetBlock(null, 1).getY()+1, mob.getTargetBlock(null, 1).getZ()).getBlock().setType(Material.AIR);
					break;
					
				case SILVERFISH:
					LivingEntity entity = (LivingEntity)world.spawnEntity(mob.getLocation(), mob.getType());
					entity.setMaxHealth(1);
					break;
	
				case WITCH:
					for(LivingEntity ent: mobs) {
						
						ent.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5*20, 0));
						ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 0));
						
					}
					break;
					
				case WITHER_SKELETON:
					for(Player p: Bukkit.getOnlinePlayers()) {
						
						p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 2*20, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1*20, 0));
						
					}
					break;
					
				case BLAZE:
					Location loc = mob.getLocation();
					Block block = mob.getTargetBlock(null, 16);
					while(loc.getBlock().getType() == Material.AIR) {
						
						loc = new Location(loc.getWorld(), loc.getX(), loc.getY()-1, loc.getZ());
						
					}
					new Location(loc.getWorld(), loc.getX(), loc.getY()+1, loc.getZ()).getBlock().setType(Material.FIRE);
					new Location(loc.getWorld(), block.getX(), block.getY()+1, block.getZ()).getBlock().setType(Material.FIRE);
					
					mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3*20, 2));
					mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20, 2));
					break;
					
					
				default:
					
					break;
			
			}
			
		}
		
	}


	void spawnMobs(Location loc) {
		
		EntityType mob;
		
		switch((int)Math.floor(Math.random() * 10)) {
		
			case 0:
				mob = EntityType.ZOMBIE;
				break;
			
			case 1:
				mob = EntityType.SKELETON;
				break;
				
			case 2:
				mob = EntityType.CREEPER;
				break;
				
			case 3:
				mob = EntityType.SPIDER;
				break;
				
			case 4:
				mob = EntityType.ENDERMAN;
				break;
				
			case 5:
				mob = EntityType.HUSK;
				break;
				
			case 6:
				mob = EntityType.RAVAGER;
				break;
				
			case 7:
				mob = EntityType.SILVERFISH;
				break;

			case 8:
				mob = EntityType.WITCH;
				break;
				
			case 9:
				mob = EntityType.WITHER_SKELETON;
				break;

			
			default:
				mob = EntityType.ZOMBIE;
				break;
			
		}
		
		if(main.maxWave-1 == main.waves && !isSuperMob) {
			
			Creature super_mob = (Creature)loc.getWorld().spawnEntity(loc, EntityType.BLAZE);
			mobs.add(super_mob);
			for(Player pls: Bukkit.getOnlinePlayers()) {
				
				pls.sendMessage(main.tagPlugin + "Появился супер-моб!");
				
			}
			
		}
		
		Creature entity = (Creature)loc.getWorld().spawnEntity(loc, mob);
		
		mobs.add(entity);
		
	}

}
