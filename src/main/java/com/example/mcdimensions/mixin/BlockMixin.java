package com.example.mcdimensions.mixin;

import com.example.mcdimensions.event.ModEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "onBlockAdded", at = @At("TAIL"))
    private void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (!world.isClient) {
            // Schedule portal check for this block and adjacent blocks
            ModEvents.schedulePortalCheck(world, pos);
            for (net.minecraft.util.math.Direction dir : net.minecraft.util.math.Direction.values()) {
                ModEvents.schedulePortalCheck(world, pos.offset(dir));
            }
        }
    }
}


