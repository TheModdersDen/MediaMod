package com.skcraft.playblock.projector;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import com.skcraft.playblock.GuiHandler;
import com.skcraft.playblock.PlayBlock;
import com.skcraft.playblock.PlayBlockCreativeTab;

/**
 * The projector block.
 */
public class BlockProjector extends Block {

    public static final String INTERNAL_NAME = "projector";

    public BlockProjector() {
        super(Material.IRON);
        setHardness(0.5F);
        setLightLevel(1.0F);
        setSoundType(SoundType.GLASS);
        setUnlocalizedName(INTERNAL_NAME);
        setCreativeTab(PlayBlockCreativeTab.tab);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack stack) {
        super.onBlockPlacedBy(world, new BlockPos(pos.getX(), pos.getY(), pos.getZ()), state, entityLiving, stack);

        int p = MathHelper.floor_double(Math.abs(((180 + entityLiving.rotationYaw) % 360) / 360) * 4 + 0.5);
        world.setBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), state, 2);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        // Be sure rather than crash the world
        TileEntity tileEntity = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
        if (tileEntity == null || !(tileEntity instanceof TileEntityProjector) || player.isSneaking()) {
            return false;
        }

        TileEntityProjector projector = (TileEntityProjector) tileEntity;

        // Show the GUI if it's the client
        player.openGui(PlayBlock.instance, GuiHandler.PROJECTOR, world, pos.getX(), pos.getY(), pos.getZ());

        if (!world.isRemote) {
            projector.getAccessList().allow(player);
        }

        return true;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityProjector();
    }

}
