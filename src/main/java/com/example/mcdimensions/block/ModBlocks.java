package com.example.mcdimensions.block;

import com.example.mcdimensions.McDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static Block CUSTOM_PORTAL;

    public static void register() {
        // Create Identifier and RegistryKey first
        Identifier id = Identifier.of(McDimensions.MOD_ID, "custom_portal");
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        
        // Create settings and set the registry key
        AbstractBlock.Settings settings = AbstractBlock.Settings.create()
            .registryKey(blockKey)
            .noCollision()
            .strength(-1.0F);
        
        // Create the block with settings that have the registry key set
        CUSTOM_PORTAL = new CustomPortalBlock(settings);
        
        // Register the block
        CUSTOM_PORTAL = Registry.register(Registries.BLOCK, blockKey, CUSTOM_PORTAL);
        McDimensions.LOGGER.info("Registered custom portal block");
    }
}

