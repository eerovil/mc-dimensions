package com.example.mcdimensions.block;

import com.example.mcdimensions.McDimensions;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block CUSTOM_PORTAL = Registry.register(
            Registries.BLOCK,
            Identifier.of(McDimensions.MOD_ID, "custom_portal"),
            new CustomPortalBlock()
    );

    public static void register() {
        McDimensions.LOGGER.info("Registered blocks");
    }
}



