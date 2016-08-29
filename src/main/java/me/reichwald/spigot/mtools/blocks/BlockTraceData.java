package me.reichwald.spigot.mtools.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockTraceData
{
  private BlockFace face;
  private Block block;
  
  public final BlockFace getFace()
  {
    return face;
  }
  
  public final Block getBlock()
  {
    return block;
  }
  
  public final void setFace( BlockFace face )
  {
    this.face = face;
  }
  
  public final void setBlock( Block block )
  {
    this.block = block;
  }

  public BlockTraceData( BlockFace face, Block block )
  {
    super();
    this.face = face;
    this.block = block;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( block == null ) ? 0 : block.hashCode() );
    result = prime * result + ( ( face == null ) ? 0 : face.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( getClass() != obj.getClass() ) return false;
    BlockTraceData other = (BlockTraceData) obj;
    if ( block == null )
    {
      if ( other.block != null ) return false;
    }
    else if ( !block.equals( other.block ) ) return false;
    if ( face != other.face ) return false;
    return true;
  }
  
  
  
  
}
