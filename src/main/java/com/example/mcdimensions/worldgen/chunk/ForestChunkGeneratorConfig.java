package com.example.mcdimensions.worldgen.chunk;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ForestChunkGeneratorConfig implements ChunkGeneratorSettings {
    public static final Codec<ForestChunkGeneratorConfig> CODEC = Codec.unit(ForestChunkGeneratorConfig::new);
}


