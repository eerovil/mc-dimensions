package com.example.mcdimensions.worldgen.chunk;

import com.example.mcdimensions.McDimensions;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class ModChunkGenerators {
    public static final MapCodec<StartChunkGenerator> START_CHUNK_GENERATOR = 
            Registry.register(
                    Registries.CHUNK_GENERATOR,
                    Identifier.of(McDimensions.MOD_ID, "start_chunk_generator"),
                    StartChunkGenerator.CODEC
            );
    
    public static final MapCodec<ForestChunkGenerator> FOREST_CHUNK_GENERATOR = 
            Registry.register(
                    Registries.CHUNK_GENERATOR,
                    Identifier.of(McDimensions.MOD_ID, "forest_chunk_generator"),
                    ForestChunkGenerator.CODEC
            );
    
    public static final MapCodec<StoneChunkGenerator> STONE_CHUNK_GENERATOR = 
            Registry.register(
                    Registries.CHUNK_GENERATOR,
                    Identifier.of(McDimensions.MOD_ID, "stone_chunk_generator"),
                    StoneChunkGenerator.CODEC
            );

    public static void register() {
        McDimensions.LOGGER.info("Registered chunk generators");
    }
}

