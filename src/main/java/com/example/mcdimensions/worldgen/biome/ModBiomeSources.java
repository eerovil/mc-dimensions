package com.example.mcdimensions.worldgen.biome;

import com.example.mcdimensions.McDimensions;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.BiomeSource;

public class ModBiomeSources {
    public static final MapCodec<StartBiomeSource> START_BIOME_SOURCE = 
            Registry.register(
                    Registries.BIOME_SOURCE,
                    Identifier.of(McDimensions.MOD_ID, "start_biome_source"),
                    StartBiomeSource.CODEC
            );
    
    public static final MapCodec<ForestBiomeSource> FOREST_BIOME_SOURCE = 
            Registry.register(
                    Registries.BIOME_SOURCE,
                    Identifier.of(McDimensions.MOD_ID, "forest_biome_source"),
                    ForestBiomeSource.CODEC
            );
    
    public static final MapCodec<StoneBiomeSource> STONE_BIOME_SOURCE = 
            Registry.register(
                    Registries.BIOME_SOURCE,
                    Identifier.of(McDimensions.MOD_ID, "stone_biome_source"),
                    StoneBiomeSource.CODEC
            );

    public static void register() {
        McDimensions.LOGGER.info("Registered biome sources");
    }
}

