package com.example.mcdimensions.worldgen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ForestChunkGeneratorConfig {
    public static final ForestChunkGeneratorConfig INSTANCE = new ForestChunkGeneratorConfig();
    public static final Codec<ForestChunkGeneratorConfig> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.BOOL.optionalFieldOf("dummy", false).forGetter(config -> false)
        ).apply(instance, ignored -> INSTANCE)
    );
}



