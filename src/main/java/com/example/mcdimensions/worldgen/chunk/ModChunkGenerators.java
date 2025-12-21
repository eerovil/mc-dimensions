package com.example.mcdimensions.worldgen.chunk;

import com.example.mcdimensions.McDimensions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class ModChunkGenerators {
    public static void register() {
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of(McDimensions.MOD_ID, "start_chunk_generator"), StartChunkGenerator.CODEC);
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of(McDimensions.MOD_ID, "forest_chunk_generator"), ForestChunkGenerator.CODEC);
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of(McDimensions.MOD_ID, "stone_chunk_generator"), StoneChunkGenerator.CODEC);
        McDimensions.LOGGER.info("Registered chunk generators");
    }
}

