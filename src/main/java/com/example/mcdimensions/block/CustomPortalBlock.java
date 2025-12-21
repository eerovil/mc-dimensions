package com.example.mcdimensions.block;

import com.example.mcdimensions.portal.PortalFrameDetector;
import com.example.mcdimensions.portal.PortalTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CustomPortalBlock extends Block {
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public CustomPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && !entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals(false)) {
            PortalTeleporter.teleportEntity(entity, world, pos);
        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient()) {
            // Check if portal frame is still valid
            if (!PortalFrameDetector.isValidPortalFrame(world, pos)) {
                world.removeBlock(pos, false);
            }
        }
    }
}



