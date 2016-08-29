
package me.reichwald.spigot.mtools.unsafe;



import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.reichwald.util.reflection.ReflectClass;
import me.reichwald.util.reflection.ReflectHelper;
import me.reichwald.util.reflection.ReflectObject;


public enum VanillaActions
{
  
  BREAK_BLOCK()
  {
    
    @Override
    public void execute( Player player, ItemStack item, Block block )
    {
      Location l = block.getLocation();
      ReflectObject pos = getNMSClass( "BlockPosition" ).create( l.getBlockX(), l.getBlockY(), l.getBlockZ() );
      new ReflectObject( player ).callMethod( "getHandle" ).wrapAs( getNMSClass( "EntityPlayer" ).get() )
          .getValue( "playerInteractManager" ).wrapAs( getNMSClass( "PlayerInteractManager" ).get() )
          .callMethod( "breakBlock", pos );
    }
    
    
    @Override
    public boolean canExecute( Player player, ItemStack item, Block block )
    {
      Location l = block.getLocation();
      ReflectObject pos = getNMSClass( "BlockPosition" ).create( l.getBlockX(), l.getBlockY(), l.getBlockZ() );
      
      ReflectObject world = new ReflectObject( block.getWorld() ).callMethod( "getHandle" )
          .wrapAs( getNMSClass( "World" ).get() );
      ReflectObject type = world.callMethod( "getType", pos.as( getNMSClass( "BlockPosition" ).get() ) );
      
      ReflectObject nmsItemStack = getCBClass( "inventory.CraftItemStack" ).callMethodStatic(
          "asNMSCopy", new ReflectObject( item ).wrapAs( ItemStack.class )
      );
      
      nmsItemStack = nmsItemStack.wrapAs( getNMSClass( "ItemStack" ).get() );
      
      ReflectObject nmsItem = nmsItemStack.callMethod( "getItem" ).wrapAs( getNMSClass( "Item" ).get() );
      
      return nmsItem.callMethod( "canDestroySpecialBlock", type.wrapAs( getNMSClass( "IBlockData" ).get() ) )
          .as( Boolean.class );
    }
  },
  
  ;
  
  VanillaActions()
  {
  }
  
  
  static void log( Object... objects )
  {
    Bukkit.getServer().broadcastMessage( Arrays.toString( objects ) );
  }
  
  private static final String BUKKIT_VERSION = Bukkit.getServer().getClass().getCanonicalName().split( "\\." )[ 3 ];
  
  
  public static String getBukkitVersion()
  {
    return BUKKIT_VERSION;
  }
  
  
  public static ReflectClass getCBClass( String suffix )
  {
    return ReflectHelper.getClass( "org.bukkit.craftbukkit." + getBukkitVersion() + "." + suffix );
  }
  
  
  public static ReflectClass getNMSClass( String suffix )
  {
    return ReflectHelper.getClass( "net.minecraft.server." + getBukkitVersion() + "." + suffix );
  }
  
  
  public abstract void execute( Player player, ItemStack item, Block block );
  
  
  public abstract boolean canExecute( Player player, ItemStack item, Block block );
  
  
}
