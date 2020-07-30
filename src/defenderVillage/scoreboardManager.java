package defenderVillage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class scoreboardManager {
	
	Scoreboard sb;
	Objective obj;
	
	@SuppressWarnings("deprecation")
	void init() {
		
		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
		obj = sb.registerNewObjective("game", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("DefenderVillage");
		
	}
	
	void reload() {
		
		for(String str: sb.getEntries()) {
			
			sb.resetScores(str);
			
		}
		
		obj.getScore("�����: " + main.waves).setScore(7);
		obj.getScore("���������: " + mobs.difficult).setScore(6);
		obj.getScore(ChatColor.AQUA + "").setScore(5);
		obj.getScore("����� ��-��: " + (Bukkit.getOnlinePlayers().toArray().length-commands.deadPlayers.toArray().length)).setScore(4);
		obj.getScore("�������: " + commands.villagers.toArray().length).setScore(3);
		obj.getScore("��������: " + mobs.mobs.toArray().length).setScore(2);
		obj.getScore(ChatColor.BLACK + "").setScore(1);
		obj.getScore("������ - " + (mobs.blood ? "���" : "����")).setScore(0);
		
	}
	
	void givePlayer(Player p) {
		
		p.setScoreboard(sb);
		
	}
	
}
