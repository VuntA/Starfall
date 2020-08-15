package me.starfall.biome;

import org.bukkit.Location;
import org.bukkit.block.Biome;

public class BiomeCheck {
    public static BiomeCheck plugin;

    public static boolean checkBiome(Location loc, Biome b) {
        return loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ()) == b;
    }
}
