package com.example.mcdimensions.test;

import com.example.mcdimensions.dimension.ModDimensions;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class AITest implements FabricGameTest {

    @GameTest(template = "empty", timeoutTicks = 100)
    public void testTeleportToStartDimension(TestContext context) {
        // Get the test player
        ServerPlayerEntity player = context.getFirstPlayer();
        
        // Get the overworld (current world)
        ServerWorld overworld = context.getWorld();
        
        // Get the start dimension
        ServerWorld startDimension = context.getWorld().getServer().getWorld(ModDimensions.START_DIMENSION);
        
        if (startDimension == null) {
            context.throwGameTestException("Start dimension not found");
            return;
        }
        
        // Verify player is initially in overworld
        if (player.getServerWorld() != overworld) {
            context.throwGameTestException("Player should start in overworld");
            return;
        }
        
        // Find a safe spawn position in the start dimension
        BlockPos spawnPos = new BlockPos(0, 64, 0);
        Vec3d targetPos = Vec3d.ofCenter(spawnPos);
        
        // Create teleport target - TeleportTarget requires ServerWorld, Vec3d, Vec3d, float, float, and PostDimensionTransition
        TeleportTarget teleportTarget = new TeleportTarget(
            startDimension,
            targetPos,
            Vec3d.ZERO,
            player.getYaw(),
            player.getPitch(),
            null // PostDimensionTransition - can be null for basic teleportation
        );
        
        // Teleport player to start dimension using FabricDimensions
        Entity teleportedEntity = FabricDimensions.teleport(player, startDimension, teleportTarget);
        
        // Wait a tick for teleportation to complete
        context.waitAndRun(1, () -> {
            // Verify player is now in the start dimension
            if (teleportedEntity.getWorld() != startDimension) {
                context.throwGameTestException("Player should be in start dimension after teleportation");
                return;
            }
            
            // Verify player position is approximately correct
            Vec3d playerPos = teleportedEntity.getPos();
            double distance = playerPos.distanceTo(targetPos);
            if (distance > 5.0) {
                context.throwGameTestException("Player position is too far from target: " + distance);
                return;
            }
            
            // Test passed
            context.complete();
        });
    }
}

