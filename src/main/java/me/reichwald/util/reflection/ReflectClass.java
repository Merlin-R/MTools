package me.reichwald.util.reflection;

import java.lang.reflect.Constructor;

public class ReflectClass implements IReflectWrapper
{
  Class<?> wrapped;
  
  public ReflectClass( Class<?> wrapped )
  {
    this.wrapped = wrapped;
  }
  
  public ReflectObject callMethod( String name, Object instance, Object... args )
  {
    Object[] unwrapped = ReflectHelper.unpackWrapped( args );
    try
    {
      return new ReflectObject( wrapped.getMethod( name, ReflectHelper.getObjectClasses( unwrapped ) ).invoke( instance, unwrapped ), this );
    }
    catch ( Exception e )
    {
      e.printStackTrace();
      return null;
    }
  }
  
  public ReflectObject callMethodStatic( String name, Object...args )
  {
    return callMethod( name, null, args );
  }
  
  public ReflectObject getValue( String field, Object instance )
  {
    try
    {
      return new ReflectObject( wrapped.getField( field ).get( instance ), this );
    }
    catch ( Exception e )
    {
      e.printStackTrace();
      return null;
    }
  }
  
  public ReflectObject getValueStatic( String field )
  {
    return getValue( field, null );
  }
  
  public Class<?> get()
  {
    return wrapped;
  }
  
  public String toString()
  {
    return String.valueOf( get() );
  }
  
  public ReflectObject create( Object...args )
  {
    Object[] unwrapped = ReflectHelper.unpackWrapped( args );
    Class<?>[] classes = ReflectHelper.getObjectClasses( unwrapped );
    try
    {
      Constructor<?> constructor = null;
      try {
        constructor = wrapped.getConstructor( ReflectHelper.getObjectClasses( unwrapped ) );
      }
      catch( NoSuchMethodException e )
      {}
      int i = 0;
      while ( constructor == null && i < classes.length )
      {
        classes[ i ] = ReflectHelper.getPrimitiveClass( classes[ i++ ] );
        try {
          constructor = wrapped.getConstructor( classes );
        }
        catch( NoSuchMethodException e )
        {}
      }
      return new ReflectObject( constructor.newInstance( unwrapped ), this );
    }
    catch ( Exception e )
    {
      e.printStackTrace();
      return null;
    }
    
  }
  
}
