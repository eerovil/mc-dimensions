package com.example.mcdimensions.event;

import com.example.mcdimensions.McDimensions;
import com.example.mcdimensions.dimension.ModDimensions;
import com.example.mcdimensions.portal.PortalFrameDetector;
import com.example.mcdimensions.worldgen.biome.ForestBiomeSource;
import com.example.mcdimensions.worldgen.biome.StartBiomeSource;
import com.example.mcdimensions.worldgen.biome.StoneBiomeSource;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.registry.RegistryKey;

public class ModEvents {
    private static final Set<BlockPos> blocksToCheck = new HashSet<>();
    private static int tickCounter = 0;

    public static void register() {
        // Resolve biome entries when worlds are loaded
        // Use a Set to track which worlds we've already processed
        final Set<net.minecraft.registry.RegistryKey<net.minecraft.world.World>> processedWorlds = new HashSet<>();
        
        ServerTickEvents.START_WORLD_TICK.register((ServerWorld world) -> {
            // Only do this once per world
            var worldKey = world.getRegistryKey();
            if (!processedWorlds.contains(worldKey)) {
                processedWorlds.add(worldKey);
                try {
                    var biomeSource = world.getChunkManager().getChunkGenerator().getBiomeSource();
                    var server = world.getServer();
                    if (server == null) {
                        McDimensions.LOGGER.warn("Server is null when trying to resolve biome entries for world {}", worldKey.getValue());
                        return;
                    }
                    // Try to get the registry through the world's registry manager
                    // Use the world's registry manager which should have access to the biome registry
                    var worldRegistryManager = world.getRegistryManager();
                    net.minecraft.registry.Registry<net.minecraft.world.biome.Biome> registry = null;
                    
                    // Try to get all available methods and find one that works
                    var methods = worldRegistryManager.getClass().getMethods();
                    for (var method : methods) {
                        if (method.getParameterCount() == 1 && 
                            method.getParameterTypes()[0] == net.minecraft.registry.RegistryKey.class &&
                            net.minecraft.registry.Registry.class.isAssignableFrom(method.getReturnType())) {
                            try {
                                var result = method.invoke(worldRegistryManager, net.minecraft.registry.RegistryKeys.BIOME);
                                if (result != null) {
                                    registry = (net.minecraft.registry.Registry<net.minecraft.world.biome.Biome>) result;
                                    McDimensions.LOGGER.info("Found working method: {} for world {}", method.getName(), worldKey.getValue());
                                    break;
                                }
                            } catch (Exception e) {
                                // Try next method
                            }
                        }
                    }
                    
                    if (registry == null) {
                        McDimensions.LOGGER.error("Could not find a method to get biome registry for world {}", worldKey.getValue());
                        return;
                    }
                    
                    McDimensions.LOGGER.info("Resolving biome entries for world {}", worldKey.getValue());
                    if (biomeSource instanceof StartBiomeSource startBiomeSource) {
                        startBiomeSource.resolveBiomeEntry(registry);
                    } else if (biomeSource instanceof ForestBiomeSource forestBiomeSource) {
                        forestBiomeSource.resolveBiomeEntry(registry);
                    } else if (biomeSource instanceof StoneBiomeSource stoneBiomeSource) {
                        stoneBiomeSource.resolveBiomeEntry(registry);
                    } else {
                        McDimensions.LOGGER.warn("Unknown biome source type: {}", biomeSource.getClass().getName());
                    }
                } catch (Exception e) {
                    McDimensions.LOGGER.error("Failed to resolve biome entries for world {}: {}", worldKey.getValue(), e.getMessage(), e);
                }
            }
        });
        
        // When player joins, teleport them to start dimension if they're in overworld
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            // Schedule teleport on next tick to ensure world is loaded
            server.execute(() -> {
                if (player != null && !player.isRemoved()) {
                    // Check if player is in overworld and move to start dimension
                    ServerWorld startWorld = server.getWorld(ModDimensions.START_DIMENSION);
                    if (startWorld != null) {
                        // Use refreshPositionAndAngles for now - full teleportation requires proper entity movement
                        player.refreshPositionAndAngles(0.5, 65.0, 0.5, player.getYaw(), player.getPitch());
                        McDimensions.LOGGER.info("Attempting to move player {} to start dimension", player.getName().getString());
                    }
                }
            });
        });
        
        // Listen for block break events - when a block is broken, check adjacent blocks
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient()) {
                // When a block is broken, check adjacent blocks for portal frames
                for (net.minecraft.util.math.Direction dir : net.minecraft.util.math.Direction.values()) {
                    schedulePortalCheck(world, pos.offset(dir));
                }
            }
        });
        
        // Periodically scan for portal frames around recently changed blocks
        // This will catch block placements and other changes
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

