package com.example.mcdimensions.worldgen.biome;

import com.example.mcdimensions.McDimensions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.BiomeSource;

public class ModBiomeSources {
    public static void register() {
        Registry.register(Registries.BIOME_SOURCE, Identifier.of(McDimensions.MOD_ID, "start_biome_source"), StartBiomeSource.CODEC);
        Registry.register(Registries.BIOME_SOURCE, Identifier.of(McDimensions.MOD_ID, "forest_biome_source"), ForestBiomeSource.CODEC);
        Registry.register(Registries.BIOME_SOURCE, Identifier.of(McDimensions.MOD_ID, "stone_biome_source"), StoneBiomeSource.CODEC);
        McDimensions.LOGGER.info("Registered biome sources");
    }
}


