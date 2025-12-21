package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.example.mcdimensions.McDimensions;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
    
    private RegistryEntry<Biome> biomeEntry;

    public StoneBiomeSource(StoneBiomeSourceConfig config) {
        super();
        this.biomeEntry = null; // Will be resolved lazily
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
        // For single-biome dimensions, always return the same biome
        if (biomeEntry == null) {
            throw new IllegalStateException("Biome entry for " + STONE_BIOME.getValue() + " is null. Make sure the biome is registered.");
        }
        return biomeEntry;
    }
    
    // Method to resolve biome entry from registry - should be called when world is created
    public void resolveBiomeEntry(net.minecraft.registry.Registry<Biome> registry) {
        if (biomeEntry == null) {
            try {
                var entry = registry.getOrThrow(STONE_BIOME);
                biomeEntry = (RegistryEntry<Biome>) entry;
            } catch (Exception e) {
                McDimensions.LOGGER.error("Failed to resolve biome entry for {}", STONE_BIOME.getValue(), e);
            }
        }
    }
}



