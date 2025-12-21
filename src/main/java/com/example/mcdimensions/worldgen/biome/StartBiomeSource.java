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

public class StartBiomeSource extends BiomeSource {
    private static final RegistryKey<Biome> START_BIOME = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of(McDimensions.MOD_ID, "start_biome")
    );
    
    public static final MapCodec<StartBiomeSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    StartBiomeSourceConfig.CODEC.fieldOf("config").forGetter(source -> StartBiomeSourceConfig.INSTANCE)
            ).apply(instance, config -> new StartBiomeSource())
    );
    
    private RegistryEntry<Biome> biomeEntry;

    public StartBiomeSource() {
        super();
        this.biomeEntry = null; // Will be resolved lazily
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
        // For single-biome dimensions, always return the same biome
        // If biome entry is null, it means it wasn't resolved yet
        // The event listener should resolve it, but if it hasn't, we'll throw an error
        if (biomeEntry == null) {
            // Log a warning to help debug
            McDimensions.LOGGER.warn("Biome entry for {} is null when getBiome() is called. The event listener should have resolved it.", START_BIOME.getValue());
            throw new IllegalStateException("Biome entry for " + START_BIOME.getValue() + " is null. The biome source needs to be initialized with a valid biome entry when the world is created.");
        }
        return biomeEntry;
    }
    
    // Method to resolve biome entry from registry - should be called when world is created
    public void resolveBiomeEntry(net.minecraft.registry.Registry<Biome> registry) {
        if (biomeEntry == null) {
            try {
                // getOrThrow returns a RegistryEntry.Reference, cast it to RegistryEntry
                var entry = registry.getOrThrow(START_BIOME);
                biomeEntry = (RegistryEntry<Biome>) entry;
                McDimensions.LOGGER.info("Resolved biome entry for {}", START_BIOME.getValue());
            } catch (Exception e) {
                McDimensions.LOGGER.error("Failed to resolve biome entry for {}", START_BIOME.getValue(), e);
            }
        }
    }
}



