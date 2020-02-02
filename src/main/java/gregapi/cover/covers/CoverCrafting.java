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

package gregapi.cover.covers;

import static gregapi.data.CS.*;

import gregapi.cover.CoverData;
import gregapi.render.ITexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntityMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

/**
 * @author Gregorius Techneticies
 */
public class CoverCrafting extends CoverTextureMulti {
	public CoverCrafting(ITexture... aTextures) {
		super(T, aTextures);
	}
	
	public CoverCrafting(String aFolder, int aAmount) {
		super(T, aFolder, aAmount);
	}
	
	@Override
	public boolean onCoverClickedRight(byte aSide, CoverData aData, Entity aPlayer, byte aSideClicked, float aHitX, float aHitY, float aHitZ) {
		if (aPlayer instanceof PlayerEntityMP) {
			((PlayerEntityMP)aPlayer).getNextWindowId();
			((PlayerEntityMP)aPlayer).playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(((PlayerEntityMP)aPlayer).currentWindowId, 1, "Crafting", 9, T));
			((PlayerEntityMP)aPlayer).openContainer = new ContainerWorkbench(((PlayerEntityMP)aPlayer).inventory, ((PlayerEntityMP)aPlayer).worldObj, aData.mTileEntity.getX(), aData.mTileEntity.getY(), aData.mTileEntity.getZ()) {@Override public boolean canInteractWith(PlayerEntity par1PlayerEntity) {return T;}};
			((PlayerEntityMP)aPlayer).openContainer.windowId = ((PlayerEntityMP)aPlayer).currentWindowId;
			((PlayerEntityMP)aPlayer).openContainer.addCraftingToCrafters(((PlayerEntityMP)aPlayer));
		}
		return T;
	}
	
	@Override public boolean isSealable(byte aCoverSide, CoverData aData) {return F;}
	@Override public boolean showsConnectorFront(byte aCoverSide, CoverData aData) {return F;}
}
