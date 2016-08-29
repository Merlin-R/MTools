package me.reichwald.spigot.mtools.blocks;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.google.common.collect.Lists;

public class BlockUtil
{
  // [[ Block Face Stuff ]]
  
  public static final BlockFace[] FACES_XYZ = new BlockFace[] {
    BlockFace.NORTH,
    BlockFace.SOUTH,
    BlockFace.EAST,
    BlockFace.WEST,
    BlockFace.UP,
    BlockFace.DOWN
  };
  public static final BlockFace[] FACES_XZ = new BlockFace[] {
    BlockFace.NORTH,
    BlockFace.SOUTH,
    BlockFace.EAST,
    BlockFace.WEST
  };
  public static final BlockFace[] FACES_XY = new BlockFace[] {
    BlockFace.EAST,
    BlockFace.WEST,
    BlockFace.UP,
    BlockFace.DOWN
  };
  public static final BlockFace[] FACES_YZ = new BlockFace[] {
    BlockFace.NORTH,
    BlockFace.SOUTH,
    BlockFace.UP,
    BlockFace.DOWN
  };
  public static final BlockFace[] getOrthogonal( BlockFace face )
  {
    switch ( face )
    {
    case NORTH:
    case SOUTH:
      return FACES_XY;
    case EAST:
    case WEST:
      return FACES_YZ;
    case UP:
    case DOWN:
      return FACES_XZ;
    default:
      return null;
    }
  }
  // ]] Block Face Stuff [[
  
  
  public static final IBlockQuery QUERY_DIAMOND = new IBlockQuery() {

    @Override
    public List<Block> query( Block start, int steps )
    {
      return query( start, steps, FACES_XYZ );
    }

    @Override
    public List<Block> queryWhitelist( Block start, Set<Material> whitelist, int steps )
    {
      return queryWhitelist( start, whitelist, steps, FACES_XYZ );
    }

    @Override
    public List<Block> queryBlacklist( Block start, Set<Material> blacklist, int steps )
    {
      return queryBlacklist( start, blacklist, steps, FACES_XYZ );
    }

    @Override
    public List<Block> query( Block start, int steps, BlockFace[] directions )
    {
      List<Block> checked = Lists.newLinkedList();
      List<Block> unchecked = Lists.newLinkedList();
      unchecked.add( start );
      while( steps-- > 0 )
      {
        Block block = unchecked.remove( 0 );
        for ( BlockFace face : directions )
        {
          Block current = block.getRelative( face );
            if ( !checked.contains( current ) )
              unchecked.add( current );
        }
        checked.add( block );
      }
      return checked;
    }

    @Override
    public List<Block> queryWhitelist( Block start, Set<Material> whitelist, int steps, BlockFace[] directions )
    {
      List<Block> unchecked = Lists.newLinkedList();
      List<Block> checked = Lists.newLinkedList();
      
      unchecked.add( start );
      
      while ( steps-- > 0 && unchecked.size() > 0 )
      {
        Block block = unchecked.remove( 0 );
        for ( BlockFace face : directions )
        {
          Block current = block.getRelative( face );
          if ( whitelist.contains( current.getType() ) )
            if ( !checked.contains( current ) )
              unchecked.add( current );
        }
        checked.add( block );
      }
      
      return checked;
    }

    @Override
    public List<Block> queryBlacklist( Block start, Set<Material> blacklist, int steps, BlockFace[] directions )
    {
      List<Block> unchecked = Lists.newLinkedList();
      List<Block> checked = Lists.newLinkedList();
      
      unchecked.add( start );
      
      while ( steps-- > 0 && unchecked.size() > 0 )
      {
        Block block = unchecked.remove( 0 );
        for ( BlockFace face : directions )
        {
          Block current = block.getRelative( face );
          if ( !blacklist.contains( current.getType() ) )
            if ( !checked.contains( current ) )
              unchecked.add( current );
        }
        checked.add( block );
      }
      
      return checked;
    }
    
  };
  
  
  public static final IBlockQuery QUERY_SQUARE = new IBlockQuery() {

    @Override
    public List<Block> query( Block start, int steps )
    {
      return query( start, steps, FACES_XYZ );
    }

    @Override
    public List<Block> queryWhitelist( Block start, Set<Material> whitelist, int steps )
    {
      return queryWhitelist( start, whitelist, steps );
    }

    @Override
    public List<Block> queryBlacklist( Block start, Set<Material> blacklist, int steps )
    {
      return queryBlacklist( start, blacklist, steps );
    }

    @Override
    public List<Block> query( Block start, int steps, BlockFace[] directions )
    {
      List<Block> blocks = Lists.newLinkedList();
      
      boolean east = false, west = false, up = false, down = false, north = false, south = false;
      for ( BlockFace face : directions )
        switch ( face )
        {
        case EAST: east = true; break;
        case WEST: west = true; break;
        case UP: up = true; break;
        case DOWN: down = true; break;
        case NORTH: north = true; break;
        case SOUTH: south = true; break;
        default:
        }
      
      int xSize = 1 + ( ( east ? 1 : 0 ) + ( west ? 1 : 0 ) ) * steps;
      int ySize = 1 + ( ( up ? 1 : 0 ) + ( down ? 1 : 0 ) ) * steps;
      int zSize = 1 + ( ( south ? 1 : 0 ) + ( north ? 1 : 0 ) ) * steps;

      int xStart = start.getX() - ( west ? steps : 0 );
      int yStart = start.getY() - ( down ? steps : 0 );
      int zStart = start.getZ() - ( north ? steps : 0 );
      
      World w = start.getWorld();
      
      for ( int x = 0; x < xSize; ++x )
        for ( int y = 0; y < ySize; ++y )
          for ( int z = 0; z < zSize; ++z )
          {
            Block b = w.getBlockAt( xStart + x, yStart + y, zStart + z );
              blocks.add( b );
          }
      
      return blocks;
    }

    @Override
    public List<Block> queryWhitelist( Block start, Set<Material> whitelist, int steps, BlockFace[] directions )
    {
      List<Block> blocks = Lists.newLinkedList();

      boolean east = false, west = false, up = false, down = false, north = false, south = false;
      for ( BlockFace face : directions )
        switch ( face )
        {
        case EAST: east = true; break;
        case WEST: west = true; break;
        case UP: up = true; break;
        case DOWN: down = true; break;
        case NORTH: north = true; break;
        case SOUTH: south = true; break;
        default:
        }
      
      int xSize = 1 + ( ( east ? 1 : 0 ) + ( west ? 1 : 0 ) ) * steps;
      int ySize = 1 + ( ( up ? 1 : 0 ) + ( down ? 1 : 0 ) ) * steps;
      int zSize = 1 + ( ( south ? 1 : 0 ) + ( north ? 1 : 0 ) ) * steps;

      int xStart = start.getX() - ( west ? steps : 0 );
      int yStart = start.getY() - ( down ? steps : 0 );
      int zStart = start.getZ() - ( north ? steps : 0 );
      
      World w = start.getWorld();
      
      for ( int x = 0; x < xSize; ++x )
        for ( int y = 0; y < ySize; ++y )
          for ( int z = 0; z < zSize; ++z )
          {
            Block b = w.getBlockAt( xStart + x, yStart + y, zStart + z );
            if ( whitelist.contains( b.getType() ) )
              blocks.add( b );
          }
      
      return blocks;
    }

    @Override
    public List<Block> queryBlacklist( Block start, Set<Material> blacklist, int steps, BlockFace[] directions )
    {
      List<Block> blocks = Lists.newLinkedList();

      boolean east = false, west = false, up = false, down = false, north = false, south = false;
      for ( BlockFace face : directions )
        switch ( face )
        {
        case EAST: east = true; break;
        case WEST: west = true; break;
        case UP: up = true; break;
        case DOWN: down = true; break;
        case NORTH: north = true; break;
        case SOUTH: south = true; break;
        default:
        }
      
      int xSize = 1 + ( ( east ? 1 : 0 ) + ( west ? 1 : 0 ) ) * steps;
      int ySize = 1 + ( ( up ? 1 : 0 ) + ( down ? 1 : 0 ) ) * steps;
      int zSize = 1 + ( ( south ? 1 : 0 ) + ( north ? 1 : 0 ) ) * steps;

      int xStart = start.getX() - ( west ? steps : 0 );
      int yStart = start.getY() - ( down ? steps : 0 );
      int zStart = start.getZ() - ( north ? steps : 0 );
      
      World w = start.getWorld();
      
      for ( int x = 0; x < xSize; ++x )
        for ( int y = 0; y < ySize; ++y )
          for ( int z = 0; z < zSize; ++z )
          {
            Block b = w.getBlockAt( xStart + x, yStart + y, zStart + z );
            if ( !blacklist.contains( b.getType() ) )
              blocks.add( b );
          }
      
      return blocks;
    }
    
  };
  
  
  public static final IBlockQuery QUERY_CONNECTED = new IBlockQuery() {

    @Override
    public List<Block> query( Block start, int steps )
    {
      return query( start, steps, FACES_XYZ );
    }

    @Override
    public List<Block> queryWhitelist( Block start, Set<Material> whitelist, int steps )
    {
      return queryWhitelist( start, whitelist, steps, FACES_XYZ );
    }

    @Override
    public List<Block> queryBlacklist( Block start, Set<Material> blacklist, int steps )
    {
      return queryBlacklist( start, blacklist, steps, FACES_XYZ );
    }

    @Override
    public List<Block> query( Block start, int steps, BlockFace[] directions )
    {
      List<Block> ret = Lists.newArrayList();
      List<Block> toCheck = Lists.newLinkedList();
      
      toCheck.add( start );
      
      while ( steps-- > 0 )
      {
        List<Block> query = Lists.newArrayList();
        while ( !toCheck.isEmpty() )
        {
          Block b = toCheck.remove( 0 );
          query = QUERY_SQUARE.query( start, 1, directions );
          query.remove( b );
          if ( !ret.contains( b ) )
            ret.add( b );
        }
        toCheck.addAll( query );
      }
      for ( Block b : toCheck )
        if ( !ret.contains( b ) )
          ret.add( b );
      return ret;
    }

    @Override
    public List<Block> queryWhitelist( Block start, Set<Material> whitelist, int steps, BlockFace[] directions )
    {
      List<Block> ret = Lists.newArrayList();
      List<Block> toCheck = Lists.newLinkedList();
      
      toCheck.add( start );
      
      while ( steps-- > 0 )
      {
        List<Block> query = Lists.newArrayList();
        while ( !toCheck.isEmpty() )
        {
          Block b = toCheck.remove( 0 );
          query = QUERY_SQUARE.queryWhitelist( start, whitelist, 1, directions );
          query.remove( b );
          if ( !ret.contains( b ) )
            if ( whitelist.contains( b.getType() ) )
              ret.add( b );
        }
        toCheck.addAll( query );
      }
      for ( Block b : toCheck )
        if ( !ret.contains( b ) )
          if ( whitelist.contains( b.getType() ) )
            ret.add( b );
      return ret;
    }

    @Override
    public List<Block> queryBlacklist( Block start, Set<Material> blacklist, int steps, BlockFace[] directions )
    {
      List<Block> ret = Lists.newArrayList();
      List<Block> toCheck = Lists.newLinkedList();
      
      toCheck.add( start );
      
      while ( steps-- > 0 )
      {
        List<Block> query = Lists.newArrayList();
        while ( !toCheck.isEmpty() )
        {
          Block b = toCheck.remove( 0 );
          query = QUERY_SQUARE.queryBlacklist( start, blacklist, 1, directions );
          query.remove( b );
          if ( !ret.contains( b ) )
            if ( !blacklist.contains( b.getType() ) )
              ret.add( b );
        }
        toCheck.addAll( query );
      }
      for ( Block b : toCheck )
        if ( !ret.contains( b ) )
          if ( !blacklist.contains( b.getType() ) )
            ret.add( b );
      return ret;
    }
  };

  
  
  
}
