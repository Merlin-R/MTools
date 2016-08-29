package me.reichwald.spigot.mtools.util;

import java.io.ByteArrayOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemUtils
{

  public static ItemStack deserializeLore( String line )
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    for ( int i = 0; i < line.length(); i += 4 )
    {
      bos.write( Integer.parseInt( ( line.charAt( i + 1 ) + "" + line.charAt( i + 3 ) ), 16 ) );
    }
    
    byte[] bytes = bos.toByteArray();
    
    return deserialize( new String( bytes ) );
  }
  
  public static String serializeLore( ItemStack stack )
  {
    String serialized = serialize( stack );
    
    byte[] bytes = serialized.getBytes();
    
    StringBuilder sb = new StringBuilder();
    
    for ( byte b : bytes )
      sb.append( "§" ).append( Integer.toHexString( b / 16 ) ).append( "§" ).append( Integer.toHexString( b % 16 ) );
    
    return sb.toString();
  }
  
  public static String serialize( ItemStack stack )
  {
    YamlConfiguration yaml = new YamlConfiguration();
    yaml.set( "item", stack );
    return yaml.saveToString();
  }
  
  public static ItemStack deserialize( String string )
  {
    YamlConfiguration yaml = new YamlConfiguration();
    try
    {
      yaml.loadFromString( string );
    }
    catch ( InvalidConfigurationException e )
    {
      e.printStackTrace();
      return null;
    }
    
    return yaml.getItemStack( "item" );
  }
  
}
