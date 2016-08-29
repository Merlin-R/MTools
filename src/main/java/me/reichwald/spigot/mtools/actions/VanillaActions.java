package me.reichwald.spigot.mtools.actions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_10_R1.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.IBlockData;


public enum VanillaActions
{
  BREAK_BLOCK()
  {
    @Override
    public void execute( Player player, ItemStack item, Block block )
    {
      CraftPlayer p = (CraftPlayer) player;
      Location l = block.getLocation();
      p.getHandle().playerInteractManager.breakBlock( new BlockPosition( l.getBlockX(), l.getBlockY(), l.getBlockZ() ) );
    }

    @Override
    public boolean canExecute( Player player, ItemStack item, Block block )
    {
      CraftWorld cw = ((CraftWorld) block.getWorld());
      Location l = block.getLocation();
      
      IBlockData data = cw.getHandle().getType( new BlockPosition( l.getBlockX(), l.getBlockY(), l.getBlockZ() ) );
      
      return CraftItemStack.asNMSCopy( item ).getItem().canDestroySpecialBlock( data );
    }
  },
  
  ;
  VanillaActions(){}
  public abstract void execute( Player player, ItemStack item, Block block );
  public abstract boolean canExecute( Player player, ItemStack item, Block block );
  
}
