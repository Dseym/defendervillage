package defenderVillage;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class event implements Listener {

	World world;
	Location respawnPlayer;
	
	public event(World world, Location respawnPlayer) {
		
		this.world = world;
		this.respawnPlayer = respawnPlayer;
		
	}
	
	

	@SuppressWarnings("deprecation")
	@EventHandler
	void isDeadMob(EntityDeathEvent e) {
		
		if(commands.villagers.contains(e.getEntity())) {
			
			commands.villagers.remove(mobs.target);
			
			if(commands.villagers.toArray().length < 1) {
				
				for(Player pls: Bukkit.getOnlinePlayers()) {
					
					pls.sendMessage(main.tagPlugin + ChatColor.DARK_RED + "Вы проиграли!");
					
				}
				Bukkit.getPluginManager().disablePlugin(commands.plugin);
				Bukkit.getPluginManager().enablePlugin(commands.plugin);
				
			}
			
			mobs.target = (LivingEntity)Bukkit.getOnlinePlayers().toArray()[(int)Math.floor(Math.random() * Bukkit.getOnlinePlayers().toArray().length)];
			mobs.blood = true;
			
			for(Player pls: Bukkit.getOnlinePlayers()) {
				
				pls.sendMessage(main.tagPlugin + "Один из жителей погиб.");
				
			}
			
			for(LivingEntity mob: mobs.mobs) {
				
				((Creature)mob).setTarget(mobs.target);
				try {
					
					mob.setHealth(mob.getHealth() + 2);
					
				} catch (Exception e2) {}
				
			}
			
		}
		
		if(mobs.mobs.contains(e.getEntity())) {
			
			mobs.mobs.remove(e.getEntity());
			
			LivingEntity mob = e.getEntity();
			
			switch(mob.getType()) {
			
				case ZOMBIE:
					try {
						
						mob.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 2*20, 1));
						
					} catch (Exception e2) {}
					break;
				
				case SKELETON:
					//nothing
					break;
					
				case CREEPER:
					try {
						
						world.createExplosion(mob.getLocation(), 4);
						
					} catch (Exception e2) {}
					break;
					
				case SPIDER:
					try {
						
						mob.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 2*20, 1));
						
					} catch (Exception e2) {}
					break;
					
				case ENDERMAN:
					world.spawnEntity(mob.getLocation(), EntityType.ENDERMITE);
					break;
					
				case HUSK:
					try {
						
						mob.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2*20, 1));
						mob.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2*20, 1));
						
					} catch (Exception e2) {}
					break;
					
				case RAVAGER:
					try {
						
						mob.getKiller().setVelocity(new Vector(0, 1, 0));
						mob.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 0));
						
					} catch (Exception e2) {}
					break;
					
				case SILVERFISH:
					//nothing
					break;
	
				case WITCH:
					try {
						
						mob.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2*20, 0));
						
					} catch (Exception e2) {}
					((LivingEntity)world.spawnEntity(mob.getLocation(), EntityType.WITCH)).setMaxHealth(2);
					break;
					
				case WITHER_SKELETON:
					try {
						
						mob.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 2*20, 0));
						mob.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1*20, 10));
						
					} catch (Exception e2) {}
					break;
					
					
				default:
					
					break;
			
			}
			
		}
		
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	void isPlayerDead(EntityDamageEvent e) {
		
		if(e.getEntityType() != EntityType.PLAYER)
			return;
		
		Player p = (Player)e.getEntity();
		
		if(p.getHealth() <= e.getDamage()) {
			
			e.getEntity().teleport(respawnPlayer);
			p.setFoodLevel(20);
			for(PotionEffect effect: p.getActivePotionEffects()) {
				
				p.removePotionEffect(effect.getType());
				
			}
			p.setHealth(p.getMaxHealth());
			e.setCancelled(true);
			
			if(mobs.blood) {
				
				commands.deadPlayers.add(p);
				
				for(Player pls: Bukkit.getOnlinePlayers()) {
					
					pls.sendMessage(main.tagPlugin + e.getEntity().getName() + " погиб.");
					
				}
				
				if(commands.deadPlayers.toArray().length >= Bukkit.getOnlinePlayers().toArray().length) {
					
					for(Player pls: Bukkit.getOnlinePlayers()) {
						
						pls.sendMessage(main.tagPlugin + ChatColor.DARK_RED  + "Вы проиграли!");
						p.sendTitle(ChatColor.DARK_RED + "ПОРАЖЕНИЕ", "");
						
					}
					Bukkit.getPluginManager().disablePlugin(commands.plugin);
					Bukkit.getPluginManager().enablePlugin(commands.plugin);
					
				}
				
				p.setGameMode(GameMode.SPECTATOR);
				mobs.blood = false;
				mobs.target = null;
				mobs.targetMob((LivingEntity)e.getEntity());
				
			}
			
		}
		
	}
	
	
	@EventHandler
	void isMobDamageMob(EntityTargetEvent e) {
		
		if(mobs.mobs.contains(e.getTarget())) {
			
			if(!mobs.blood) {
				
				mobs.target = null;
				mobs.targetMob((LivingEntity)e.getEntity());
				
			}
			
			if(e.getEntityType() == EntityType.BLAZE) {
				
				((LivingEntity)e.getTarget()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 0));
				((LivingEntity)e.getTarget()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10*20, 0));
				((LivingEntity)e.getTarget()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10*20, 0));
				
			}
			
			e.setCancelled(true);
			
		}
		
	}
	
}
