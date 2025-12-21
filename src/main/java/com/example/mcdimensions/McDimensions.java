package com.example.mcdimensions;

import com.example.mcdimensions.block.ModBlocks;
import com.example.mcdimensions.dimension.ModDimensions;
import com.example.mcdimensions.event.ModEvents;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McDimensions implements ModInitializer {
    public static final String MOD_ID = "mc-dimensions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing MC Dimensions mod");
        
        // Register blocks first
        ModBlocks.register();
        
        // Register dimensions and world generation
        ModDimensions.register();
        
        // Register events
        ModEvents.register();
        
        LOGGER.info("MC Dimensions mod initialized");
    }
}


