package me.reichwald.spigot.mtools.unsafe;



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
      
      new ReflectObject( player ).callMethod( "getHandle" ).getValue( "playerInteractManager" ).callMethod( "breakBlock", pos );
    }

    @Override
    public boolean canExecute( Player player, ItemStack item, Block block )
    {
      Location l = block.getLocation();
      ReflectObject pos = getNMSClass( "BlockPosition" ).create( l.getBlockX(), l.getBlockY(), l.getBlockZ() );
      
      ReflectObject type = new ReflectObject( block.getWorld() ).callMethod( "getHandle" ).callMethod( "getType", pos );
      ReflectObject nmsItem = getCBClass( "inventory.CraftItemStack" ).callMethodStatic( "asNMSCopy", item ).callMethod( "getItem" );
      
      return nmsItem.callMethod( "canDestroySpecialBlock", type ).as( Boolean.class );
    }
  },
  
  ;
  VanillaActions(){}
  
  public static String getBukkitVersion()
  {
    return Bukkit.getServer().getClass().getCanonicalName().split( "." )[ 3 ];
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
