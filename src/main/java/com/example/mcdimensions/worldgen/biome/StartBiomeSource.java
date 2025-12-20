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

public class StartBiomeSource extends BiomeSource {
    public static final MapCodec<StartBiomeSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.unit(StartBiomeSourceConfig.INSTANCE)
            ).apply(instance, instance.stable(StartBiomeSource::new))
    );
    
    private static final RegistryKey<Biome> START_BIOME = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of(McDimensions.MOD_ID, "start_biome")
    );

    public StartBiomeSource(StartBiomeSourceConfig config) {
        super(List.of());
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC.codec();
    }

    @Override
    protected Stream<RegistryKey<Biome>> getBiomes() {
        return Stream.of(START_BIOME);
    }

    @Override
    public RegistryKey<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        return START_BIOME;
    }
}


