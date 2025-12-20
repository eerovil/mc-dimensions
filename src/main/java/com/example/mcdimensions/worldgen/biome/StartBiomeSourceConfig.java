package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StartBiomeSourceConfig {
    public static final StartBiomeSourceConfig INSTANCE = new StartBiomeSourceConfig();
    public static final Codec<StartBiomeSourceConfig> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.BOOL.optionalFieldOf("dummy", false).forGetter(config -> false)
        ).apply(instance, ignored -> INSTANCE)
    );
}



