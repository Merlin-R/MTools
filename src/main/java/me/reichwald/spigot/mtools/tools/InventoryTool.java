package me.reichwald.spigot.mtools.tools;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import me.reichwald.spigot.mtools.blocks.BlockTraceData;
import me.reichwald.spigot.mtools.util.ItemUtils;


public abstract class InventoryTool extends AbstractToolAdapter
{
  
  
  public static final String INVENTORY_START_FLAG = "§aStored Items:";
  public static final String INVENTORY_ITEM_FLAG = "§0§0§0§0§0§0§0§0§b";
  
  public abstract int getInventorySize();
  
  public abstract String getInventoryTitle();
  
  @Override
  public void onRightClickAir( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data )
  {
    if ( player.isSneaking() )
      this.openInventory( player, item );
  }
  
  @Override
  public void onRightClickBlock( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data )
  {
    if ( player.isSneaking() )
      this.openInventory( player, item );
  }
  
  @Override
  public void onRightClickEntity( Player player, ItemStack item, PlayerInteractEntityEvent event )
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
    
    if ( start == -1 )
    {
      lore.add( INVENTORY_START_FLAG );
      return;
    }
    
    lore.stream().filter( (s)->s.startsWith( INVENTORY_ITEM_FLAG ) )
      .map( (s) -> s.substring( INVENTORY_ITEM_FLAG.length(), s.lastIndexOf( INVENTORY_ITEM_FLAG ) ) )
      .map( ItemUtils :: deserializeLore )
      .forEach( inventory::addItem );
  }
  
  protected void saveInventory( Player player, Inventory inventory, ItemStack item )
  {
    ItemMeta meta = item.getItemMeta();
    List<String> lore = meta.getLore();
    
    List<String> toRemove = Lists.newArrayList();
    lore.stream().filter( (s)->s.startsWith( INVENTORY_ITEM_FLAG ) ).forEach( toRemove::add );
    lore.removeAll( toRemove );
    
    int start = lore.indexOf( INVENTORY_START_FLAG );
    
    if ( start == -1 )
    {
      lore.add( INVENTORY_START_FLAG );
      start = lore.indexOf( INVENTORY_START_FLAG );
    }
    
    for ( ItemStack stack : inventory.getContents() ) if ( null != stack )
    {
      if ( AbstractTool.byItemStack( stack ) != null )
      {
        player.getInventory().addItem( stack );
        continue;
      }
      
      ItemMeta stackMeta = stack.getItemMeta();
      String itemTitle = "§a" + stack.getAmount() + " §9x§a " + getItemName( stack, stackMeta );
      
      lore.add( ++start, INVENTORY_ITEM_FLAG + ItemUtils.serializeLore( stack ) + INVENTORY_ITEM_FLAG + itemTitle );
    }
    
    meta.setLore( lore );
    item.setItemMeta( meta );
  }
  
  private String getItemName( ItemStack stack, ItemMeta meta )
  {
    String itemTitle = meta.getDisplayName();
    if ( itemTitle != null ) return itemTitle;
    
    itemTitle = stack.getType().name().replaceAll( "_", " " );
    char[] chars = itemTitle.toLowerCase().toCharArray();
    boolean found = false;
    for (int i = 0; i < chars.length; i++) {
      if (!found && Character.isLetter(chars[i])) {
        chars[i] = Character.toUpperCase(chars[i]);
        found = true;
      } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
        found = false;
      }
    }
    return String.valueOf(chars);
  }
  
  public final void _inventoryClose( Player player, ItemStack stack, InventoryCloseEvent event )
  {
    saveInventory( player, event.getInventory(), stack );
  }
  
}
