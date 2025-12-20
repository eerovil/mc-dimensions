package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.Codec;

public class StoneBiomeSourceConfig {
    public static final StoneBiomeSourceConfig INSTANCE = new StoneBiomeSourceConfig();
    public static final Codec<StoneBiomeSourceConfig> CODEC = Codec.unit(INSTANCE);
}


