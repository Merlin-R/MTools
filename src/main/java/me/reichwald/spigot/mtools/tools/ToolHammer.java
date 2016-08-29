package me.reichwald.spigot.mtools.tools;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.reichwald.spigot.mtools.MTools;
import me.reichwald.spigot.mtools.blocks.BlockTraceData;
import me.reichwald.spigot.mtools.blocks.BlockUtil;

public class ToolHammer extends AbstractToolAdapter
{
  
  
  private int id;
  private String displayName;
  private int durability;
  private Material base;
  private Recipe recipe;
  private List<String> lore;
  
  
  
  public ToolHammer( int id, String displayName, int durability, Material base, String... lore )
  {
    this( id, displayName, durability, base, Arrays.asList( lore ) );
  }
  public ToolHammer( int id, String displayName, int durability, Material base, List<String> lore )
  {
    
    this.id = id;
    this.displayName = displayName;
    this.durability = durability;
    this.base = base;
    this.lore = lore;
  }
  
  public ToolHammer setRecipe( Recipe recipe )
  {
    this.recipe = recipe;
    return this;
  }
  
  @Override
  public String getDisplayName()
  {
    return displayName;
  }

  @Override
  public int getId()
  {
    return id;
  }

  @Override
  public int getMaximumDurability()
  {
    return durability;
  }

  @Override
  public Material getBaseMaterial()
  {
    return base;
  }

  @Override
  public Recipe getCraftingRecipe()
  {
    return recipe;
  }

  @Override
  public List<String> getLore()
  {
    return lore;
  }
  
  @Override
  public void onBlockBreak( Player player, ItemStack item, BlockBreakEvent event, BlockTraceData data )
  {
    if ( !player.isSneaking() )
    for ( Block c : BlockUtil.QUERY_SQUARE.query( event.getBlock(), 1, BlockUtil.getOrthogonal( data.getFace() ) ) )
    {
      if ( MTools.canPlayerBreakBlock( player, item, c ) )
        MTools.playerBreakBlock( player, item, c );
    }
  }
  
}
