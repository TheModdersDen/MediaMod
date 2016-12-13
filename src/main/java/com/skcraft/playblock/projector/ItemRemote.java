package com.skcraft.playblock.projector;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import com.skcraft.playblock.PlayBlock;
import com.skcraft.playblock.PlayBlockCreativeTab;
import com.skcraft.playblock.queue.ExposedQueue;

public class ItemRemote extends Item {

    public static final String INTERNAL_NAME = "remote";

    public ItemRemote() {
        setUnlocalizedName(ItemRemote.INTERNAL_NAME);
        setCreativeTab(PlayBlockCreativeTab.tab);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack item, World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            ExposedQueue queuable = getLinked(world, item);
            if (queuable == null) {
                player.addChatMessage(new TextComponentString("Not linked."));
            } else {
                PlayBlock.getClientRuntime().showRemoteGui(player, queuable);
            }
        }
        return new ActionResult(EnumActionResult.SUCCESS, item);
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack item, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
        if (tileEntity == null || !(tileEntity instanceof ExposedQueue)) {
        	return EnumActionResult.FAIL;
        }

        ExposedQueue queuable = (ExposedQueue) tileEntity;

        if (!item.hasTagCompound()) {
            item.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = item.getTagCompound();
        item.getTagCompound().setInteger("dim", world.provider.getDimension());
        item.getTagCompound().setInteger("x", pos.getX());
        item.getTagCompound().setInteger("y", pos.getY());
        item.getTagCompound().setInteger("z", pos.getZ());

        player.addChatMessage(new TextComponentString("Remote linked!"));

        return EnumActionResult.SUCCESS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, EntityPlayer player, List items, boolean showAdvanced) {
        super.addInformation(item, player, items, showAdvanced);

        NBTTagCompound tag = item.getTagCompound();

        if (tag != null && tag.hasKey("x")) {
            int x = item.getTagCompound().getInteger("x");
            int y = item.getTagCompound().getInteger("y");
            int z = item.getTagCompound().getInteger("z");

            items.add("Linked to" + " " + x + ", " + y + ", " + z);
        } else {
            items.add("Right click a projector to link.");
        }
    }

    /**
     * Get the {@link ExposedQueue} from an instance of an item.
     * 
     * @param world
     *            the current world
     * @param item
     *            the item
     * @return the linked object, otherwise null
     */
    public static ExposedQueue getLinked(World world, ItemStack item) {
        if (!item.hasTagCompound()) {
            return null;
        }

        NBTTagCompound tag = item.getTagCompound();
        int dim = item.getTagCompound().getInteger("dim");
        int x = item.getTagCompound().getInteger("x");
        int y = item.getTagCompound().getInteger("y");
        int z = item.getTagCompound().getInteger("z");

        if (world.provider.getDimension() != dim) {
            return null;
        }

        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity == null || !(tileEntity instanceof ExposedQueue)) {
            return null;
        }

        return (ExposedQueue) tileEntity;
    }

}
