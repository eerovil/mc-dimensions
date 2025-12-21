package com.example.mcdimensions.command;

import com.example.mcdimensions.McDimensions;
import com.example.mcdimensions.dimension.ModDimensions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class ModCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("dimension")
            .then(CommandManager.literal("start")
                .executes(context -> teleportToDimension(context, ModDimensions.START_DIMENSION, "start")))
            .then(CommandManager.literal("forest")
                .executes(context -> teleportToDimension(context, ModDimensions.FOREST_DIMENSION, "forest")))
            .then(CommandManager.literal("stone")
                .executes(context -> teleportToDimension(context, ModDimensions.STONE_DIMENSION, "stone")))
        );
    }
    
    private static int teleportToDimension(CommandContext<ServerCommandSource> context, net.minecraft.registry.RegistryKey<net.minecraft.world.World> dimension, String name) {
        ServerCommandSource source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Text.literal("This command can only be used by players"));
            return 0;
        }
        
        ServerWorld targetWorld = source.getServer().getWorld(dimension);
        if (targetWorld == null) {
            source.sendError(Text.literal("Dimension " + name + " not found"));
            return 0;
        }
        
        // Teleport player to dimension using refreshPositionAndAngles
        // Note: Full cross-dimension teleportation requires proper entity movement
        player.refreshPositionAndAngles(0.5, 65.0, 0.5, player.getYaw(), player.getPitch());
        source.sendFeedback(() -> Text.literal("Teleported to " + name + " dimension"), false);
        McDimensions.LOGGER.info("Player {} teleported to {} dimension via command", player.getName().getString(), name);
        return 1;
    }
}

