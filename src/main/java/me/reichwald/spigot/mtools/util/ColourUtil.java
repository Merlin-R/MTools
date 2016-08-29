
package me.reichwald.spigot.mtools.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class ColourUtil
{
  
  private static final Base64.Encoder B64ENCODER = Base64.getEncoder();
  private static final Base64.Decoder B64DECODER = Base64.getDecoder();
  private static final SecureRandom   RANDOM;
  static
  {
    SecureRandom tmp = null;
    try
    {
      tmp = SecureRandom.getInstanceStrong();
    }
    catch ( Exception e )
    {
    }
    RANDOM = tmp;
  }
  
  
  
  private static final String[] BYTE_MAP = new String[ 256 ];
  static
  {
    for ( int i = 0; i < 256; ++i )
      BYTE_MAP[ i ] = '§' + Integer.toHexString( i >> 4 ) + '§' + Integer.toHexString( i & 15 );
  }
  
  
  private static final byte[][] CHAR_MAP = new byte[ 'f' + 1 ][];
  static
  {
    char[] chars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    for ( byte i = 0; i < chars.length; ++i )
    {
      CHAR_MAP[ chars[ i ] ] = new byte[ 'f' + 1 ];
      for ( byte j = 0; j < chars.length; ++j )
        CHAR_MAP[ chars[ i ] ][ chars[ j ] ] = ( byte ) ( ( i << 4 ) | ( j & 15 ) );
    }
  }
  
  private static final String _CONSTANT_PATTERN_ = "§r§r%s§l§r";
  
  private static final String END_OF_COLOUR      = String.format( _CONSTANT_PATTERN_, encodeColour( "1776b0e8" ) );
  private static final String DELIMITER          = String.format( _CONSTANT_PATTERN_, encodeColour( "853b38f3" ) );
  private static final String PAIR_DELIMITER     = String.format( _CONSTANT_PATTERN_, encodeColour( "55f150bb" ) );
  
  
  public static final String encodeColour( String orig, String suffix )
  {
    return encodeColour( orig ) + END_OF_COLOUR + suffix;
  }
  
  
  public static final String encodeColour( String orig )
  {
    byte[] bytes = orig.getBytes();
    StringBuilder enc = new StringBuilder();
    
    for ( byte b : bytes )
      enc.append( BYTE_MAP[ b ] );
    
    return enc.toString();
  }
  
  
  public static final String decodeColour( String encoded )
  {
    int end = encoded.indexOf( END_OF_COLOUR );
    if ( end != -1 ) encoded = encoded.substring( 0, end );
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    
    char[] chars = encoded.toLowerCase().toCharArray();
    
    for ( int i = 1; i < chars.length; i += 4 )
      bos.write( CHAR_MAP[ chars[ i ] ][ chars[ i + 2 ] ] );
    
    return new String( bos.toByteArray() );
  }
  
  
  public static final String generateUniqueColourString()
  {
    return encodeColour( new String( RANDOM.generateSeed( 32 ) ) );
  }
  
  
  public static final String encodeColour( List<String> strings )
  {
    StringBuilder sb = new StringBuilder();
    
    strings.forEach(
        ( s ) -> {
          sb.append( ColourUtil.encodeColour( s ) ).append( DELIMITER );
        }
    );
    
    return sb.substring( 0, sb.length() - DELIMITER.length() );
  }
  
  
  public static final String encodeColour( List<String> strings, String suffix )
  {
    return encodeColour( strings ) + END_OF_COLOUR + suffix;
  }
  
  
  public static final List<String> decodeColourList( String encoded )
  {
    List<String> ret = Lists.newArrayList();
    
    int end = encoded.indexOf( END_OF_COLOUR );
    if ( end != -1 ) encoded = encoded.substring( 0, end );
    
    Arrays.stream( encoded.split( DELIMITER ) ).map( ColourUtil::decodeColour ).forEach( ret::add );
    
    return ret;
  }
  
  public static final String encodeColour( Map<String, String> mapping )
  {
    StringBuilder ret = new StringBuilder();
    
    mapping.entrySet().forEach((e)->{
      ret.append( encodeColour( e.getKey() ) )
         .append( PAIR_DELIMITER )
         .append( encodeColour( e.getValue() ) )
         .append( DELIMITER );
    });
    
    return ret.substring( 0, ret.length() - DELIMITER.length() );
  }
  
  public static final Map<String, String> encodeColourMap( String encoded, String suffix )
  {
    Map<String, String> ret = Maps.newHashMap();

    int end = encoded.indexOf( END_OF_COLOUR );
    if ( end != -1 ) encoded = encoded.substring( 0, end );
    
    Arrays.stream( encoded.split( DELIMITER ) ).map( ( s ) -> s.split( PAIR_DELIMITER ) ).forEach( ( p ) -> {
      ret.put( p[ 0 ], p[ 1 ] );
    });
    
    return ret;
  }
  
  
}
