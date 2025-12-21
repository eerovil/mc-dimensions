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

public class StoneBiomeSource extends BiomeSource {
    public static final MapCodec<StoneBiomeSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    StoneBiomeSourceConfig.CODEC.fieldOf("config").forGetter(source -> StoneBiomeSourceConfig.INSTANCE)
            ).apply(instance, config -> new StoneBiomeSource(StoneBiomeSourceConfig.INSTANCE))
    );
    
    private static final RegistryKey<Biome> STONE_BIOME = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of(McDimensions.MOD_ID, "stone_biome")
    );
    
    private final RegistryEntry<Biome> biomeEntry;

    public StoneBiomeSource(StoneBiomeSourceConfig config) {
        super();
        this.biomeEntry = null;
    }
    
    public StoneBiomeSource(RegistryEntry<Biome> biomeEntry) {
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



