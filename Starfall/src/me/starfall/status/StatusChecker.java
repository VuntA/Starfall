package me.starfall.status;

import me.starfall.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class StatusChecker {
    public static void statusStarfall(Player p) {
        p.sendMessage(Main.prefix() + ChatColor.GRAY + "Starfall is currently set to " + ChatColor.RED + Main.plugin.getGameCheck());
    }
}
