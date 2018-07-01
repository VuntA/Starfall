package me.starfall.cmds;

import me.starfall.Main;
import me.starfall.status.StatusChecker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StarFallCMD implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
        Player p = (Player) sender;
            if (p.hasPermission("starfall.Admin")) {
                if (args.length == 0) {
                    p.sendMessage(Main.prefix() + ChatColor.RED + "Usage: " + ChatColor.GRAY +
                            "\n\t/starfall start" +
                            "\n\t/starfall stop" +
                            "\n\t/starfall status" +
                            "\n\t/starfall give " + ChatColor.RED + "<ign>");
                }else if (args[0].equalsIgnoreCase("give")) {
                    try {
                        Player t = Bukkit.getPlayer(args[1]);
                        if (t != null) {
                            sender.sendMessage(Main.prefix() + ChatColor.RED + t.getName() + ChatColor.GRAY + " has been given a " + ChatColor.RED +
                                    "Starfall Pickaxe" + ChatColor.GRAY + ".");
                            giveItem(t);
                        }else {
                            sender.sendMessage(Main.prefix() + ChatColor.RED + args[1] + ChatColor.GRAY + " is not an online player" +
                                    ChatColor.RED + ".");
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException exception) {
                        p.sendMessage(ChatColor.RED + "Not enough arguments");
                    }
                } else if (args[0].equalsIgnoreCase("start")) {
                    Main.plugin.spawnStarfall();
                } else if (args[0].equalsIgnoreCase("stop")) {
                    Main.plugin.removeStarfall(p);
                } else if (args[0].equalsIgnoreCase("status")) {
                    StatusChecker.statusStarfall(p);
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "Insufficient Permissions");
            }
        }
        else {
            sender.sendMessage("Unable");
        }
        return true;
    }

    public void giveItem(Player t) {
        t.getPlayer().getInventory().addItem(Main.getPickAxe());
    }
}