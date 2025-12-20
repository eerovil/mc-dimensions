package com.example.mcdimensions.dimension;

import com.example.mcdimensions.McDimensions;
import com.example.mcdimensions.worldgen.biome.ModBiomeSources;
import com.example.mcdimensions.worldgen.chunk.ModChunkGenerators;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

public class ModDimensions {
    public static final RegistryKey<DimensionType> START_DIMENSION_TYPE = RegistryKey.of(
            RegistryKeys.DIMENSION_TYPE,
            Identifier.of(McDimensions.MOD_ID, "start")
    );
    
    public static final RegistryKey<DimensionType> FOREST_DIMENSION_TYPE = RegistryKey.of(
            RegistryKeys.DIMENSION_TYPE,
            Identifier.of(McDimensions.MOD_ID, "forest")
    );
    
    public static final RegistryKey<DimensionType> STONE_DIMENSION_TYPE = RegistryKey.of(
            RegistryKeys.DIMENSION_TYPE,
            Identifier.of(McDimensions.MOD_ID, "stone")
    );

    public static final RegistryKey<net.minecraft.world.World> START_DIMENSION = RegistryKey.of(
            RegistryKeys.WORLD,
            Identifier.of(McDimensions.MOD_ID, "start")
    );
    
    public static final RegistryKey<net.minecraft.world.World> FOREST_DIMENSION = RegistryKey.of(
            RegistryKeys.WORLD,
            Identifier.of(McDimensions.MOD_ID, "forest")
    );
    
    public static final RegistryKey<net.minecraft.world.World> STONE_DIMENSION = RegistryKey.of(
            RegistryKeys.WORLD,
            Identifier.of(McDimensions.MOD_ID, "stone")
    );

    public static void register() {
        ModChunkGenerators.register();
        ModBiomeSources.register();
        McDimensions.LOGGER.info("Registered dimensions");
    }
}



