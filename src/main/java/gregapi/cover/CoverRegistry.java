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

package gregapi.cover;

import gregapi.code.ItemStackContainer;
import gregapi.code.ItemStackMap;
import gregapi.util.ST;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author Gregorius Techneticies
 */
public class CoverRegistry {
	public static ItemStackMap<ItemStackContainer, ICover> COVERS = new ItemStackMap<>();
	
	public static ICover get(short aID, short aMetaData) {
		return COVERS.get(aID, aMetaData);
	}
	
	public static ICover get(ItemStack aStack) {
		return aStack==null?null:get(ST.id(aStack), ST.meta_(aStack));
	}
	
	public static void put(ItemStackContainer aStack, ICover aCover) {
		if (aStack != null && ST.valid(aStack.toStack())) COVERS.put(aStack, aCover);
	}
	
	public static void put(ItemStack aStack, ICover aCover) {
		put(new ItemStackContainer(aStack), aCover);
	}
	
	public static void put(Item aItem, long aMetaData, ICover aCover) {
		put(new ItemStackContainer(aItem, 1, aMetaData), aCover);
	}
	
	public static void put(Block aBlock, long aMetaData, ICover aCover) {
		put(new ItemStackContainer(aBlock, 1, aMetaData), aCover);
	}
	
	public static CoverData coverdata(ITileEntityCoverable aTileEntity, CompoundNBT aNBT) {
		return aNBT == null ? new CoverData(aTileEntity) : new CoverData(aTileEntity, aNBT);
	}
}
