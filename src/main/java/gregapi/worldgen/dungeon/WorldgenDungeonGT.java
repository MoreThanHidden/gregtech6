/**
 * Copyright (c) 2018 Gregorius Techneticies
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

package gregapi.worldgen.dungeon;

import static gregapi.data.CS.*;

import java.util.List;
import java.util.Random;
import java.util.Set;

import gregapi.block.metatype.BlockStones;
import gregapi.block.multitileentity.MultiTileEntityRegistry;
import gregapi.code.ArrayListNoNulls;
import gregapi.code.HashSetNoNulls;
import gregapi.data.CS.BlocksGT;
import gregapi.data.CS.ConfigsGT;
import gregapi.data.IL;
import gregapi.data.MD;
import gregapi.data.MT;
import gregapi.util.UT;
import gregapi.util.WD;
import gregapi.worldgen.WorldgenObject;
import gregtech.tileentity.misc.MultiTileEntityCoin;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import twilightforest.TwilightForestMod;

/**
 * @author Gregorius Techneticies
 */
public class WorldgenDungeonGT extends WorldgenObject {
	public static IDungeonChunk
	  ROOM_EMPTY  = new DungeonChunkRoomEmpty()
	, DOOR_PISTON = new DungeonChunkDoorPiston()
	, CORRIDOR    = new DungeonChunkCorridor()
	, PILLAR      = new DungeonChunkPillar()
	, ENTRANCE    = new DungeonChunkEntrance()
	;
	
	public static final List<IDungeonChunk> ROOMS = new ArrayListNoNulls<>(F, ROOM_EMPTY);
	
	public final int mProbability, mMinSize, mMaxSize, mMinY, mMaxY, mRoomChance;
	public final boolean mPortalNether, mPortalEnd, mPortalTwilight, mPortalMyst, mZPM;
	
	@SafeVarargs
	public WorldgenDungeonGT(String aName, boolean aDefault, int aProbability, int aMinSize, int aMaxSize, int aMinY, int aMaxY, int aRoomChance, boolean aOverworld, boolean aNether, boolean aEnd, boolean aPortalNether, boolean aPortalEnd, boolean aPortalTwilight, boolean aPortalMyst, List<WorldgenObject>... aLists) {
		super(aName, aDefault, aLists);
		mProbability        = Math.max(1,           ConfigsGT.WORLDGEN.get(mCategory, "Probability"     , aProbability));
		mMinSize            = Math.max(2,           ConfigsGT.WORLDGEN.get(mCategory, "MinSize"         , aMinSize));
		mMaxSize            = Math.max(mMinSize,    ConfigsGT.WORLDGEN.get(mCategory, "MaxSize"         , aMaxSize));
		mMinY               = Math.max(5,           ConfigsGT.WORLDGEN.get(mCategory, "MinY"            , aMinY));
		mMaxY               = Math.max(mMinY,       ConfigsGT.WORLDGEN.get(mCategory, "MaxY"            , aMaxY));
		mRoomChance         = Math.max(1,           ConfigsGT.WORLDGEN.get(mCategory, "RoomChance"      , aRoomChance));
		mPortalNether       =                       ConfigsGT.WORLDGEN.get(mCategory, "PortalNether"    , aPortalNether);
		mPortalEnd          =                       ConfigsGT.WORLDGEN.get(mCategory, "PortalEnd"       , aPortalEnd);
		mPortalTwilight     =                       ConfigsGT.WORLDGEN.get(mCategory, "PortalTwilight"  , aPortalTwilight);
		mPortalMyst         =                       ConfigsGT.WORLDGEN.get(mCategory, "PortalMyst"      , aPortalMyst);
		mZPM                =                       ConfigsGT.WORLDGEN.get(mCategory, "ZPMs"            , T);
	}
	
	public WorldgenDungeonGT() {this(null, F, 100, 3, 7, 20, 20, 6, F, F, F, F, F, F, F);}
	
	public static final int ROOM_ID_COUNT = 5;
	
	@Override
	public boolean generate(World aWorld, Chunk aChunk, int aDimType, int aMinX, int aMinZ, int aMaxX, int aMaxZ, Random aRandom, BiomeGenBase[][] aBiomes, Set<String> aBiomeNames) {
		if (aRandom.nextInt(mProbability) != 0 || checkForMajorWorldgen(aWorld, aMinX, aMinZ, aMaxX, aMaxZ)) return F;
		if (Math.abs(aMinZ) < 256+mMaxSize*16 && Math.abs(aMinX) < 256+mMaxSize*16) return F;
		if ((GENERATE_STREETS && aWorld.provider.dimensionId == DIM_OVERWORLD) && (Math.abs(aMinX) < 256+mMaxSize*16 || Math.abs(aMinZ) < 256+mMaxSize*16)) return F;
		if (Math.abs(aMinX/16)%(mMaxSize+4) != (mMaxSize+4)/2 || Math.abs(aMinZ/16)%(mMaxSize+4) != (mMaxSize+4)/2 || !WD.bedrock(aWorld, aMinX+8, 0, aMinZ+8)) return F;
		
		MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry("gt.multitileentity");
		
		if (tRegistry == null) return F;
		
		int tOffsetY = mMinY + aRandom.nextInt(Math.max(1, mMaxY-mMinY)), tColor = aRandom.nextInt(16);
		
		BlockStones tPrimaryBlock = (BlockStones)BlocksGT.stones[aRandom.nextInt(BlocksGT.stones.length)], tSecondaryBlock = (BlockStones)BlocksGT.stones[aRandom.nextInt(BlocksGT.stones.length)];
		
		HashSetNoNulls<ChunkCoordinates> tLightUpdateCoords = new HashSetNoNulls<>();
		
		byte[][] tRoomLayout = new byte[2+mMinSize+aRandom.nextInt(1+mMaxSize-mMinSize)][2+mMinSize+aRandom.nextInt(1+mMaxSize-mMinSize)];
		
		boolean[] tGeneratedKeys = new boolean[5];
		
		boolean
		  tDidntPortalNether    = (mPortalNether                            && (aWorld.provider.dimensionId == DIM_OVERWORLD || aWorld.provider.dimensionId == DIM_NETHER))
		, tDidntPortalEnd       = (mPortalEnd                               && (aWorld.provider.dimensionId == DIM_OVERWORLD || aWorld.provider.dimensionId == DIM_END))
		, tDidntPortalTwilight  = (mPortalTwilight  && MD.TF.mLoaded        && (aWorld.provider.dimensionId == DIM_OVERWORLD || aWorld.provider.dimensionId == TwilightForestMod.dimensionID))
		, tDidntPortalMyst      = (mPortalMyst      && MD.MYST.mLoaded      );
		
		long[] tKeyIDs = new long[tGeneratedKeys.length];
		tKeyIDs[0] = System.nanoTime();
		for (int i = 1; i < tKeyIDs.length; i++) tKeyIDs[i] = tKeyIDs[i-1]-1;
		ItemStack[] tKeyStacks = new ItemStack[tKeyIDs.length];
		for (int i = 0; i < tKeyIDs.length; i++) tKeyStacks[i] = IL.KEYS[aRandom.nextInt(IL.KEYS.length)].getWithNameAndNBT(1, "Key #"+(i+1), UT.NBT.makeLong(NBT_KEY, tKeyIDs[i]));
		
		aMinX -= (tRoomLayout   .length / 2) * 16;
		aMinZ -= (tRoomLayout[0].length / 2) * 16;
		
		for (int i = 0; i < tRoomLayout.length; i++) for (int j = 0; j < tRoomLayout[i].length; j++) aWorld.setBlock(aMinX+8+i*16, 254, aMinZ+8+j*16, NB, 0, 3);
		
		for (int i = 0, j = 0, k = -1, l = 0; k >= -2 && l < 10000; l++) {
			i = 1+aRandom.nextInt(tRoomLayout   .length-2);
			j = 1+aRandom.nextInt(tRoomLayout[i].length-2);
			if (tRoomLayout[i][j] == 0) {tRoomLayout[i][j] = (byte)k--;}
		}
		
		for (int i = 1; i < tRoomLayout.length-1; i++) for (int j = 1; j < tRoomLayout[i].length-1; j++) if (tRoomLayout[i][j] == 0) if (aRandom.nextInt(mRoomChance) == 0) tRoomLayout[i][j] = (byte)(1+aRandom.nextInt(ROOM_ID_COUNT));
		
		for (int i = 1; i < tRoomLayout.length-1; i++) for (int j = 1; j < tRoomLayout[i].length-1; j++) if (tRoomLayout[i][j] != 0) {
			int a = i, b = j;
			while (a != tRoomLayout   .length/2) {a+=(a>(tRoomLayout   .length/2)?-1:+1); if (tRoomLayout[a][b] == 0) tRoomLayout[a][b] = -128; else break;}
			while (b != tRoomLayout[a].length/2) {b+=(b>(tRoomLayout[a].length/2)?-1:+1); if (tRoomLayout[a][b] == 0) tRoomLayout[a][b] = -128; else break;}
		}
		
		NBTTagCompound tCoin = (NBTTagCompound)MultiTileEntityCoin.COIN_MAP.get(UT.Code.select(null, MT.Cu, MT.Cu, MT.Cu, MT.Ag, MT.Ag, MT.Au, MT.Au, MT.Pt)).getTagCompound().copy();
		
		boolean
		temp = T;
		while (temp) {
			temp = F;
			for (int i = 1; i < tRoomLayout.length-1; i++) for (int j = 1; j < tRoomLayout[i].length-1; j++) if (tRoomLayout[i][j] == -128) {
				if (tRoomLayout[i+1][j  ] != 0 && tRoomLayout[i-1][j  ] != 0 && tRoomLayout[i  ][j-1] == 0 && tRoomLayout[i  ][j+1] == 0) continue;
				if (tRoomLayout[i+1][j  ] == 0 && tRoomLayout[i-1][j  ] == 0 && tRoomLayout[i  ][j-1] != 0 && tRoomLayout[i  ][j+1] != 0) continue;
				
				int tConnectionCount = 0;
				for (byte tSide : ALL_SIDES_HORIZONTAL) if (tRoomLayout[i+OFFSETS_X[tSide]][j+OFFSETS_Z[tSide]] != 0) tConnectionCount++;
				
				if (tConnectionCount <= 1) {tRoomLayout[i][j] = 0; temp = T; continue;}
				
				if (tRoomLayout[i+1][j  ] != 0 && tRoomLayout[i+1][j+1] != 0 && tRoomLayout[i  ][j+1] != 0 && tRoomLayout[i-1][j  ] == 0 && tRoomLayout[i  ][j-1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
				if (tRoomLayout[i+1][j  ] != 0 && tRoomLayout[i+1][j-1] != 0 && tRoomLayout[i  ][j-1] != 0 && tRoomLayout[i-1][j  ] == 0 && tRoomLayout[i  ][j+1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
				if (tRoomLayout[i-1][j  ] != 0 && tRoomLayout[i-1][j+1] != 0 && tRoomLayout[i  ][j+1] != 0 && tRoomLayout[i+1][j  ] == 0 && tRoomLayout[i  ][j-1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
				if (tRoomLayout[i-1][j  ] != 0 && tRoomLayout[i-1][j-1] != 0 && tRoomLayout[i  ][j-1] != 0 && tRoomLayout[i+1][j  ] == 0 && tRoomLayout[i  ][j+1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
			}
		}
		temp = T;
		while (temp) {
			temp = F;
			for (int i = 1; i < tRoomLayout.length-1; i++) for (int j = 1; j < tRoomLayout[i].length-1; j++) if (tRoomLayout[i][j] == -128) {
				if (tRoomLayout[i+1][j  ] != 0 && tRoomLayout[i-1][j  ] != 0 && tRoomLayout[i  ][j-1] == 0 && tRoomLayout[i  ][j+1] == 0) continue;
				if (tRoomLayout[i+1][j  ] == 0 && tRoomLayout[i-1][j  ] == 0 && tRoomLayout[i  ][j-1] != 0 && tRoomLayout[i  ][j+1] != 0) continue;
				
				int tConnectionCount = 0;
				for (byte tSide : ALL_SIDES_HORIZONTAL) if (tRoomLayout[i+OFFSETS_X[tSide]][j+OFFSETS_Z[tSide]] != 0) tConnectionCount++;
				
				if (tConnectionCount <= 1) {tRoomLayout[i][j] = 0; temp = T; continue;}
				
				if (tRoomLayout[i+1][j+1] != 0) tConnectionCount++;
				if (tRoomLayout[i+1][j-1] != 0) tConnectionCount++;
				if (tRoomLayout[i-1][j+1] != 0) tConnectionCount++;
				if (tRoomLayout[i-1][j-1] != 0) tConnectionCount++;
				
				if (tConnectionCount >= 7) {tRoomLayout[i][j] = 0; temp = T; continue;}
				
				if (tConnectionCount == 5) {
					if (tRoomLayout[i+1][j-1] == 0 && tRoomLayout[i+1][j  ] == 0 && tRoomLayout[i+1][j+1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
					if (tRoomLayout[i-1][j-1] == 0 && tRoomLayout[i-1][j  ] == 0 && tRoomLayout[i-1][j+1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
					if (tRoomLayout[i-1][j+1] == 0 && tRoomLayout[i  ][j+1] == 0 && tRoomLayout[i+1][j+1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
					if (tRoomLayout[i-1][j-1] == 0 && tRoomLayout[i  ][j-1] == 0 && tRoomLayout[i+1][j-1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
				}
				
				if (tRoomLayout[i+1][j  ] != 0 && tRoomLayout[i+1][j+1] != 0 && tRoomLayout[i  ][j+1] != 0 && tRoomLayout[i-1][j  ] == 0 && tRoomLayout[i  ][j-1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
				if (tRoomLayout[i+1][j  ] != 0 && tRoomLayout[i+1][j-1] != 0 && tRoomLayout[i  ][j-1] != 0 && tRoomLayout[i-1][j  ] == 0 && tRoomLayout[i  ][j+1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
				if (tRoomLayout[i-1][j  ] != 0 && tRoomLayout[i-1][j+1] != 0 && tRoomLayout[i  ][j+1] != 0 && tRoomLayout[i+1][j  ] == 0 && tRoomLayout[i  ][j-1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
				if (tRoomLayout[i-1][j  ] != 0 && tRoomLayout[i-1][j-1] != 0 && tRoomLayout[i  ][j-1] != 0 && tRoomLayout[i+1][j  ] == 0 && tRoomLayout[i  ][j+1] == 0) {tRoomLayout[i][j] = 0; temp = T; continue;}
			}
		}
		
		for (int i = 1; i < tRoomLayout.length-1; i++) for (int j = 1; j < tRoomLayout[i].length-1; j++) if (tRoomLayout[i][j] > 0) {
			aWorld.getChunkFromChunkCoords((aMinX >> 4) + i, (aMinZ >> 4) + j).setChunkModified();
			
			int tConnectionCount = 0;
			for (byte tSide : ALL_SIDES_HORIZONTAL) if (tRoomLayout[i+OFFSETS_X[tSide]][j+OFFSETS_Z[tSide]] != 0) tConnectionCount++;
			
			
			DungeonData aData = new DungeonData(aWorld, aMinX+i*16, tOffsetY, aMinZ+j*16, this, tPrimaryBlock, tSecondaryBlock, tRegistry, tLightUpdateCoords, tKeyIDs, tKeyStacks, tGeneratedKeys, tRoomLayout, i, j, tConnectionCount, tColor, aRandom, tCoin);
			
			switch(tRoomLayout[i][j]) {
			case 1:
				if (ROOM_EMPTY.generate(aData)) {
					// Default Room, because not all the Stuff has been added yet.
					WorldgenDungeonInteriorDefault.generate(aData);
				}
				break;
			case 2:
				if (ROOM_EMPTY.generate(aData)) {
					switch(tConnectionCount) {
					case 1:
						// Piston Door + a Portal Room.
						DOOR_PISTON.generate(aData);
						if (aRandom.nextBoolean()) {
								 if (tDidntPortalEnd        && WorldgenDungeonInteriorPortalEnd       .generate(aData)) tDidntPortalEnd = F;
							else if (tDidntPortalMyst       && WorldgenDungeonInteriorPortalMyst      .generate(aData)) tDidntPortalMyst = F;
							else if (tDidntPortalTwilight   && WorldgenDungeonInteriorPortalTwilight  .generate(aData)) tDidntPortalTwilight = F;
							else if (tDidntPortalNether     && WorldgenDungeonInteriorPortalNether    .generate(aData)) tDidntPortalNether = F;
						} else {
								 if (tDidntPortalNether     && WorldgenDungeonInteriorPortalNether    .generate(aData)) tDidntPortalNether = F;
							else if (tDidntPortalTwilight   && WorldgenDungeonInteriorPortalTwilight  .generate(aData)) tDidntPortalTwilight = F;
							else if (tDidntPortalEnd        && WorldgenDungeonInteriorPortalEnd       .generate(aData)) tDidntPortalEnd = F;
							else if (tDidntPortalMyst       && WorldgenDungeonInteriorPortalMyst      .generate(aData)) tDidntPortalMyst = F;
						}
						break;
					case 2:
						// Pool containing Glowtus and Bonus Chests.
						WorldgenDungeonInteriorPool.generate(aData);
						break;
					case 3:
						// Empty Room
						break;
					default:
						// Empty Room
						break;
					}
				}
				break;
			case 3:
				if (ROOM_EMPTY.generate(aData)) {
					switch(tConnectionCount) {
					case 1:
						// Piston Door + Storage Room.
						DOOR_PISTON.generate(aData);
						WorldgenDungeonInteriorStorage.generate(aData);
						break;
					case 2:
						// Pool containing Glowtus and Bonus Chests.
						WorldgenDungeonInteriorPool.generate(aData);
						break;
					case 3:
						// Empty Room
						break;
					default:
						// Empty Room
						break;
					}
				}
				break;
			case 4:
				if (ROOM_EMPTY.generate(aData)) {
					// Library
					WorldgenDungeonInteriorLibrary.generate(aData);
				}
				break;
			case ROOM_ID_COUNT:
				List<IDungeonChunk> tList = new ArrayListNoNulls<>(ROOMS);
				while (T) {
					if (tList.remove(aRandom.nextInt(tList.size())).generate(aData)) break;
					if (tList.isEmpty()) {ROOM_EMPTY.generate(aData); break;}
				}
				break;
			}
			
			aWorld.getChunkFromChunkCoords((aMinX >> 4) + i, (aMinZ >> 4) + j).setChunkModified();
		}
		for (int i = 1; i < tRoomLayout.length-1; i++) for (int j = 1; j < tRoomLayout[i].length-1; j++) if (tRoomLayout[i][j] < 0) {
			aWorld.getChunkFromChunkCoords((aMinX >> 4) + i, (aMinZ >> 4) + j).setChunkModified();
			
			int tConnectionCount = 0;
			for (byte tSide : ALL_SIDES_HORIZONTAL) if (tRoomLayout[i+OFFSETS_X[tSide]][j+OFFSETS_Z[tSide]] != 0) tConnectionCount++;
			
			DungeonData aData = new DungeonData(aWorld, aMinX+i*16, tOffsetY, aMinZ+j*16, this, tPrimaryBlock, tSecondaryBlock, tRegistry, tLightUpdateCoords, tKeyIDs, tKeyStacks, tGeneratedKeys, tRoomLayout, i, j, tConnectionCount, tColor, aRandom, tCoin);
			
			switch(tRoomLayout[i][j]) {
			case -128:
				// Corridors, very important, to connect Stuff
				CORRIDOR.generate(aData);
				break;
			case -2:
				// Always have an Entrance
				ENTRANCE.generate(aData);
				break;
			case -1:
				// Always have a Sleeping Room
				if (ROOM_EMPTY.generate(aData)) {
					WorldgenDungeonInteriorBarracks.generate(aData);
				}
				break;
			}
			
			aWorld.getChunkFromChunkCoords((aMinX >> 4) + i, (aMinZ >> 4) + j).setChunkModified();
		}
		for (ChunkCoordinates tCoords : tLightUpdateCoords) {
			aWorld.setLightValue(EnumSkyBlock.Block, tCoords.posX, tCoords.posY, tCoords.posZ, 15);
			for (byte tSide : ALL_SIDES_MIDDLE) {
				aWorld.func_147451_t(tCoords.posX+OFFSETS_X[tSide], tCoords.posY+OFFSETS_Y[tSide], tCoords.posZ+OFFSETS_Z[tSide]);
				aWorld.markBlockForUpdate(tCoords.posX+OFFSETS_X[tSide], tCoords.posY+OFFSETS_Y[tSide], tCoords.posZ+OFFSETS_Z[tSide]);
			}
		}
		return T;
	}
	
	public static boolean setRandomBricks   (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, 3+aRandom.nextInt(3), 2);}
	public static boolean setStandardBrick  (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.BRICK, 2);}
	public static boolean setRedstoneBrick  (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.RSTBR, 3);}
	public static boolean setCrackedBrick   (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.CRACK, 2);}
	public static boolean setMossyBrick     (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.MBRIK, 2);}
	public static boolean setChiseledStone  (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.CHISL, 2);}
	public static boolean setStoneTiles     (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.TILES, 2);}
	public static boolean setSmallTiles     (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.STILE, 2);}
	public static boolean setSmallBricks    (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.SBRIK, 2);}
	public static boolean setSmoothBlock    (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aSecondary : aPrimary, BlockStones.SMOTH, 2);}
	public static boolean setAirBlock       (World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, NB, 0, 2);}
	
	public static boolean setRandomBricks   (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, 3+aRandom.nextInt(3), 2);}
	public static boolean setStandardBrick  (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.BRICK, 2);}
	public static boolean setRedstoneBrick  (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.RSTBR, 3);}
	public static boolean setCrackedBrick   (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.CRACK, 2);}
	public static boolean setMossyBrick     (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.MBRIK, 2);}
	public static boolean setChiseledStone  (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.CHISL, 2);}
	public static boolean setStoneTiles     (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.TILES, 2);}
	public static boolean setSmallTiles     (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.STILE, 2);}
	public static boolean setSmallBricks    (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.SBRIK, 2);}
	public static boolean setSmoothBlock    (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, aY == aData.mY+2 ? aData.mSecondary : aData.mPrimary, BlockStones.SMOTH, 2);}
	public static boolean setAirBlock       (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, NB, 0, 2);}
	
	public static boolean setGlass          (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, BlocksGT.Glass, aData.mColor, 2);}
	public static boolean setGlowGlass      (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, BlocksGT.GlowGlass, aData.mColor, 2);}
	public static boolean setColored        (World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {return aWorld.setBlock(aX, aY, aZ, BlocksGT.Concrete, aData.mColor, 2);}
	
	public static boolean setLampBlock(World aWorld, int aX, int aY, int aZ, DungeonData aData, Block aPrimary, Block aSecondary, Random aRandom, int aGenerateRedstoneBrick) {
		aData.mLightUpdateCoords.add(new ChunkCoordinates(aX, aY, aZ));
		if (aGenerateRedstoneBrick != 0) setRedstoneBrick(aWorld, aX, aY+aGenerateRedstoneBrick, aZ, aData, aRandom);
		aWorld.setBlock(aX, aY, aZ, aGenerateRedstoneBrick == 0 ? Blocks.redstone_lamp : Blocks.lit_redstone_lamp, 0, 2);
		return T;
	}
	
	public static boolean setLampBlock(World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom, int aGenerateRedstoneBrick) {
		aData.mLightUpdateCoords.add(new ChunkCoordinates(aX, aY, aZ));
		if (aGenerateRedstoneBrick != 0) setRedstoneBrick(aWorld, aX, aY+aGenerateRedstoneBrick, aZ, aData, aRandom);
		aWorld.setBlock(aX, aY, aZ, aGenerateRedstoneBrick == 0 ? Blocks.redstone_lamp : Blocks.lit_redstone_lamp, 0, 2);
		return T;
	}
	
	public static boolean setCoins(World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {
		for (int i = 0; i < 16; i++) aData.mCoin.setByte("gt.coin.stacksize."+i, (byte)(aRandom.nextInt(3) == 0 ? aRandom.nextInt(8) : 0));
		aData.mCoin.setByte("gt.coin.stacksize."+aRandom.nextInt(16), (byte)(1+aRandom.nextInt(8)));
		aData.mMTERegistryGT.mBlock.placeBlock(aWorld, aX, aY, aZ, SIDE_UNKNOWN, (short)32700, aData.mCoin, T, T);
		return T;
	}
	
	public static boolean setFlower(World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {
		int tIndex = aRandom.nextInt(BlocksGT.FLOWER_TILES.length);
		aWorld.setBlock(aX, aY, aZ, BlocksGT.FLOWER_TILES[tIndex], BlocksGT.FLOWER_METAS[tIndex], 2);
		return T;
	}
	
	public static boolean setFlowerPot(World aWorld, int aX, int aY, int aZ, DungeonData aData, Random aRandom) {
		int tIndex = aRandom.nextInt(BlocksGT.POT_FLOWER_TILES.length);
		aWorld.setBlock(aX, aY, aZ, Blocks.flower_pot, 0, 2);
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof TileEntityFlowerPot) ((TileEntityFlowerPot)tTileEntity).func_145964_a(Item.getItemFromBlock(BlocksGT.POT_FLOWER_TILES[tIndex]), BlocksGT.POT_FLOWER_METAS[tIndex]);
		return T;
	}
	
	public static boolean setBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMeta, int aFlags) {
		aWorld.setBlock(aX, aY, aZ, aBlock, aMeta, aFlags);
		return T;
	}
	
	public static boolean setBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMeta, int aFlags, int aRotationCount) {
		aWorld.setBlock(aX, aY, aZ, aBlock, aMeta, aFlags);
		while (aRotationCount-->0) aBlock.rotateBlock(aWorld, aX, aY, aZ, FORGE_DIR[SIDE_Y_POS]);
		return T;
	}
}
