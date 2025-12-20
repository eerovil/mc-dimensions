package com.example.mcdimensions.event;

import com.example.mcdimensions.portal.PortalFrameDetector;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class ModEvents {
    private static final Set<BlockPos> blocksToCheck = new HashSet<>();
    private static int tickCounter = 0;

    public static void register() {
        // Check for portal frames on server tick
        ServerTickEvents.END_WORLD_TICK.register((ServerWorld world) -> {
            tickCounter++;
            
            // Check every 5 ticks to reduce load
            if (tickCounter % 5 != 0) {
                return;
            }
            
            // Check blocks that were recently added to the check list
            Set<BlockPos> toRemove = new HashSet<>();
            for (BlockPos pos : blocksToCheck) {
                if (world.isChunkLoaded(pos)) {
                    BlockState state = world.getBlockState(pos);
                    PortalFrameDetector.checkAndCreatePortal(world, pos, state);
                    toRemove.add(pos);
                }
            }
            blocksToCheck.removeAll(toRemove);
        });
    }
    
    public static void schedulePortalCheck(World world, BlockPos pos) {
        if (!world.isClient()) {
            blocksToCheck.add(pos.toImmutable());
        }
    }
}

