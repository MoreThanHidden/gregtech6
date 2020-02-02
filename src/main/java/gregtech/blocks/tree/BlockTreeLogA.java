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

package gregtech.blocks.tree;

import static gregapi.data.CS.*;

import java.util.List;

import gregapi.block.IBlockToolable;
import gregapi.block.ToolCompat;
import gregapi.block.multitileentity.MultiTileEntityRegistry;
import gregapi.block.tree.BlockBaseLogFlammable;
import gregapi.data.CS.BlocksGT;
import gregapi.data.LH;
import gregapi.data.MT;
import gregapi.old.Textures;
import gregapi.util.OM;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockTreeLogA extends BlockBaseLogFlammable implements IBlockToolable {
	public BlockTreeLogA(String aUnlocalised) {
		super(null, aUnlocalised, Material.wood, soundTypeWood, 4, Textures.BlockIcons.LOGS_A);
		
		LH.add(getUnlocalizedName()+ ".0.name", "Rubber Log");
		LH.add(getUnlocalizedName()+ ".4.name", "Rubber Log");
		LH.add(getUnlocalizedName()+ ".8.name", "Rubber Log");
		LH.add(getUnlocalizedName()+".12.name", "Rubber Log");
		OM.reg(ST.make(this, 1, 0), "logRubber");
		OM.reg(ST.make(this, 1, 4), "logRubber");
		OM.reg(ST.make(this, 1, 8), "logRubber");
		OM.reg(ST.make(this, 1,12), "logRubber");
		
		LH.add(getUnlocalizedName()+ ".1.name", "Maple Log");
		LH.add(getUnlocalizedName()+ ".5.name", "Maple Log");
		LH.add(getUnlocalizedName()+ ".9.name", "Maple Log");
		LH.add(getUnlocalizedName()+".13.name", "Maple Log");
		OM.reg(ST.make(this, 1, 1), "logWood");
		OM.reg(ST.make(this, 1, 5), "logWood");
		OM.reg(ST.make(this, 1, 9), "logWood");
		OM.reg(ST.make(this, 1,13), "logWood");
		
		LH.add(getUnlocalizedName()+ ".2.name", "Willow Log");
		LH.add(getUnlocalizedName()+ ".6.name", "Willow Log");
		LH.add(getUnlocalizedName()+".10.name", "Willow Log");
		LH.add(getUnlocalizedName()+".14.name", "Willow Log");
		OM.reg(ST.make(this, 1, 2), "logWood");
		OM.reg(ST.make(this, 1, 6), "logWood");
		OM.reg(ST.make(this, 1,10), "logWood");
		OM.reg(ST.make(this, 1,14), "logWood");
		
		LH.add(getUnlocalizedName()+ ".3.name", "Blue Mahoe Log");
		LH.add(getUnlocalizedName()+ ".7.name", "Blue Mahoe Log");
		LH.add(getUnlocalizedName()+".11.name", "Blue Mahoe Log");
		LH.add(getUnlocalizedName()+".15.name", "Blue Mahoe Log");
		OM.reg(ST.make(this, 1, 3), "logWood");
		OM.reg(ST.make(this, 1, 7), "logWood");
		OM.reg(ST.make(this, 1,11), "logWood");
		OM.reg(ST.make(this, 1,15), "logWood");
	}
	
	@Override public int getLeavesRangeSide(byte aMetaData) {aMetaData &= 3; return aMetaData == 3 ? 3 : aMetaData+2;}
	@Override public int getLeavesRangeYPos(byte aMetaData) {return (aMetaData & 3) == 3 ? 4 : 2;}
	@Override public int getLeavesRangeYNeg(byte aMetaData) {return 0;}
	
	@Override
	public long onToolClick(String aTool, long aRemainingDurability, long aQuality, Entity aPlayer, List<String> aChatReturn, IInventory aPlayerInventory, boolean aSneaking, ItemStack aStack, World aWorld, byte aSide, int aX, int aY, int aZ, float aHitX, float aHitY, float aHitZ) {
		if (aTool.equals(TOOL_axe) || aTool.equals(TOOL_saw) || aTool.equals(TOOL_knife)) {
			if (aWorld.isRemote) return 0;
			aWorld.setBlock(aX, aY, aZ, BlocksGT.BeamA, aWorld.getBlockMetadata(aX, aY, aZ), 3);
			UT.Inventories.addStackToPlayerInventoryOrDrop(aPlayer instanceof PlayerEntity ? (PlayerEntity)aPlayer : null, OM.dust(MT.Bark), aWorld, aX+OFFSETS_X[aSide], aY+OFFSETS_Y[aSide], aZ+OFFSETS_Z[aSide]);
			return 1000;
		}
		if (SIDES_HORIZONTAL[aSide] && aTool.equals(TOOL_drill) && aWorld.getBlockMetadata(aX, aY, aZ) == 1) {
			if (aWorld.isRemote) return 0;
			MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry("gt.multitileentity");
			if (tRegistry != null) {
				tRegistry.mBlock.placeBlock(aWorld, aX, aY, aZ, SIDE_UNKNOWN, (short)32761, UT.NBT.make(NBT_FACING, aSide), T, T);
				return aTool.equals(TOOL_axe) ? 500 : 1000;
			}
		}
		return ToolCompat.onToolClick(this, aTool, aRemainingDurability, aQuality, aPlayer, aChatReturn, aPlayerInventory, aSneaking, aStack, aWorld, aSide, aX, aY, aZ, aHitX, aHitY, aHitZ);
	}
}
