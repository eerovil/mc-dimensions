package com.example.mcdimensions.portal;

import com.example.mcdimensions.McDimensions;
import com.example.mcdimensions.block.ModBlocks;
import com.example.mcdimensions.dimension.ModDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.HashSet;
import java.util.Set;

public class PortalFrameDetector {
    private static final int MIN_PORTAL_SIZE = 3;
    private static final int MAX_PORTAL_SIZE = 21;

    public static void checkAndCreatePortal(World world, BlockPos pos, BlockState placedBlock) {
        if (world.isClient()) {
            return;
        }

        // Determine frame block type and target dimension
        FrameType frameType = getFrameType(placedBlock);
        if (frameType == null) {
            return;
        }

        // Check for valid portal frame
        PortalFrame frame = findPortalFrame(world, pos, frameType);
        if (frame != null) {
            createPortal(world, frame, frameType.targetDimension);
        }
    }

    private static FrameType getFrameType(BlockState block) {
        Block blockType = block.getBlock();
        
        // Dirt frame -> forest dimension
        if (blockType == Blocks.DIRT || blockType == Blocks.GRASS_BLOCK) {
            return new FrameType(ModDimensions.FOREST_DIMENSION, blockType);
        }
        
        // Wood frame (logs or planks) -> stone dimension
        if (block.isIn(BlockTags.LOGS) || block.isIn(BlockTags.PLANKS)) {
            return new FrameType(ModDimensions.STONE_DIMENSION, blockType);
        }
        
        return null;
    }

    private static PortalFrame findPortalFrame(World world, BlockPos pos, FrameType frameType) {
        // Try horizontal frames first (X-Z plane)
        for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            PortalFrame frame = findFrameInPlane(world, pos, frameType, axis);
            if (frame != null) {
                return frame;
            }
        }
        
        return null;
    }

    private static PortalFrame findFrameInPlane(World world, BlockPos pos, FrameType frameType, Direction.Axis axis) {
        // Determine horizontal and vertical directions
        Direction horizontal = axis == Direction.Axis.X ? Direction.NORTH : Direction.EAST;
        Direction vertical = Direction.UP;

        // Find the bottom-left corner of potential frame
        BlockPos.Mutable corner = pos.mutableCopy();
        
        // Move to bottom
        while (isFrameBlock(world, corner.down(), frameType)) {
            corner.move(Direction.DOWN);
        }
        
        // Move to left (negative horizontal)
        while (isFrameBlock(world, corner.offset(horizontal.getOpposite()), frameType)) {
            corner.move(horizontal.getOpposite());
        }

        // Check if this is a valid frame starting point
        int width = measureFrame(world, corner, horizontal, frameType);
        int height = measureFrame(world, corner, vertical, frameType);
        
        if (width < MIN_PORTAL_SIZE || height < MIN_PORTAL_SIZE || 
            width > MAX_PORTAL_SIZE || height > MAX_PORTAL_SIZE) {
            return null;
        }

        // Verify all frame blocks are correct (2D rectangle)
        if (verifyFrame2D(world, corner, horizontal, vertical, width, height, frameType)) {
            // Calculate interior bounds (inside the frame, excluding frame blocks)
            BlockPos interiorStart = corner.offset(horizontal).offset(vertical);
            BlockPos interiorEnd = corner.offset(horizontal, width - 2)
                                          .offset(vertical, height - 2);
            
            // Use horizontal for both right and forward since it's a 2D portal
            return new PortalFrame(interiorStart, interiorEnd, horizontal, vertical, horizontal);
        }

        return null;
    }
    
    private static boolean verifyFrame2D(World world, BlockPos corner, Direction horizontal, 
                                         Direction vertical, int width, int height, FrameType frameType) {
        BlockPos.Mutable pos = corner.mutableCopy();
        
        // Check bottom edge
        for (int i = 0; i < width; i++) {
            if (!isFrameBlock(world, pos, frameType)) {
                return false;
            }
            pos.move(horizontal);
        }
        
        // Check top edge
        pos = corner.offset(vertical, height - 1).mutableCopy();
        for (int i = 0; i < width; i++) {
            if (!isFrameBlock(world, pos, frameType)) {
                return false;
            }
            pos.move(horizontal);
        }
        
        // Check left edge
        pos = corner.mutableCopy();
        for (int i = 0; i < height; i++) {
            if (!isFrameBlock(world, pos, frameType)) {
                return false;
            }
            pos.move(vertical);
        }
        
        // Check right edge
        pos = corner.offset(horizontal, width - 1).mutableCopy();
        for (int i = 0; i < height; i++) {
            if (!isFrameBlock(world, pos, frameType)) {
                return false;
            }
            pos.move(vertical);
        }
        
        // Verify interior is empty (at least 1 block inside)
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                pos = corner.offset(horizontal, x).offset(vertical, y).mutableCopy();
                if (!world.getBlockState(pos).isAir()) {
                    return false;
                }
            }
        }
        
        return true;
    }

    private static int measureFrame(World world, BlockPos start, Direction direction, FrameType frameType) {
        int count = 0;
        BlockPos.Mutable pos = start.mutableCopy();
        
        while (isFrameBlock(world, pos, frameType)) {
            count++;
            pos.move(direction);
        }
        
        return count;
    }


    private static boolean isFrameBlock(World world, BlockPos pos, FrameType frameType) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        
        // For dirt frames, accept dirt or grass
        if (frameType.frameBlock == Blocks.DIRT || frameType.frameBlock == Blocks.GRASS_BLOCK) {
            return block == Blocks.DIRT || block == Blocks.GRASS_BLOCK;
        }
        
        // For wood frames, accept any log or plank
        if (frameType.frameBlock != null) {
            BlockState frameState = frameType.frameBlock.getDefaultState();
            if (frameState.isIn(BlockTags.LOGS) || frameState.isIn(BlockTags.PLANKS)) {
                return state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS);
            }
        }
        
        return block == frameType.frameBlock;
    }

    private static void createPortal(World world, PortalFrame frame, RegistryKey<net.minecraft.world.World> targetDimension) {
        Direction right = frame.right;
        Direction up = frame.up;
        
        BlockPos start = frame.interiorStart;
        BlockPos end = frame.interiorEnd;
        
        // Calculate dimensions
        int width = Math.abs(getAxisValue(end, right.getAxis()) - getAxisValue(start, right.getAxis())) + 1;
        int height = Math.abs(getAxisValue(end, up.getAxis()) - getAxisValue(start, up.getAxis())) + 1;
        
        // Fill portal area
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BlockPos currentPos = start.offset(right, x).offset(up, y);
                if (world.getBlockState(currentPos).isAir()) {
                    world.setBlockState(currentPos, ModBlocks.CUSTOM_PORTAL.getDefaultState());
                }
            }
        }
    }
    
    private static int getAxisValue(BlockPos pos, Direction.Axis axis) {
        return switch (axis) {
            case X -> pos.getX();
            case Y -> pos.getY();
            case Z -> pos.getZ();
        };
    }
    
    public static RegistryKey<net.minecraft.world.World> getTargetDimensionForPortal(World world, BlockPos portalPos) {
        // Check adjacent blocks to determine frame type
        for (Direction dir : Direction.values()) {
            BlockPos framePos = portalPos.offset(dir);
            BlockState state = world.getBlockState(framePos);
            FrameType frameType = getFrameType(state);
            if (frameType != null) {
                return frameType.targetDimension;
            }
        }
        return null;
    }

    public static boolean isValidPortalFrame(World world, BlockPos portalPos) {
        // Check if any adjacent block is a valid frame block
        for (Direction dir : Direction.values()) {
            BlockPos framePos = portalPos.offset(dir);
            BlockState state = world.getBlockState(framePos);
            
            if (state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS_BLOCK ||
                state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS)) {
                return true;
            }
        }
        
        return false;
    }

    private static class FrameType {
        final RegistryKey<net.minecraft.world.World> targetDimension;
        final Block frameBlock;

        FrameType(RegistryKey<net.minecraft.world.World> targetDimension, Block frameBlock) {
            this.targetDimension = targetDimension;
            this.frameBlock = frameBlock;
        }
    }

    private static class PortalFrame {
        final BlockPos interiorStart;
        final BlockPos interiorEnd;
        final Direction right;
        final Direction up;
        final Direction forward;

        PortalFrame(BlockPos interiorStart, BlockPos interiorEnd, Direction right, Direction up, Direction forward) {
            this.interiorStart = interiorStart;
            this.interiorEnd = interiorEnd;
            this.right = right;
            this.up = up;
            this.forward = forward;
        }
    }
}

