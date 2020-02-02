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

package gregtech.items.behaviors;

import static gregapi.data.CS.*;

import java.util.List;

import gregapi.code.ArrayListNoNulls;
import gregapi.data.CS.SFX;
import gregapi.data.LH;
import gregapi.item.multiitem.MultiItem;
import gregapi.item.multiitem.behaviors.IBehavior;
import gregapi.item.multiitem.behaviors.IBehavior.AbstractBehaviorDefault;
import gregapi.tileentity.ITileEntityRemoteActivateable;
import gregapi.util.UT;
import gregapi.util.WD;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkPos;
import net.minecraft.world.World;

public class Behavior_Remote extends AbstractBehaviorDefault {
	public static final IBehavior<MultiItem> INSTANCE = new Behavior_Remote();
	
	@Override
	public boolean onItemUseFirst(MultiItem aItem, ItemStack aStack, PlayerEntity aPlayer, World aWorld, int aX, int aY, int aZ, byte aSide, float aHitX, float aHitY, float aHitZ) {
		if (aWorld.isRemote || aPlayer == null || !aPlayer.isSneaking() || !aPlayer.canPlayerEdit(aX, aY, aZ, aSide, aStack)) return F;
		if (!aStack.hasTagCompound()) aStack.setTagCompound(UT.NBT.make());
		ArrayListNoNulls<ChunkPos> tList = getCoordinateList(aStack.getTagCompound(), aWorld.provider.dimensionId);
		ChunkPos tCoords = new ChunkPos(aX, aY, aZ);
		if (tList.contains(tCoords)) {
			UT.Entities.chat(aPlayer, "Coordinates removed!");
			UT.Sounds.send(aWorld, SFX.IC_SCANNER, 1.0F, 1.0F, tCoords);
			tList.remove(tCoords);
		} else if (tList.size() >= 16) {
			UT.Entities.chat(aPlayer, "Cant hold more than 16 Coordinates per Dimension!");
		} else {
			TileEntity tTileEntity = WD.te(aWorld, tCoords, F);
			if (tTileEntity instanceof ITileEntityRemoteActivateable) {
				UT.Entities.chat(aPlayer, "Coordinates added!");
				UT.Sounds.send(aWorld, SFX.IC_SCANNER, 1.0F, 1.0F, tCoords);
				tList.add(tCoords);
			} else {
				UT.Entities.chat(aPlayer, "This cannot be added!");
			}
		}
		setCoordinateList(aStack.getTagCompound(), aWorld.provider.dimensionId, tList);
		if (aStack.getTagCompound().hasNoTags()) aStack.setTagCompound(null);
		return T;
	}
	
	@Override
	public ItemStack onItemRightClick(MultiItem aItem, ItemStack aStack, World aWorld, PlayerEntity aPlayer) {
		if (aWorld.isRemote || aPlayer.isSneaking() || !aStack.hasTagCompound()) return aStack;
		ArrayListNoNulls<ChunkPos> tToBeKept = new ArrayListNoNulls<>();
		for (ChunkPos tCoords : getCoordinateList(aStack.getTagCompound(), aWorld.provider.dimensionId)) {
			if (Math.abs(tCoords.posX - aPlayer.posX) <= 128 && Math.abs(tCoords.posY - aPlayer.posY) <= 128 && Math.abs(tCoords.posZ - aPlayer.posZ) <= 128) {
				TileEntity tTileEntity = WD.te(aWorld, tCoords, F);
				if (tTileEntity instanceof ITileEntityRemoteActivateable && ((ITileEntityRemoteActivateable)tTileEntity).remoteActivate()) tToBeKept.add(tCoords);
			} else {
				tToBeKept.add(tCoords);
			}
		}
		setCoordinateList(aStack.getTagCompound(), aWorld.provider.dimensionId, tToBeKept);
		UT.Sounds.send(aWorld, SFX.MC_CLICK, 1.0F, 1.0F, aPlayer);
		return aStack;
	}
	
	public ArrayListNoNulls<ChunkPos> getCoordinateList(CompoundNBT aNBT, int aDimension) {
		ArrayListNoNulls<ChunkPos> rList = new ArrayListNoNulls<>();
		if (aNBT == null) return rList;
		CompoundNBT tNBT = aNBT.getCompoundTag("gt.remote.dim."+aDimension);
		if (tNBT.hasNoTags()) return rList;
		int i = -1; while (++i < 16) {
			if (!tNBT.hasKey("x"+i)) break;
			rList.add(new ChunkPos(tNBT.getInteger("x"+i), tNBT.getInteger("y"+i), tNBT.getInteger("z"+i)));
		}
		return rList;
	}
	
	public void setCoordinateList(CompoundNBT aNBT, int aDimension, ArrayListNoNulls<ChunkPos> aList) {
		CompoundNBT tNBT = UT.NBT.make();
		for (int i = 0, j = aList.size(); i < j; i++) {
			ChunkPos tCoords = aList.get(i);
			tNBT.setInteger("x"+i, tCoords.posX);
			tNBT.setInteger("y"+i, tCoords.posY);
			tNBT.setInteger("z"+i, tCoords.posZ);
		}
		aNBT.setTag("gt.remote.dim."+aDimension, tNBT);
		if (tNBT.hasNoTags()) aNBT.removeTag("gt.remote.dim."+aDimension);
	}
	
	static {
		LH.add("gt.behaviour.remote", "Activates up to 16 Blocks within a Range of 128m");
	}
	
	@Override
	public List<String> getAdditionalToolTips(MultiItem aItem, List<String> aList, ItemStack aStack) {
		aList.add(LH.get("gt.behaviour.remote"));
		return aList;
	}
}
