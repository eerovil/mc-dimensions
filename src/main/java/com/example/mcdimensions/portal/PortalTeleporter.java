package com.example.mcdimensions.portal;

import com.example.mcdimensions.McDimensions;
import com.example.mcdimensions.dimension.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

public class PortalTeleporter {
    public static void teleportEntity(Entity entity, World world, BlockPos portalPos) {
        if (!(world instanceof ServerWorld)) {
            return;
        }

        ServerWorld currentWorld = (ServerWorld) world;
        
        // Determine target dimension based on portal frame type
        RegistryKey<net.minecraft.world.World> targetDimension = 
                PortalFrameDetector.getTargetDimensionForPortal(currentWorld, portalPos);
        
        if (targetDimension == null) {
            return;
        }

        ServerWorld targetWorld = currentWorld.getServer().getWorld(targetDimension);
        if (targetWorld == null) {
            McDimensions.LOGGER.warn("Target dimension {} not found", targetDimension);
            return;
        }

        // Find safe spawn position in target dimension
        BlockPos targetPos = findSafeSpawnPosition(targetWorld, portalPos);
        
        // Teleport entity
        TeleportTarget teleportTarget = new TeleportTarget(
                targetWorld,
                Vec3d.ofCenter(targetPos),
                Vec3d.ZERO,
                entity.getYaw(),
                entity.getPitch(),
                net.minecraft.world.PositionFlag.DEFAULT,
                net.minecraft.world.PostDimensionTransition.UNKNOWN
        );
        
        entity.teleportTo(teleportTarget);
    }

    private static BlockPos findSafeSpawnPosition(ServerWorld world, BlockPos referencePos) {
        // Try to find a safe position near the reference
        BlockPos.Mutable pos = referencePos.mutableCopy();
        
        // Find ground level
        int groundY = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ());
        pos.setY(groundY + 1);
        
        // Ensure safe spawn (air block above solid ground)
        for (int attempts = 0; attempts < 10; attempts++) {
            BlockState state = world.getBlockState(pos);
            BlockState below = world.getBlockState(pos.down());
            
            if (state.isAir() && !below.isAir()) {
                return pos.toImmutable();
            }
            
            pos.move(0, 1, 0);
        }
        
        // Fallback: spawn at y=64
        return new BlockPos(pos.getX(), 64, pos.getZ());
    }
}

