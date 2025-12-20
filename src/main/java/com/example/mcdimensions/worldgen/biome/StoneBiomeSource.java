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

public class StoneBiomeSource extends BiomeSource {
    public static final MapCodec<StoneBiomeSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.unit(StoneBiomeSourceConfig.INSTANCE)
            ).apply(instance, instance.stable(StoneBiomeSource::new))
    );
    
    private static final RegistryKey<Biome> STONE_BIOME = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of(McDimensions.MOD_ID, "stone_biome")
    );

    public StoneBiomeSource(StoneBiomeSourceConfig config) {
        super(List.of());
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC.codec();
    }

    @Override
    protected Stream<RegistryKey<Biome>> getBiomes() {
        return Stream.of(STONE_BIOME);
    }

    @Override
    public RegistryKey<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        return STONE_BIOME;
    }
}


