package me.reichwald.spigot.mtools.blocks;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public interface IBlockQuery
{
  public List<Block> query( Block start, int steps );
  public List<Block> queryWhitelist( Block start, Set<Material> whitelist, int steps );
  public List<Block> queryBlacklist( Block start, Set<Material> blacklist, int steps );
  
  public List<Block> query( Block start, int steps, BlockFace[] directions );
  public List<Block> queryWhitelist( Block start, Set<Material> whitelist, int steps, BlockFace[] directions );
  public List<Block> queryBlacklist( Block start, Set<Material> blacklist, int steps, BlockFace[] directions );
  
}
