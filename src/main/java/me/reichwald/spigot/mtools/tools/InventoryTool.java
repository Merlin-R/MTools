package me.reichwald.spigot.mtools.tools;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.reichwald.spigot.mtools.blocks.BlockTraceData;
import me.reichwald.spigot.mtools.util.ItemUtils;


public abstract class InventoryTool extends AbstractToolAdapter
{
  
  
  public static final String INVENTORY_START_FLAG = "§0§0§0§0§aStored Items:";
  public static final String INVENTORY_END_FLAG = "§0§0§0§0§b";
  
  public abstract int getInventorySize();
  
  public abstract String getInventoryTitle();
  
  @Override
  public void onRightClickAir( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data )
  {
    if ( player.isSneaking() )
      this.openInventory( player, item );
  }

  protected void openInventory( Player player, ItemStack item )
  {
    player.openInventory( this.getInventory( player, item ) );
  }
  
  private Inventory getInventory( Player player, ItemStack item )
  {
    Inventory inventory = Bukkit.createInventory( player, getInventorySize(), getInventoryTitle() );
    
    populateInventory( player, inventory, item );
    
    return inventory;
  }
  
  protected void populateInventory( Player player, Inventory inventory, ItemStack item )
  {
    ItemMeta meta = item.getItemMeta();
    List<String> lore = meta.getLore();
    
    int start = lore.indexOf( INVENTORY_START_FLAG );
    int end = lore.lastIndexOf( INVENTORY_END_FLAG );
    
    if ( start == -1 || end == -1 )
    {
      lore.add( INVENTORY_START_FLAG );
      lore.add( INVENTORY_END_FLAG );
      start = lore.indexOf( INVENTORY_START_FLAG );
      end = start + 1;
    }
    
    for ( int i = start + 1; i < end; ++i )
    {
      String itemString = lore.get( i );
      if ( !"".equals( itemString ) )
      {
        inventory.addItem( ItemUtils.deserializeLore( itemString.substring( 0, itemString.indexOf( INVENTORY_END_FLAG ) ) ) );
      }
    }
    
  }
  
  protected void saveInventory( Player player, Inventory inventory, ItemStack item )
  {
    ItemMeta meta = item.getItemMeta();
    List<String> lore = meta.getLore();
    
    int start = lore.indexOf( INVENTORY_START_FLAG );
    int end = lore.lastIndexOf( INVENTORY_END_FLAG ) - 1;
    
    if ( start == -1 || end == -1 )
    {
      lore.add( INVENTORY_START_FLAG );
      lore.add( INVENTORY_END_FLAG );
      start = lore.indexOf( INVENTORY_START_FLAG );
      end = start + 1;
    }
    
    while ( end > start )
      lore.remove( --end );
    
    for ( ItemStack stack : inventory.getContents() ) if ( null != stack )
    {
      ItemMeta stackMeta = stack.getItemMeta();
      String itemTitle = "§a" + stack.getAmount() + " §9x§a " + stackMeta.getDisplayName();
      
      lore.add( ++start, ItemUtils.serializeLore( stack ) + INVENTORY_END_FLAG + itemTitle );
    }
    
    item.setItemMeta( meta );
  }
  
  public final void _inventoryClose( Player player, ItemStack stack, InventoryCloseEvent event )
  {
    saveInventory( player, event.getInventory(), stack );
  }
  
}
