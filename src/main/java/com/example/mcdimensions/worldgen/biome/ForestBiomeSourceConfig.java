package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.Codec;

public class ForestBiomeSourceConfig {
    public static final ForestBiomeSourceConfig INSTANCE = new ForestBiomeSourceConfig();
    public static final Codec<ForestBiomeSourceConfig> CODEC = Codec.unit(INSTANCE);
}


