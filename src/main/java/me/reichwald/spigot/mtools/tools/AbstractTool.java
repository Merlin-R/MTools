package me.reichwald.spigot.mtools.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.reichwald.spigot.mtools.MTools;
import me.reichwald.spigot.mtools.blocks.BlockTraceData;

public abstract class AbstractTool
{
  public static final Map<String, AbstractTool> LOOKUP = Maps.newHashMap();
  
  public static final void registerTool( AbstractTool tool )
  {
    LOOKUP.put( tool.generateIdentifierString(), tool );
    Recipe recipe = tool.getCraftingRecipe();
    if ( recipe != null )
      MTools.SERVER.addRecipe( recipe );
  }
  
  public final void register()
  {
    registerTool( this );
  }
  
  public static final AbstractTool byItemStack( ItemStack stack )
  {
    if ( stack == null ) return null;
    ItemMeta meta = stack.getItemMeta();
    if ( meta == null ) return null;
    List<String> lore = meta.getLore();
    if ( lore == null || lore.size() < 2 )
      return null;
    return LOOKUP.get( lore.get( 1 ) );
  }
  
  
  
  
  
  
  
  public abstract String getDisplayName();
  public abstract int getId();
  public abstract int getMaximumDurability();
  public abstract Material getBaseMaterial();
  public abstract Recipe getCraftingRecipe();
  public abstract List<String> getLore();
  
  
  
  public final ItemStack create()
  {
    ItemStack item = new ItemStack( getBaseMaterial() );
    ItemMeta meta = item.getItemMeta();
    LinkedList<String> lore = Lists.newLinkedList( getLore() );
    
    meta.setDisplayName( getDisplayName() );
    lore.addFirst( generateIdentifierString() );
    lore.addFirst( generateDurabilityString( getMaximumDurability() ) );
    lore.addLast( "§6§l[MTools]" );
    
    meta.setLore( lore );
    item.setItemMeta( meta );
    return item;
  }
  
  private static final char[] FORMAT_CODES = "0123456789abcdeklmnor".toCharArray();
  private static final String FORMAT_START = "§0§0§0§r";
  private static final String FORMAT_DURABILITY = "§0§0§1§r";
  
  private String generateIdentifierString()
  {
    int id = getId();
    int formatLength = FORMAT_CODES.length;
    StringBuilder sb = new StringBuilder();
    while ( id > formatLength )
    {
      int part = id % formatLength;
      id /= formatLength;
      sb.append( '§' ).append( FORMAT_CODES[ part ] );
    }
    sb.append( '§' ).append( id );
    return FORMAT_START + sb.toString();
  }
  
  private String generateDurabilityString( int current )
  {
    return FORMAT_DURABILITY + "§9( §3" + current + "§3§9 / §3" + getMaximumDurability() + "§9 )";
  }
  
  private int parseDurabilityString( String durabilityString )
  {
    return Integer.parseInt( durabilityString.split( "§3" )[ 1 ] );
  }
  
  public abstract void onLeftClickAir( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data );
  public abstract void onLeftClickBlock( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data );
  public abstract void onLeftClickEntity( Player player, ItemStack item, EntityDamageByEntityEvent event );

  public abstract void onRightClickAir( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data );
  public abstract void onRightClickBlock( Player player, ItemStack item, PlayerInteractEvent event, BlockTraceData data );
  public abstract void onRightClickEntity( Player player, ItemStack item, PlayerInteractEntityEvent event );
  
  public abstract void onBlockBreak( Player player, ItemStack item, BlockBreakEvent event, BlockTraceData data );
  
  public final void digest( Player player, ItemStack item, PlayerItemDamageEvent event )
  {
    damage( event.getItem(), 1 );
  }
  public abstract void onItemBreak( Player player, ItemStack item, PlayerItemBreakEvent event );
  public abstract void onCraftEvent( Player player, ItemStack item, CraftItemEvent event );
  
  public void damage( ItemStack stack, int amount )
  {
    if ( stack == null ) return;
    
    ItemMeta meta = stack.getItemMeta();
    List<String> lore = meta.getLore();
    
    int unbreaking = stack.getEnchantmentLevel( Enchantment.DURABILITY );
    
    int durab = parseDurabilityString( lore.get( 0 ) );
    int maxDurab = getMaximumDurability();
    int toolMaxDurab = stack.getType().getMaxDurability();
    if ( Math.random() < 1.0 / ( unbreaking + 1 ) )
      durab -= amount;
    
    
    int portion = durab * toolMaxDurab / maxDurab;
    if ( durab <= 0 ) durab = portion = 0; // Effectively break tool...
    else if ( portion < 5 ) portion = 5; // Prevent Sync Issues
    

    System.out.println( "DURA: " + durab + " OF: " + maxDurab + " IS " + portion + " OF " + toolMaxDurab );
    
    lore.set( 0, generateDurabilityString( durab ) );
    meta.setLore( lore );
    stack.setItemMeta( meta );
    stack.setDurability( (short) (  toolMaxDurab - portion ) );
  }
}
