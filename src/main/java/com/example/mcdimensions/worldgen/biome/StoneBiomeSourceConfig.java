package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StoneBiomeSourceConfig {
    public static final StoneBiomeSourceConfig INSTANCE = new StoneBiomeSourceConfig();
    public static final Codec<StoneBiomeSourceConfig> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.BOOL.optionalFieldOf("dummy", false).forGetter(config -> false)
        ).apply(instance, ignored -> INSTANCE)
    );
}



