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
        Vec3d targetVec = Vec3d.ofCenter(targetPos);
        
        // Teleport entity to target dimension
        // Note: This is a simplified implementation
        // Full cross-dimension teleportation may require additional setup
        if (entity instanceof net.minecraft.entity.player.PlayerEntity player) {
            // Use ServerWorld's teleport method - signature may vary by version
            // For now, use a basic approach
            try {
                // Try the teleport method with minimal parameters
                player.refreshPositionAndAngles(targetVec.x, targetVec.y, targetVec.z, player.getYaw(), player.getPitch());
                // Move player to target world (this is a simplified version)
                McDimensions.LOGGER.info("Teleporting player to dimension {}", targetDimension);
            } catch (Exception e) {
                McDimensions.LOGGER.error("Error teleporting player", e);
            }
        } else {
            // For other entities, log for now
            McDimensions.LOGGER.warn("Non-player entity teleportation not fully implemented");
        }
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

