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

import gregapi.data.LH;
import gregapi.item.multiitem.MultiItem;
import gregapi.item.multiitem.behaviors.IBehavior;
import gregapi.item.multiitem.behaviors.IBehavior.AbstractBehaviorDefault;
import gregapi.tileentity.ITileEntityKeyInteractable;
import gregapi.tileentity.delegate.DelegatorTileEntity;
import gregapi.util.UT;
import gregapi.util.WD;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Behavior_Key extends AbstractBehaviorDefault {
	public static final IBehavior<MultiItem> INSTANCE = new Behavior_Key();
	
	@Override
	public boolean onItemUseFirst(MultiItem aItem, ItemStack aStack, PlayerEntity aPlayer, World aWorld, int aX, int aY, int aZ, byte aSide, float hitX, float hitY, float hitZ) {
		if (aWorld.isRemote) return F;
		
		DelegatorTileEntity<TileEntity> aTileEntity = WD.te(aWorld, aX, aY, aZ, aSide, T);
		if (aTileEntity.mTileEntity instanceof ITileEntityKeyInteractable) {
			NBTTagCompound tNBT = aStack.getTagCompound();
			if (tNBT == null) tNBT = UT.NBT.make();
			if (!tNBT.hasKey(NBT_KEY)) tNBT.setLong(NBT_KEY, System.nanoTime());
			UT.NBT.set(aStack, tNBT);
			return ((ITileEntityKeyInteractable)aTileEntity.mTileEntity).useKey(aPlayer, aSide, hitX, hitY, hitZ, tNBT.getLong(NBT_KEY));
		}
		return F;
	}
	
	static {LH.add("gt.behaviour.key", "Can open certain regular Locks");}
	
	@Override
	public List<String> getAdditionalToolTips(MultiItem aItem, List<String> aList, ItemStack aStack) {
		aList.add(LH.get("gt.behaviour.key"));
		NBTTagCompound tNBT = aStack.getTagCompound();
		if (tNBT != null && tNBT.hasKey(NBT_KEY)) aList.add("Key ID: " + tNBT.getLong(NBT_KEY)); else aList.add("*BLANK*");
		return aList;
	}
}
