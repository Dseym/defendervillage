package defenderVillage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class waves implements Runnable {
	
	main plugin;
	World world;
	static boolean isNight = true;

	public waves(main plugin, World world) {
		
		this.plugin = plugin;
		this.world = world;
		
	}
	
	

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		if(isNight) {
			
			world.setTime(5500);
			Bukkit.getScheduler().runTaskLater(plugin, this, 1000);
			for(Player p: Bukkit.getOnlinePlayers()) {
				
				p.sendMessage(main.tagPlugin + "����� �����������, �������� ����!");
				mobs.target = null;
				mobs.blood = false;
				
			}
			
			if(main.waves == main.maxWave) {
				
				for(Player p: Bukkit.getOnlinePlayers()) {
					
					p.sendMessage(main.tagPlugin + ChatColor.GREEN + "�� ��������!");
					p.sendTitle(ChatColor.GREEN + "������", "");
					
				}
				Bukkit.getPluginManager().disablePlugin(commands.plugin);
				Bukkit.getPluginManager().enablePlugin(commands.plugin);
				
			}
			isNight = false;
			commands.scoreboard.reload();
			
		} else {
			
			world.setTime(17400);
			Bukkit.getScheduler().runTaskLater(plugin, this, 1200);
			main.waves++;
			for(Player p: Bukkit.getOnlinePlayers()) {
				
				p.sendMessage(main.tagPlugin + "�����: " + main.waves);
				p.sendMessage(main.tagPlugin + "������� ������ �������� �� ����� ������!");
				
			}
			isNight = true;
			commands.scoreboard.reload();
			
		}
		
	}
	
}
