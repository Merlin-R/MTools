package me.reichwald.spigot.mtools.tools;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.reichwald.spigot.mtools.blocks.BlockTraceData;

public abstract class AbstractToolAdapter extends AbstractTool
{


  @Override
  public void onLeftClickAir( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data )
  {}

  @Override
  public void onLeftClickBlock( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data )
  {}

  @Override
  public void onLeftClickEntity( Player player, ItemStack item, EntityDamageByEntityEvent event )
  {}

  @Override
  public void onRightClickAir( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data )
  {}

  @Override
  public void onRightClickBlock( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data )
  {}

  @Override
  public void onRightClickEntity( Player player, ItemStack item, PlayerInteractEntityEvent event )
  {}

  @Override
  public void onBlockBreak( Player player, ItemStack item, BlockBreakEvent event, BlockTraceData data )
  {}

  @Override
  public void onItemBreak( Player player, ItemStack item, PlayerItemBreakEvent event )
  {}

  @Override
  public void onCraftEvent( Player player, ItemStack item, CraftItemEvent event )
  {}

}
