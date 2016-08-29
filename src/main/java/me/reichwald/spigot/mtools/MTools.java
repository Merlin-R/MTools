
package me.reichwald.spigot.mtools;


import java.util.Arrays;
import java.util.Map;

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
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;

import me.reichwald.spigot.mtools.util.ColourUtil;



public class MTools extends JavaPlugin implements Listener
{
  
  @Override
  public void onEnable()
  {
    getServer().getPluginManager().registerEvents( this, this );
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
    
  }
  
  
  @EventHandler
  public void digest( PlayerItemBreakEvent event )
  {
    
  }
  
  
  @EventHandler
  public void digest( PlayerItemDamageEvent event )
  {
    
  }
  
  
  @EventHandler
  public void digest( PlayerInteractEntityEvent event )
  {
    
  }
  
  
  @EventHandler
  public void digest( EntityDamageByEntityEvent event )
  {
    
  }
  
  
  @EventHandler
  public void digest( PlayerInteractEvent event )
  {
    String orig = event.getItem().getType().name();
    String enc = ColourUtil.encodeColour( orig, "§a" + orig );
    String dec = ColourUtil.decodeColour( enc );
    
    Arrays.asList( orig, enc, dec ).forEach( event.getPlayer()::sendMessage );
    
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void digest( InventoryCloseEvent event )
  {
    
  }
  
  Map<Player, Boolean> eventBlock = Maps.newHashMap();
  
}
