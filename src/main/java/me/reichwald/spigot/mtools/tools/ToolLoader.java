package me.reichwald.spigot.mtools.tools;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import me.reichwald.spigot.mtools.MTools;
import me.reichwald.spigot.mtools.tools.actions.ToolAction;
import me.reichwald.spigot.mtools.tools.actions.ToolActionPattern;

public class ToolLoader
{
  public static final void loadTools()
  {
    ToolHammer holzHammer = new ToolHammer( 0, "Holzhammer", 128, Material.WOOD_PICKAXE );
    holzHammer.setRecipe( shaped( holzHammer.create(), "lll","lsl"," s ", 'l', Material.LOG, 's', Material.STICK ) ).register();
    
    ToolHammer steinHammer = new ToolHammer( 1, "Steinhammer", 512, Material.WOOD_PICKAXE );
    steinHammer.setRecipe( shaped( steinHammer.create(), "fff", "fsf", " s ", 'f', Material.FLINT, 's', Material.STICK ) ).register();
    
    ToolHammer eisenHammer = new ToolHammer( 2, "Eisenhammer", 2048, Material.STONE_PICKAXE, "Bergischer Hammer, bergisches Land..." );
    eisenHammer.setRecipe( shaped( eisenHammer.create(), "ibi", "isi", " s ", 'i', Material.IRON_INGOT, 'b', Material.IRON_BLOCK, 's', Material.STICK ) ).register();
    
    ToolHammer goldHammer = new ToolHammer( 3, "Goldhammer", 1024, Material.GOLD_PICKAXE, "Diggy Diggy Hole~" );
    goldHammer.setRecipe( shaped( goldHammer.create(), "ibi", "isi", " s ", 'i', Material.IRON_INGOT, 'b', Material.IRON_BLOCK, 's', Material.STICK ) ).register();
    
    ToolHammer diamantHammer = new ToolHammer( 4, "Diamanthammer", 8192, Material.DIAMOND_PICKAXE, "Nobel und Mächtig." );
    diamantHammer.setRecipe( shaped( diamantHammer.create(), "ibi", "isi", " s ", 'i', Material.DIAMOND, 'b', Material.DIAMOND_BLOCK, 's', Material.STICK ) ).register();
    
    InventoryTool bag = new InventoryTool() {

      @Override
      public int getInventorySize()
      {
        return 9;
      }

      @Override
      public String getInventoryTitle()
      {
        // TODO Auto-generated method stub
        return "I am a bag";
      }

      @Override
      public String getDisplayName()
      {
        return "Bag";
      }

      @Override
      public int getId()
      {
        return 5;
      }

      @Override
      public int getMaximumDurability()
      {
        return 0;
      }

      @Override
      public Material getBaseMaterial()
      {
        return Material.BOWL;
      }

      @Override
      public Recipe getCraftingRecipe()
      {
        ShapedRecipe r = new ShapedRecipe( this.create() ).shape( "xxx", "xsx", "xxx" );
        r.setIngredient( 'x', Material.LEATHER );
        r.setIngredient( 's', Material.STRING );
        return r;
      }

      @Override
      public List<String> getLore()
      {
        return new LinkedList<String>();
      }
    };
    
    /*
    FileConfiguration cfg = MTools.CFG;
    ConfigurationSection tools = cfg.getConfigurationSection( "tools" );
    for ( String toolName : tools.getKeys( false ) )
    {
      
    }*/
  }
  
  public static final ShapedRecipe shaped( ItemStack result, String r0, String r1, String r2, Object...mapping )
  {
    ShapedRecipe r = new ShapedRecipe( result ).shape( r0, r1, r2 );
    int pairs = mapping.length / 2;
    for ( int i = 0; i < pairs; ++i )
      r = r.setIngredient( (char) mapping[ i * 2 ], (Material) mapping[ i * 2 + 1] );
    return r;
  }
  
  public static final ShapedRecipe shaped( ItemStack result, String r0, String r1, Object...mapping )
  {
    ShapedRecipe r = new ShapedRecipe( result ).shape( r0, r1 );
    int pairs = mapping.length / 2;
    for ( int i = 0; i < pairs; ++i )
      r = r.setIngredient( (char) mapping[ i * 2 ], (Material) mapping[ i * 2 + 1] );
    return r;
  }
  
  @SuppressWarnings( "unchecked" )
  public static final void generateTools()
  {
    FileConfiguration cfg = MTools.CFG;
    ConfigurationSection tools = cfg.getConfigurationSection( "tools" );
    for ( String toolName : tools.getKeys( false ) )
    {
      ConfigurationSection tool = tools.getConfigurationSection( toolName );
      ConfigurationSection variants = tool.getConfigurationSection( "variants" );
      

      String name = tool.getString( "name" );
      int durability = tool.getInt( "durability" );
      ItemStack item = tool.getItemStack( "item" );
      ConfigurationSection recipe = tool.getConfigurationSection( "recipe" );
      Map<String, Object> ingredients;
      List<String> pattern;
      if ( recipe != null )
      {
        ConfigurationSection is = recipe.getConfigurationSection( "ingredients" );
        ConfigurationSection ps = recipe.getConfigurationSection( "pattern" );
        if ( is != null )
          ingredients = is.getValues( false );
        if ( ps != null )
          pattern = (List<String>) is.getList( "pattern" );
      }
      
      if ( variants == null )
      {
        
        
      }
      
    }
    
    
    /*
    FileConfiguration cfg = MTools.CFG;
    ConfigurationSection tools = cfg.createSection( "tools" );
    
    ConfigurationSection hammer = tools.createSection( "Hammer" );
    ConfigurationSection wandOfEqualTrace = tools.createSection( "Wand of equal Trade" );
    ConfigurationSection lumberaxe = tools.createSection( "Lumberaxe" );
    
    ConfigurationSection hammerBlockBreak = hammer.createSection( "block-break" );
    */
    
    
    
    
    
    
    
  }
  
  private static final void generateTool( String toolKind, String toolName, Material toolBase, Recipe recipe, int durability, ToolTrigger trigger, ToolAction action, ToolActionPattern pattern, int radius )
  {
    
  }
}
