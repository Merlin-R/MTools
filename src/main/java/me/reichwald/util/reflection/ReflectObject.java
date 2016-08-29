
package me.reichwald.util.reflection;


public class ReflectObject implements IReflectWrapper
{
  
  private Object       wrapped;
  private ReflectClass clazz;
  
  
  public ReflectObject( Object wrapped, ReflectClass clazz )
  {
    this.wrapped = wrapped;
    this.clazz = clazz;
  }
  
  
  public ReflectObject( Object wrapped )
  {
    this( wrapped, new ReflectClass( wrapped.getClass() ) );
  }
  
  
  public Object get()
  {
    return wrapped;
  }
  
  
  public String toString()
  {
    return String.valueOf( get() );
  }
  
  
  @SuppressWarnings( "unchecked" )
  public <T> T as( Class<T> clazz )
  {
    return ( T ) wrapped;
  }
  
  
  @SuppressWarnings( "unchecked" )
  public <T> T cast()
  {
    return ( T ) wrapped;
  }
  
  
  public ReflectObject getValue( String field )
  {
    return clazz.getValue( field, wrapped );
  }
  
  
  public ReflectObject callMethod( String name, Object... args )
  {
    return clazz.callMethod( name, wrapped, args );
  }
  
}
