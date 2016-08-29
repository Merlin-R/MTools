package me.reichwald.spigot.mtools.tools.actions;

import java.util.Map;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import me.reichwald.spigot.mtools.blocks.BlockTraceData;
import me.reichwald.spigot.mtools.blocks.BlockUtil;
import me.reichwald.spigot.mtools.blocks.IBlockQuery;
import me.reichwald.spigot.mtools.tools.AbstractTool;

public enum ToolAction
{
  BREAK_BLOCK,
  PLACE_BLOCK,
  DAMAGE_AOE,
  HEAL_AOE,
  POTION_AOE;
  
  private static final Map<String, ToolAction> LOOKUP = Maps.newHashMap();
  
  static {
    for ( ToolAction a : ToolAction.values() )
      LOOKUP.put( a.name(), a );
  }
  
  public ToolAction lookup( String name )
  {
    return LOOKUP.get( name.toUpperCase().replace( '-', '_' ) );
  }
  
}
