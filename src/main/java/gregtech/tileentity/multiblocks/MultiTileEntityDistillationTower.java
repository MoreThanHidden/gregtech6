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

package gregtech.tileentity.multiblocks;

import static gregapi.data.CS.*;

import java.util.List;

import gregapi.data.FL;
import gregapi.data.LH;
import gregapi.data.LH.Chat;
import gregapi.data.TD;
import gregapi.tileentity.delegate.DelegatorTileEntity;
import gregapi.tileentity.energy.ITileEntityEnergy;
import gregapi.tileentity.machines.ITileEntityAdjacentOnOff;
import gregapi.tileentity.multiblocks.ITileEntityMultiBlockController;
import gregapi.tileentity.multiblocks.MultiTileEntityMultiBlockPart;
import gregapi.tileentity.multiblocks.TileEntityBase10MultiBlockMachine;
import gregapi.util.ST;
import gregapi.util.WD;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

/**
 * @author Gregorius Techneticies
 */
public class MultiTileEntityDistillationTower extends TileEntityBase10MultiBlockMachine {
	@Override
	public boolean checkStructure2() {
		int tX = getOffsetXN(mFacing), tY = yCoord, tZ = getOffsetZN(mFacing);
		if (worldObj.blockExists(tX-1, tY, tZ-1) && worldObj.blockExists(tX+1, tY, tZ-1) && worldObj.blockExists(tX-1, tY, tZ+1) && worldObj.blockExists(tX+1, tY, tZ+1)) {
			boolean tSuccess = T;
			
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY-1, tZ-1, 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY-1, tZ-1, 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY-1, tZ-1, 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY-1, tZ  , 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY-1, tZ  , 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY-1, tZ  , 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY-1, tZ+1, 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY-1, tZ+1, 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY-1, tZ+1, 18101, getMultiTileEntityRegistryID(), 0, MultiTileEntityMultiBlockPart.ONLY_ENERGY_IN)) tSuccess = F;
			
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY  , tZ-1, 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY  , tZ-1, 18102, getMultiTileEntityRegistryID(),  mFacing == SIDE_Z_POS ? 1 : 0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY  , tZ-1, 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY  , tZ  , 18102, getMultiTileEntityRegistryID(),  mFacing == SIDE_X_POS ? 1 : 0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY  , tZ  , 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY  , tZ  , 18102, getMultiTileEntityRegistryID(),  mFacing == SIDE_X_NEG ? 1 : 0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY  , tZ+1, 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY  , tZ+1, 18102, getMultiTileEntityRegistryID(),  mFacing == SIDE_Z_NEG ? 1 : 0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY  , tZ+1, 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_ITEM_FLUID)) tSuccess = F;
			
			for (int i = 1; i < 8; i++) {
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY+i, tZ-1, 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY+i, tZ-1, 18102, getMultiTileEntityRegistryID(),  mFacing == SIDE_Z_POS ? 1 : 0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY+i, tZ-1, 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY+i, tZ  , 18102, getMultiTileEntityRegistryID(),  mFacing == SIDE_X_POS ? 1 : 0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY+i, tZ  , 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY+i, tZ  , 18102, getMultiTileEntityRegistryID(),  mFacing == SIDE_X_NEG ? 1 : 0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX-1, tY+i, tZ+1, 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX  , tY+i, tZ+1, 18102, getMultiTileEntityRegistryID(),  mFacing == SIDE_Z_NEG ? 1 : 0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			if (!ITileEntityMultiBlockController.Util.checkAndSetTarget(this, tX+1, tY+i, tZ+1, 18102, getMultiTileEntityRegistryID(),                              0, MultiTileEntityMultiBlockPart.ONLY_FLUID_OUT)) tSuccess = F;
			}
			
			return tSuccess;
		}
		return mStructureOkay;
	}
	
	static {
		LH.add("gt.tooltip.multiblock.distillationtower.1", "3x3 Base of Heat Transmitters");
		LH.add("gt.tooltip.multiblock.distillationtower.2", "3x3x8 of Distillation Tower Parts");
		LH.add("gt.tooltip.multiblock.distillationtower.3", "Main centered on Side-Bottom of Tower facing outwards");
		LH.add("gt.tooltip.multiblock.distillationtower.4", "Outputs automatically to the Holes on the Backside");
		LH.add("gt.tooltip.multiblock.distillationtower.5", "Bottom Hole is for outputting all Items");
		LH.add("gt.tooltip.multiblock.distillationtower.6", "Input only possible at Bottom Layer of Tower");
	}
	
	@Override
	public void addToolTips(List<String> aList, ItemStack aStack, boolean aF3_H) {
		aList.add(Chat.CYAN     + LH.get(LH.STRUCTURE) + ":");
		aList.add(Chat.WHITE    + LH.get("gt.tooltip.multiblock.distillationtower.1"));
		aList.add(Chat.WHITE    + LH.get("gt.tooltip.multiblock.distillationtower.2"));
		aList.add(Chat.WHITE    + LH.get("gt.tooltip.multiblock.distillationtower.3"));
		aList.add(Chat.WHITE    + LH.get("gt.tooltip.multiblock.distillationtower.4"));
		aList.add(Chat.WHITE    + LH.get("gt.tooltip.multiblock.distillationtower.5"));
		aList.add(Chat.WHITE    + LH.get("gt.tooltip.multiblock.distillationtower.6"));
		super.addToolTips(aList, aStack, aF3_H);
	}
	
	@Override
	public void addToolTipsSided(List<String> aList, ItemStack aStack, boolean aF3_H) {
		String tSideNames = ""; boolean temp = F;
		if (mEnergyTypeAccepted != TD.Energy.TU) {
		for (byte tSide : ALL_SIDES_VALID) if (FACE_CONNECTED[tSide][mEnergyInputs])    {tSideNames += (temp?", ":"")+LH.get(LH.FACES[tSide]); temp = T;}
		LH.addEnergyToolTips(this, aList, mEnergyTypeAccepted, null, tSideNames, null);
		}
	}
	
	@Override
	public boolean isInsideStructure(int aX, int aY, int aZ) {
		int tX = getOffsetXN(mFacing), tY = yCoord, tZ = getOffsetZN(mFacing);
		return aX >= tX - 1 && aY >= tY - 1 && aZ >= tZ - 1 && aX <= tX + 1 && aY <= tY + 7 && aZ <= tZ + 1;
	}
	
	@Override
	public void updateAdjacentToggleableEnergySources() {
		int tX = getOffsetXN(mFacing) - 1, tZ = getOffsetZN(mFacing) - 1;
		for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) {
			DelegatorTileEntity<TileEntity> tDelegator = WD.te(worldObj, tX+i, yCoord-2, tZ+j, SIDE_TOP, F);
			if (tDelegator.mTileEntity instanceof ITileEntityAdjacentOnOff && tDelegator.mTileEntity instanceof ITileEntityEnergy && ((ITileEntityEnergy)tDelegator.mTileEntity).isEnergyEmittingTo(mEnergyTypeAccepted, tDelegator.mSideOfTileEntity, T)) {
				((ITileEntityAdjacentOnOff)tDelegator.mTileEntity).setAdjacentOnOff(getStateOnOff());
			}
		}
	}
	
	@Override
	public void doOutputItems() {
		ST.moveAll(delegator(FACING_TO_SIDE[mFacing][mItemAutoOutput]), WD.te(worldObj, getOffsetXN(mFacing, 3), yCoord, getOffsetZN(mFacing, 3), mFacing, F));
	}
	
	@Override
	public void doOutputFluids() {
		for (IFluidTank tTank : mTanksOutput) {
			FluidStack tFluid = tTank.getFluid();
			if (tFluid != null && tFluid.getAmount() > 0) {
				DelegatorTileEntity<TileEntity> tDelegator = null;
				if (FL.is(tFluid, "propane", "methane")) {
					tDelegator = WD.te(worldObj, getOffsetXN(mFacing, 3), yCoord+7, getOffsetZN(mFacing, 3), mFacing, F);
				} else if (FL.is(tFluid, "butane")) {
					tDelegator = WD.te(worldObj, getOffsetXN(mFacing, 3), yCoord+6, getOffsetZN(mFacing, 3), mFacing, F);
				} else if (FL.is(tFluid, "petrol", "gasoline", "bioethanol")) {
					tDelegator = WD.te(worldObj, getOffsetXN(mFacing, 3), yCoord+5, getOffsetZN(mFacing, 3), mFacing, F);
				} else if (FL.is(tFluid, "kerosene", "kerosine", "glycerol")) {
					tDelegator = WD.te(worldObj, getOffsetXN(mFacing, 3), yCoord+4, getOffsetZN(mFacing, 3), mFacing, F);
				} else if (FL.is(tFluid, "diesel", "biodiesel")) {
					tDelegator = WD.te(worldObj, getOffsetXN(mFacing, 3), yCoord+3, getOffsetZN(mFacing, 3), mFacing, F);
				} else if (FL.is(tFluid, "fuel", "fueloil", "biofuel")) {
					tDelegator = WD.te(worldObj, getOffsetXN(mFacing, 3), yCoord+2, getOffsetZN(mFacing, 3), mFacing, F);
				} else {
					tDelegator = WD.te(worldObj, getOffsetXN(mFacing, 3), yCoord+1, getOffsetZN(mFacing, 3), mFacing, F);
				}
				
				if (FL.move(tTank, tDelegator) > 0) updateInventory();
			}
		}
	}
	
	@Override public DelegatorTileEntity<IInventory> getItemInputTarget(byte aSide) {return null;}
	@Override public DelegatorTileEntity<TileEntity> getItemOutputTarget(byte aSide) {return null;}
	@Override public DelegatorTileEntity<IFluidHandler> getFluidInputTarget(byte aSide) {return null;}
	@Override public DelegatorTileEntity<IFluidHandler> getFluidOutputTarget(byte aSide, Fluid aOutput) {return null;}
	
	@Override public String getTileEntityName() {return "gt.multitileentity.multiblock.distillationtower";}
}
