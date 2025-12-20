package com.example.mcdimensions.worldgen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ForestChunkGenerator extends ChunkGenerator {
    public static final MapCodec<ForestChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource)
            ).apply(instance, instance.stable(ForestChunkGenerator::new))
    );

    public ForestChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public int getMinimumY() {
        return -64;
    }

    public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {
        // Surface is already dirt/grass from generateTerrain
    }

    public void generateTerrain(ChunkRegion region, StructureAccessor structures, Chunk chunk) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int minY = region.getBottomY();
        int maxY = 320; // Max build height
        
        // Generate terrain with dirt and grass
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int surfaceHeight = 64 + (int)(Math.sin(x * 0.1) * 3) + (int)(Math.cos(z * 0.1) * 3);
                surfaceHeight = Math.max(minY, Math.min(maxY, surfaceHeight));
                
                for (int y = minY; y <= surfaceHeight; y++) {
                    mutable.set(x, y, z);
                    if (y == surfaceHeight) {
                        chunk.setBlockState(mutable, Blocks.GRASS_BLOCK.getDefaultState(), 0);
                    } else {
                        chunk.setBlockState(mutable, Blocks.DIRT.getDefaultState(), 0);
                    }
                }
            }
        }
    }

    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, Object noiseConfig) {
        return 64 + (int)(Math.sin(x * 0.1) * 3) + (int)(Math.cos(z * 0.1) * 3);
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        // No entities
    }

    @Override
    public void appendDebugHudText(java.util.List<String> text, Object noiseConfig, BlockPos pos) {
        // No debug info
    }

    @Override
    public Object getColumnSample(int x, int z, HeightLimitView heightLimitView, Object noiseConfig) {
        return null; // Placeholder - will need proper implementation
    }
}



