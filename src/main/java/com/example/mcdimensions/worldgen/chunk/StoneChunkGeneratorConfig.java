package com.example.mcdimensions.worldgen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StoneChunkGeneratorConfig {
    public static final StoneChunkGeneratorConfig INSTANCE = new StoneChunkGeneratorConfig();
    public static final Codec<StoneChunkGeneratorConfig> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.BOOL.optionalFieldOf("dummy", false).forGetter(config -> false)
        ).apply(instance, ignored -> INSTANCE)
    );
}


