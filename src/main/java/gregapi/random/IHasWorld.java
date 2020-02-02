/**
 * Copyright (c) 2019 Gregorius Techneticies
 *
 * This file is part of GregTech.
 *
 * GregTech is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GregTech is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GregTech. If not, see <http://www.gnu.org/licenses/>.
 */

package gregapi.random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * @author Gregorius Techneticies
 * 
 * Contains simple Utility Functions based just on the World of the Implementor.
 */
public interface IHasWorld {
	public World getWorld();
	
	public boolean isServerSide();
	public boolean isClientSide();
	
	public int rng(int aRange);
	public int getRandomNumber(int aRange);
	
	public TileEntity getTileEntity(int aX, int aY, int aZ);
	public TileEntity getTileEntity(ChunkPos aCoords);
	
	public Block getBlock(int aX, int aY, int aZ);
	public Block getBlock(ChunkPos aCoords);
	
	public byte getMetaData(int aX, int aY, int aZ);
	public byte getMetaData(ChunkPos aCoords);
	
	public byte getLightLevel(int aX, int aY, int aZ);
	public byte getLightLevel(ChunkPos aCoords);
	
	public boolean getOpacity(int aX, int aY, int aZ);
	public boolean getOpacity(ChunkPos aCoords);
	
	public boolean getSky(int aX, int aY, int aZ);
	public boolean getSky(ChunkPos aCoords);
	
	public boolean getRain(int aX, int aY, int aZ);
	public boolean getRain(ChunkPos aCoords);
	
	public boolean getAir(int aX, int aY, int aZ);
	public boolean getAir(ChunkPos aCoords);
	
	public BiomeGenBase getBiome(int aX, int aZ);
	public BiomeGenBase getBiome(ChunkPos aCoords);
}
