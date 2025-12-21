package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.example.mcdimensions.McDimensions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.Set;
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
    
    private final RegistryEntry<Biome> biomeEntry;

    public StartBiomeSource(StartBiomeSourceConfig config) {
        super();
        // Biome entry will be resolved during world generation
        this.biomeEntry = null;
    }
    
    public StartBiomeSource(RegistryEntry<Biome> biomeEntry) {
        super();
        this.biomeEntry = biomeEntry;
    }

    @Override
    protected MapCodec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public Stream<RegistryEntry<Biome>> biomeStream() {
        return biomeEntry != null ? Stream.of(biomeEntry) : Stream.empty();
    }

    @Override
    public Set<RegistryEntry<Biome>> getBiomes() {
        return biomeEntry != null ? Set.of(biomeEntry) : Set.of();
    }

    @Override
    public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        return biomeEntry;
    }
}



