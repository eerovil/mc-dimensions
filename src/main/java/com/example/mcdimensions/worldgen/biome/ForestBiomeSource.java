package com.example.mcdimensions.worldgen.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.example.mcdimensions.McDimensions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.List;
import java.util.stream.Stream;

public class ForestBiomeSource extends BiomeSource {
    public static final MapCodec<ForestBiomeSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.unit(ForestBiomeSourceConfig.INSTANCE)
            ).apply(instance, instance.stable(ForestBiomeSource::new))
    );
    
    private static final RegistryKey<Biome> FOREST_BIOME = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of(McDimensions.MOD_ID, "forest_biome")
    );

    public ForestBiomeSource(ForestBiomeSourceConfig config) {
        super(List.of());
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC.codec();
    }

    @Override
    protected Stream<RegistryKey<Biome>> getBiomes() {
        return Stream.of(FOREST_BIOME);
    }

    @Override
    public RegistryKey<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        return FOREST_BIOME;
    }
}


