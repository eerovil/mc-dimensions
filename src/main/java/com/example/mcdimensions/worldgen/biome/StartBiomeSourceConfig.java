package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.Codec;

public class StartBiomeSourceConfig {
    public static final StartBiomeSourceConfig INSTANCE = new StartBiomeSourceConfig();
    public static final Codec<StartBiomeSourceConfig> CODEC = Codec.unit(INSTANCE);
}


