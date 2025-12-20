package com.example.mcdimensions.worldgen.chunk;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class StoneChunkGeneratorConfig implements ChunkGeneratorSettings {
    public static final Codec<StoneChunkGeneratorConfig> CODEC = Codec.unit(StoneChunkGeneratorConfig::new);
}


