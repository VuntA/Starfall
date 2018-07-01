package me.starfall;

import java.util.Random;
import me.starfall.biome.BiomeCheck;
import me.starfall.cmds.StarFallCMD;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
    private static ItemStack starFallPickAxe = new ItemStack(Material.DIAMOND_PICKAXE);
    private ItemMeta itemMeta = starFallPickAxe.getItemMeta();
    private static Location mainLoc;
    private static boolean gameCheck = false;
    private Biome b = Biome.OCEAN;
    private Biome c = Biome.DEEP_OCEAN;
    private Random r = new Random();

    public static Main plugin;

    public void onEnable() {
    	plugin = this;
        getLogger().info("Successfully enabled Starfall!");
        getCommand("starfall").setExecutor(new StarFallCMD());
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        starFallPickAxe();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        new BukkitRunnable() {
            public void run() {
                Main.this.spawnStarfall();
            }
        }.runTaskTimer(this, 100L, 432000L);
    }

    public void onDisable() {
        getLogger().info("Successfully disabled Starfall!");
    }
    
    private void starFallPickAxe() {
        this.itemMeta.setDisplayName(ChatColor.GRAY + "[" + ChatColor.RED + "Starfall Pickaxe" + ChatColor.GRAY + "]");
        starFallPickAxe.setItemMeta(this.itemMeta);
        starFallPickAxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 20);
        starFallPickAxe.addUnsafeEnchantment(Enchantment.DURABILITY, 15);
    }

    public static ItemStack getPickAxe() {
        return starFallPickAxe;
    }

    private void getter(Location loc) {
        mainLoc = loc;
    }

    public boolean getGameCheck() {
        return gameCheck;
    }

    public static String prefix() {
        return ChatColor.GRAY + "[" + ChatColor.RED + "StarFall" + ChatColor.GRAY + "] ";
    }

	@SuppressWarnings({ "deprecation" })
	public void spawnStarfall() {
		if (!gameCheck) {
			int x = r.nextInt(4999) + 1;
			int z = r.nextInt(4999) + 1;
			int y = (Bukkit.getWorlds().get(0)).getHighestBlockAt(x,z).getY();
			Location loc = new Location(Bukkit.getWorlds().get(0), x, y, z);
			if (BiomeCheck.isOceanBiome(loc, b) == true || BiomeCheck.isDeepOceanBiome(loc, c) == true) {
				while (BiomeCheck.isOceanBiome(loc, b) == true || BiomeCheck.isDeepOceanBiome(loc, c) == true) {
					x = r.nextInt(4999) + 1;
					z = r.nextInt(4999) + 1;
					y = (Bukkit.getWorlds().get(0)).getHighestBlockAt(x,z).getY();
					loc = new Location(Bukkit.getWorlds().get(0), x, y, z);
				}
			}
			loc.getBlock().setType(Material.OBSIDIAN);
			getter(loc);
			Bukkit.broadcastMessage(Main.prefix() + ChatColor.GRAY + "The star has been dropped at: "
					+ ChatColor.YELLOW + "x: " + mainLoc.getX() + " y: " + mainLoc.getY() + " z: " + mainLoc.getZ());
			gameCheck = true;
		}else {
			for (Player  p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("starfall.Admin")) {
                    p.sendMessage(ChatColor.RED + "There is already a starfall game running!");
                }
            }
		}
	}
	
    public void removeStarfall(Player p) {
        if (gameCheck) {
			if(mainLoc.getBlock().getType() == Material.OBSIDIAN){
				Bukkit.broadcastMessage(Main.prefix() + ChatColor.GRAY + "The star has now been ended at: "
						+ ChatColor.YELLOW + "x: " + mainLoc.getX() + " y: " + mainLoc.getY() + " z: " + mainLoc.getZ());
				mainLoc.getBlock().setType(Material.AIR);
				gameCheck = false;
			}else {
				p.sendMessage(ChatColor.RED + "Something went wrong (Perhaps the Star may be missing) Forcing game close");
				gameCheck = false;
			}
		}else {
			p.sendMessage(Main.prefix() + ChatColor.GRAY + "There is no current starfall running!");
		}
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if ((gameCheck) &&
                (e.getBlock().getLocation().equals(mainLoc)) &&
                (p.getPlayer().getInventory().getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) == 20) &&
                (p.getPlayer().getInventory().getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY) == 15) &&
                (e.getBlock().getType().equals(Material.OBSIDIAN))) {
            Bukkit.broadcastMessage(prefix() + ChatColor.GRAY + "The starfall has been obtained by: " + ChatColor.RED + p.getName());
            p.sendMessage(ChatColor.RED + "You have won the StarFall!");
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
            gameCheck = false;
            for (String str : plugin.getConfig().getStringList("rewards")) {
                str.toUpperCase();
                p.getPlayer().getInventory().addItem(new ItemStack(Material.valueOf(str), plugin.getConfig().getInt("amount")));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();
            if (e.getClickedBlock().getType().equals(Material.OBSIDIAN)) {
                if (e.getClickedBlock().getLocation().equals(mainLoc)) {
                    if (gameCheck) {
                        if (p.getPlayer().getInventory().getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) == 20
                                && p.getPlayer().getInventory().getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY) == 15) {
                        }else {
                            e.setCancelled(true);
                            p.sendMessage(ChatColor.RED + "You must use a Starfall Pickaxe!");
                        }
                    }
                }
            }
        }catch (Exception ignored) {
        }
    }
}