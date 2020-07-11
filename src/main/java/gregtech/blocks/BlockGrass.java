/**
 * Copyright (c) 2020 GregTech-6 Team
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

package gregtech.blocks;

import static gregapi.data.CS.*;

import java.util.ArrayList;
import java.util.List;

import gregapi.block.BlockBaseMeta;
import gregapi.code.ArrayListNoNulls;
import gregapi.data.CS.BlocksGT;
import gregapi.data.LH;
import gregapi.data.RM;
import gregapi.old.Textures;
import gregapi.render.IconContainerCopied;
import gregapi.util.CR;
import gregapi.util.ST;
import gregapi.util.WD;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGrass extends BlockBaseMeta {
	public BlockGrass(String aUnlocalised) {
		super(null, aUnlocalised, Material.grass, soundTypeGrass, 4, Textures.BlockIcons.GRASSES_TOP);
		LH.add(getUnlocalizedName()+ ".0.name", "Grass");
		LH.add(getUnlocalizedName()+ ".1.name", "Grass");
		LH.add(getUnlocalizedName()+ ".2.name", "Grass");
		LH.add(getUnlocalizedName()+ ".3.name", "Grass");
		LH.add(getUnlocalizedName()+ ".4.name", "Grass");
		LH.add(getUnlocalizedName()+ ".5.name", "Grass");
		LH.add(getUnlocalizedName()+ ".6.name", "Grass");
		LH.add(getUnlocalizedName()+ ".7.name", "Grass");
		LH.add(getUnlocalizedName()+ ".8.name", "Grass");
		LH.add(getUnlocalizedName()+ ".9.name", "Grass");
		LH.add(getUnlocalizedName()+ ".10.name", "Grass");
		LH.add(getUnlocalizedName()+ ".11.name", "Grass");
		LH.add(getUnlocalizedName()+ ".12.name", "Grass");
		LH.add(getUnlocalizedName()+ ".13.name", "Grass");
		LH.add(getUnlocalizedName()+ ".14.name", "Grass");
		LH.add(getUnlocalizedName()+ ".15.name", "Grass");
		
		BlocksGT.harvestableSpade.add(this);
		BlocksGT.plantableGreens.add(this);
		BlocksGT.plantableTrees.add(this);
		BlocksGT.plantableGrass.add(this);
		
		RM.generify(ST.make(this, 1, W), ST.make(Blocks.GRASS, 1, 0));
		CR.shapeless(ST.make(Blocks.GRASS, 1, 0), new Object[] {this});
		
		CR.shapeless(ST.make(this, 8, 0), new Object[] {Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, DYE_OREDICTS[DYE_INDEX_Green    ], Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS});
		CR.shapeless(ST.make(this, 8, 1), new Object[] {Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, DYE_OREDICTS[DYE_INDEX_Lime     ], Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS});
		CR.shapeless(ST.make(this, 8, 2), new Object[] {Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, DYE_OREDICTS[DYE_INDEX_Black    ], Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS});
		CR.shapeless(ST.make(this, 8, 3), new Object[] {Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, DYE_OREDICTS[DYE_INDEX_LightGray], Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS, Blocks.GRASS});
	}
	
	static {
		LH.add("gt.grass.tooltip", "Does not spread, get eaten, change color or need light");
	}
	
	@Override
	public void addInformation(ItemStack aStack, int aMeta, PlayerEntity aPlayer, List<String> aList, boolean aF3_H) {
		aList.add(LH.Chat.CYAN + LH.get("gt.grass.tooltip"));
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection aSide, IPlantable aPlant) {
		EnumPlantType tType = aPlant.getPlantType(aWorld, aX+aSide.offsetX, aY+aSide.offsetY, aZ+aSide.offsetZ);
		return tType == EnumPlantType.Plains || (tType == EnumPlantType.Beach && (WD.anywater(aWorld, aX+1, aY, aZ) || WD.anywater(aWorld, aX-1, aY, aZ) || WD.anywater(aWorld, aX, aY, aZ+1) || WD.anywater(aWorld, aX, aY, aZ-1)));
	}
	
	public static final IconContainerCopied DIRT = new IconContainerCopied(Blocks.DIRT, 0, SIDE_BOTTOM);
	
	@Override public IIcon getIcon(int aSide, int aMeta) {return (SIDES_BOTTOM[aSide]?DIRT:(SIDES_TOP[aSide]?Textures.BlockIcons.GRASSES_TOP:Textures.BlockIcons.GRASSES_SIDE)[aMeta % 16]).getIcon(0);}
	@Override public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {return new ArrayListNoNulls<>(F, ST.make(Blocks.DIRT, 1, 0));}
	@Override public boolean doesPistonPush  (short aMeta) {return T;}
	@Override public boolean canCreatureSpawn(int   aMeta) {return F;}
	@Override public boolean isSealable      (int   aMeta, byte aSide) {return F;}
	@Override public String getHarvestTool   (int   aMeta) {return TOOL_shovel;}
	@Override public int getHarvestLevel     (int   aMeta) {return 0;}
	@Override public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {return Blocks.GRASS.getBlockHardness(aWorld, aX, aY, aZ);}
	@Override public float getExplosionResistance(int aMeta) {return Blocks.GRASS.getExplosionResistance(null);}
}
