
package me.reichwald.util.reflection;


import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class ReflectHelper
{
  
  public static final Map<Class<?>, Class<?>> PRIMITIVES     = new HashMap<Class<?>, Class<?>>();
  public static final Map<Class<?>, Class<?>> NON_PRIMITIVES = new HashMap<Class<?>, Class<?>>();
  static
  {
    PRIMITIVES.put( Boolean.class, boolean.class );
    PRIMITIVES.put( Byte.class, byte.class );
    PRIMITIVES.put( Short.class, short.class );
    PRIMITIVES.put( Character.class, char.class );
    PRIMITIVES.put( Integer.class, int.class );
    PRIMITIVES.put( Long.class, long.class );
    PRIMITIVES.put( Float.class, float.class );
    PRIMITIVES.put( Double.class, double.class );
    for ( Map.Entry<Class<?>, Class<?>> pair : PRIMITIVES.entrySet() )
      NON_PRIMITIVES.put( pair.getValue(), pair.getKey() );
  }
  
  
  public static ReflectClass getClass( String className )
  {
    try
    {
      return new ReflectClass( Class.forName( className ) );
    }
    catch ( ClassNotFoundException e )
    {
      e.printStackTrace();
      return null;
    }
  }
  
  
  public static Class<?>[] getObjectClasses( Object... objects )
  {
    LinkedList<Class<?>> classes = new LinkedList<Class<?>>();
    for ( Object o : objects )
      classes.add( o.getClass() );
    return classes.toArray( new Class<?>[ classes.size() ] );
  }
  
  
  public static Object[] unpackWrapped( Object... objects )
  {
    Object[] ret = Arrays.copyOf( objects, objects.length );
    
    for ( int i = 0; i < objects.length; ++i )
      if ( objects[ i ] instanceof IReflectWrapper ) ret[ i ] = ( ( IReflectWrapper ) objects[ i ] ).get();
    
    return ret;
  }
  
  
  public static Class<?> getPrimitiveClass( Class<?> clazz )
  {
    Class<?> ret = PRIMITIVES.get( clazz );
    if ( null == ret ) ret = clazz;
    return ret;
  }
  
}
