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

package gregapi.tileentity.computer;

import gregapi.tileentity.ITileEntityUnloadable;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author Gregorius Techneticies
 */
public interface ITileEntityUSBPort extends ITileEntityUnloadable {
	/**
	 * @return the Data Tag that is contained inside the "gt.usb.data" Tag of an USB Sticks CompoundNBT. So "tItemStackUSB.getTagCompound().getCompoundTag(NBT_USB_DATA)".
	 */
	public CompoundNBT getUSBData(byte aSide, int aUSBTier);
	
	/**
	 * sets the Data Tag that is contained inside the "gt.usb.data" Tag of an USB Sticks CompoundNBT.
	 * @return true if successful.
	 */
	public boolean setUSBData(byte aSide, int aUSBTier, CompoundNBT aData);
}
