package com.example.mcdimensions.worldgen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StartChunkGeneratorConfig {
    public static final StartChunkGeneratorConfig INSTANCE = new StartChunkGeneratorConfig();
    public static final Codec<StartChunkGeneratorConfig> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.BOOL.optionalFieldOf("dummy", false).forGetter(config -> false)
        ).apply(instance, ignored -> INSTANCE)
    );
}


