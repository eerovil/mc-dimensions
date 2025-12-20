package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ForestBiomeSourceConfig {
    public static final ForestBiomeSourceConfig INSTANCE = new ForestBiomeSourceConfig();
    public static final Codec<ForestBiomeSourceConfig> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.BOOL.optionalFieldOf("dummy", false).forGetter(config -> false)
        ).apply(instance, ignored -> INSTANCE)
    );
}


