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

public class StoneChunkGenerator extends ChunkGenerator {
    public static final MapCodec<StoneChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource)
            ).apply(instance, instance.stable(StoneChunkGenerator::new))
    );

    public StoneChunkGenerator(BiomeSource biomeSource) {
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

    @Override
    public int getWorldHeight() {
        return 384;
    }

    @Override
    public int getSeaLevel() {
        return 63;
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, net.minecraft.world.gen.noise.NoiseConfig noiseConfig, Chunk chunk) {
        // Surface is already set from populateNoise
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, net.minecraft.world.gen.noise.NoiseConfig noiseConfig) {
        return 64 + (int)(Math.sin(x * 0.1) * 5) + (int)(Math.cos(z * 0.1) * 5);
    }

    @Override
    public java.util.concurrent.CompletableFuture<Chunk> populateNoise(Blender blender, net.minecraft.world.gen.noise.NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        // Generate terrain: normal overworld-like with stone and basic ores
        // Ores will be generated via biome features
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int minY = chunk.getBottomY();
        int chunkX = chunk.getPos().getStartX();
        int chunkZ = chunk.getPos().getStartZ();
        
        // Generate terrain with stone underground
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX + x;
                int worldZ = chunkZ + z;
                int surfaceHeight = 64 + (int)(Math.sin(worldX * 0.1) * 5) + (int)(Math.cos(worldZ * 0.1) * 5);
                surfaceHeight = Math.max(minY, Math.min(320, surfaceHeight));
                
                for (int y = minY; y <= surfaceHeight; y++) {
                    mutable.set(x, y, z);
                    if (y == surfaceHeight) {
                        chunk.setBlockState(mutable, Blocks.GRASS_BLOCK.getDefaultState(), 0);
                    } else if (y == surfaceHeight - 1) {
                        chunk.setBlockState(mutable, Blocks.DIRT.getDefaultState(), 0);
                    } else if (y < surfaceHeight - 4) {
                        // Stone below surface
                        chunk.setBlockState(mutable, Blocks.STONE.getDefaultState(), 0);
                    } else {
                        chunk.setBlockState(mutable, Blocks.DIRT.getDefaultState(), 0);
                    }
                }
            }
        }
        
        return java.util.concurrent.CompletableFuture.completedFuture(chunk);
    }

    @Override
    public void carve(ChunkRegion region, long seed, net.minecraft.world.gen.noise.NoiseConfig noiseConfig, net.minecraft.world.biome.source.BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk) {
        // Disable caves and ravines
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        // No entities
    }

    @Override
    public void appendDebugHudText(java.util.List<String> text, net.minecraft.world.gen.noise.NoiseConfig noiseConfig, BlockPos pos) {
        // No debug info
    }
    
    @Override
    public net.minecraft.world.gen.chunk.VerticalBlockSample getColumnSample(int x, int z, HeightLimitView heightLimitView, net.minecraft.world.gen.noise.NoiseConfig noiseConfig) {
        return new net.minecraft.world.gen.chunk.VerticalBlockSample(heightLimitView.getBottomY(), new net.minecraft.block.BlockState[0]);
    }
}



