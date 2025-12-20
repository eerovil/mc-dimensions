package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.example.mcdimensions.McDimensions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.stream.Stream;

public class StartBiomeSource extends BiomeSource {
    public static final MapCodec<StartBiomeSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    StartBiomeSourceConfig.CODEC.fieldOf("config").forGetter(source -> StartBiomeSourceConfig.INSTANCE)
            ).apply(instance, config -> new StartBiomeSource(StartBiomeSourceConfig.INSTANCE))
    );
    
    private static final RegistryKey<Biome> START_BIOME = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of(McDimensions.MOD_ID, "start_biome")
    );

    public StartBiomeSource(StartBiomeSourceConfig config) {
        super();
    }

    @Override
    protected MapCodec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public Stream<RegistryKey<Biome>> getBiomes() {
        return Stream.of(START_BIOME);
    }

    @Override
    public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise, RegistryEntryLookup<Biome> biomeLookup) {
        return biomeLookup.getOrThrow(START_BIOME);
    }
}


