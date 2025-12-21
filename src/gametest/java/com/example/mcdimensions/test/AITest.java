package com.example.mcdimensions.test;

import java.lang.reflect.Method;

import com.example.mcdimensions.dimension.ModDimensions;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;

/**
 * GameTest that tests teleporting to the start dimension using the execute command.
 * This test runs: /execute in mc-dimensions:start as @s run tp @s ~ ~ ~
 */
public class AITest implements CustomTestMethodInvoker {

    @GameTest
    public void testTeleportToStartDimension(TestContext context) {
        // Get the overworld (current world)
        ServerWorld overworld = context.getWorld();
        
        // Get the start dimension
        ServerWorld startDimension = overworld.getServer().getWorld(ModDimensions.START_DIMENSION);
        
        if (startDimension == null) {
            context.throwGameTestException("Start dimension not found. Make sure the dimension is properly registered.");
            return;
        }
        
        // Execute the command: /execute in mc-dimensions:start as @s run tp @s ~ ~ ~
        String command = "execute in mc-dimensions:start as @s run tp @s ~ ~ ~";
        
        try {
            // Get the command dispatcher
            com.mojang.brigadier.CommandDispatcher<ServerCommandSource> dispatcher = 
                overworld.getServer().getCommandManager().getDispatcher();
            
            // Create a command source from the server
            ServerCommandSource commandSource = overworld.getServer().getCommandSource();
            
            // Parse the command
            com.mojang.brigadier.ParseResults<ServerCommandSource> parseResults = 
                dispatcher.parse(command, commandSource);
            
            // Check for parse errors
            if (parseResults.getReader().canRead()) {
                context.throwGameTestException("Command parse error: " + parseResults.getReader().getRemaining());
                return;
            }
            
            // Execute the command
            int result = dispatcher.execute(parseResults);
            
            if (result == 0) {
                context.throwGameTestException("Command execution failed or returned 0");
                return;
            }
            
            // Command executed successfully
            // Note: The command will teleport the executing entity (@s) to the start dimension
            // The test framework will handle verification if the command fails
            context.complete();
        } catch (Exception e) {
            context.throwGameTestException("Failed to execute command: " + e.getMessage());
        }
    }

    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        // Invoke the test method directly
        // The test framework will provide the necessary context
        method.invoke(this, context);
    }
}
