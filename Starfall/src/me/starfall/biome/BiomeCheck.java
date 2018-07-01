package me.starfall.biome;

import org.bukkit.Location;
import org.bukkit.block.Biome;

public class BiomeCheck {
    public static BiomeCheck plugin;

    public static boolean isOceanBiome(Location loc, Biome b) {
        return loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ()) == b;
    }

    public static boolean isDeepOceanBiome(Location loc, Biome c) {
        return loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ()) == c;
    }
}
