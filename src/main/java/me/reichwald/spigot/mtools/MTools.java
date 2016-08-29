package me.reichwald.spigot.mtools;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;

import me.reichwald.spigot.mtools.actions.VanillaActions;
import me.reichwald.spigot.mtools.blocks.BlockTraceData;
import me.reichwald.spigot.mtools.tools.AbstractTool;
import me.reichwald.spigot.mtools.tools.InventoryTool;
import me.reichwald.spigot.mtools.tools.ToolLoader;

public class MTools extends JavaPlugin implements Listener
{
  public static FileConfiguration CFG; 
  public static Server SERVER;
  
  @Override
  public void onEnable()
  {
    CFG = getConfig();
    try {
      createCfg();
    } catch ( IOException e )
    {
      getLogger().log( Level.SEVERE, "Could not generate default configuration file, aborting.", e );
      setEnabled( false );
      return;
    }
    catch ( InvalidConfigurationException e )
    {
      getLogger().log( Level.SEVERE, "Configuration file invalid, aborting.", e );
      setEnabled( false );
      return;
    }
    SERVER = getServer();
    SERVER.getPluginManager().registerEvents( this, this );
    ToolLoader.loadTools();
    
    
    
  }
  
  private void createCfg() throws IOException, InvalidConfigurationException
  {
    File confFile = new File( getDataFolder(), "config.yml");
    if ( !confFile.exists() )
    {
      confFile.getParentFile().mkdirs();
      CFG.options().copyDefaults( true );
      CFG.save( confFile );
    }
    else
      CFG.load( confFile );
  }
  
  @Override
  public void onDisable()
  {
    eventBlock.clear();
    blockTraceData.clear();
  }
  
  private boolean isLocked( Player player )
  {
    return eventBlock.getOrDefault( player, false );
  }
  
  private void lock( Player player )
  {
    eventBlock.put( player, true );
  }
  
  private void unlock( Player player )
  {
    eventBlock.remove( player );
  }
  
  @EventHandler
  public void digest( BlockBreakEvent event )
  {
    Player player = event.getPlayer();
    ItemStack stack = toolsUsed.getOrDefault( player, player.getInventory().getItemInMainHand() );
    
    AbstractTool tool = AbstractTool.byItemStack( stack );
    if ( tool != null )
    {
      if ( !isLocked( player ) )
      {
        lock( player );
        tool.onBlockBreak( player, stack, event, blockTraceData.get( player ) );
        unlock( player );
      }
      else
      {
        // Workaround until PlayerItemDamageEvent is actually fired
        // digest( new PlayerItemDamageEvent( player, stack, stack.getDurability() ) );
      }
    }
  }
  
  @EventHandler
  public void digest( PlayerItemBreakEvent event )
  {
    Player player = event.getPlayer();
    ItemStack stack = event.getBrokenItem();
    AbstractTool tool = AbstractTool.byItemStack( stack );
    if ( tool != null )
      tool.onItemBreak( player, stack, event );
  }
  
  @EventHandler
  public void digest( PlayerItemDamageEvent event )
  {
    Player player = event.getPlayer();
    ItemStack stack = event.getItem();
    AbstractTool tool = AbstractTool.byItemStack( stack );
    if ( tool != null )
      tool.digest( player, stack, event );
  }
  
  @EventHandler
  public void digest( PlayerInteractEntityEvent event )
  {
    Player player = event.getPlayer();
    ItemStack stack = event.getHand() == EquipmentSlot.HAND ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
    AbstractTool tool = AbstractTool.byItemStack( stack );
    if ( tool != null )
      if ( !isLocked( player ) )
      {
        lock( player );
        tool.onRightClickEntity( player, stack, event );
        unlock( player );
      }
  }
  
  
  @EventHandler
  public void digest( EntityDamageByEntityEvent event )
  {
    if ( event.getDamager() instanceof Player )
    {
      Player player = (Player) event.getDamager();
      ItemStack stack = toolsUsed.getOrDefault( player, player.getInventory().getItemInMainHand() );
      AbstractTool tool = AbstractTool.byItemStack( stack );
      if ( tool != null )
        if ( !isLocked( player ) )
        {
          lock( player );
          tool.onLeftClickEntity( player, stack, event );
          unlock( player );
        }
    }
  }
  
  @EventHandler
  public void digest( PlayerInteractEvent event )
  {
    Player player = event.getPlayer();
    ItemStack stack = event.getItem();
    AbstractTool tool = AbstractTool.byItemStack( stack );
    toolsUsed.put( player, stack );
    if ( tool != null )
    switch ( event.getAction() )
    {
    case LEFT_CLICK_AIR:
      tool.onLeftClickAir( player, stack, event, blockTraceData.get( player ) );
      break;
    case LEFT_CLICK_BLOCK:
      BlockTraceData data = new BlockTraceData( event.getBlockFace(), event.getClickedBlock() );
      blockTraceData.put( event.getPlayer(), data );
      tool.onLeftClickBlock( player, stack, event, blockTraceData.get( player ) );
      break;
    case PHYSICAL:
      break;
    case RIGHT_CLICK_AIR:
      tool.onRightClickAir( player, stack, event, blockTraceData.get( player ) );
      break;
    case RIGHT_CLICK_BLOCK:
      BlockTraceData data2 = new BlockTraceData( event.getBlockFace(), event.getClickedBlock() );
      blockTraceData.put( event.getPlayer(), data2 );
      tool.onRightClickBlock( player, stack, event, data2 );
      break;
    default:
      break;
    }
  }
  
  public void digest( InventoryCloseEvent event )
  {
    Player player = (Player) event.getPlayer();
    ItemStack stack = toolsUsed.getOrDefault( player, player.getInventory().getItemInMainHand() );
    AbstractTool tool = AbstractTool.byItemStack( stack );
    if ( tool != null && tool instanceof InventoryTool )
    {
      InventoryTool iTool = ((InventoryTool) tool);
      if ( event.getInventory().getTitle().equals( iTool.getInventoryTitle() ) )
        iTool._inventoryClose( player, stack, event );
    }
  }
  
  public static void playerBreakBlock( Player player, ItemStack item, Block block )
  {
    VanillaActions.BREAK_BLOCK.execute( player, item, block );
  }
  
  public static boolean canPlayerBreakBlock( Player player, ItemStack item, Block block )
  {
    return VanillaActions.BREAK_BLOCK.canExecute( player, item, block );
  }
  
  Map<Player, BlockTraceData> blockTraceData = Maps.newHashMap(); 
  Map<Player, Boolean> eventBlock = Maps.newHashMap();
  Map<Player, ItemStack> toolsUsed = Maps.newHashMap(); 
}
