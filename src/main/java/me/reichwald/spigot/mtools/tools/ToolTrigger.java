package me.reichwald.spigot.mtools.tools;

import java.util.HashMap;

import com.google.common.collect.Maps;

public enum ToolTrigger
{
  LEFT_CLICK_BLOCK,
  RIGHT_CLICK_BLOCK,
  LEFT_CLICK_AIR,
  RIGHT_CLICK_AIR,
  LEFT_CLICK_ENTITY,
  RIGHT_CLICK_ENTITY,
  BREAK_BLOCK,
  TOOL_DAMAGE,
  TOOL_BREAK;
  
  private static final HashMap<String, ToolTrigger> LOOKUP = Maps.newHashMap();
  
  static {
    for ( ToolTrigger t : ToolTrigger.values() )
      LOOKUP.put( t.name(), t );
  }
  
  public static ToolTrigger lookup( String name )
  {
    return LOOKUP.get( name.toUpperCase().replace( '-', '_' ) );
  }
  
}
