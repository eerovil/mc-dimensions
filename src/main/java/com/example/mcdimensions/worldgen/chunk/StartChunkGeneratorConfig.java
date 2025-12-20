package com.example.mcdimensions.worldgen.chunk;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class StartChunkGeneratorConfig implements ChunkGeneratorSettings {
    public static final Codec<StartChunkGeneratorConfig> CODEC = Codec.unit(StartChunkGeneratorConfig::new);
}


