package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.example.mcdimensions.McDimensions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

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

    public StoneBiomeSource(StoneBiomeSourceConfig config) {
        super();
    }

    @Override
    protected MapCodec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    public Stream<RegistryKey<Biome>> getBiomes() {
        return Stream.of(STONE_BIOME);
    }

    @Override
    public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        // This will be resolved by the registry lookup during world generation
        return null; // Will be properly resolved by the biome source system
    }
}



