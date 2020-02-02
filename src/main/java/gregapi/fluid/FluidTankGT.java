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

package gregapi.fluid;

import static gregapi.data.CS.*;

import gregapi.data.FL;
import gregapi.util.UT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class FluidTankGT implements IFluidTank {
	private FluidStack mFluid;
	private long mCapacity, mAmount = 0;
	private boolean mPreventDraining = F, mVoidExcess = F, mChangedFluids = F;
	public final FluidTankGT[] AS_ARRAY = new FluidTankGT[] {this};
	
	public FluidTankGT(FluidStack aFluid) {mFluid = aFluid; if (aFluid != null) {mCapacity = aFluid.getAmount(); mAmount = aFluid.getAmount();}}
	public FluidTankGT(FluidStack aFluid, long aCapacity) {mFluid = aFluid; mCapacity = aCapacity; mAmount = (aFluid == null ? 0 : aFluid.getAmount());}
	public FluidTankGT(long aCapacity) {mCapacity = aCapacity;}
	public FluidTankGT(Fluid aFluid, long aAmount) {this(FL.make(aFluid, aAmount)); mAmount = aAmount;}
	public FluidTankGT(Fluid aFluid, long aAmount, long aCapacity) {this(FL.make(aFluid, aAmount), aCapacity); mAmount = aAmount;}
	public FluidTankGT(CompoundNBT aNBT, long aCapacity) {mCapacity = aCapacity; if (aNBT != null && !aNBT.hasNoTags()) {mFluid = FL.load_(aNBT); mAmount = (isEmpty() ? 0 : aNBT.hasKey("LAmount") ? aNBT.getLong("LAmount") : mFluid.getAmount());}}
	
	public FluidTankGT readFromNBT(CompoundNBT aNBT, String aKey) {
		if (aNBT.hasKey(aKey)) {
			aNBT = aNBT.getCompoundTag(aKey);
			if (aNBT != null && !aNBT.hasNoTags()) {
				mFluid = FL.load_(aNBT);
				mAmount = (isEmpty() ? 0 : aNBT.hasKey("LAmount") ? aNBT.getLong("LAmount") : mFluid.getAmount());
			}
		}
		return this;
	}
	
	public CompoundNBT writeToNBT(CompoundNBT aNBT, String aKey) {
		if (mFluid != null && (mPreventDraining || mAmount > 0)) {
			CompoundNBT tNBT = UT.NBT.make();
			mFluid.getAmount() = UT.Code.bindInt(mAmount);
			aNBT.setTag(aKey, mFluid.writeToNBT(tNBT));
			if (mAmount > Integer.MAX_VALUE) tNBT.setLong("LAmount", mAmount);
		} else {
			aNBT.removeTag(aKey);
		}
		return aNBT;
	}
	
	public CompoundNBT writeToNBT(String aKey) {
		CompoundNBT aNBT = UT.NBT.make();
		if (mFluid != null && (mPreventDraining || mAmount > 0)) {
			CompoundNBT tNBT = UT.NBT.make();
			mFluid.getAmount() = UT.Code.bindInt(mAmount);
			aNBT.setTag(aKey, mFluid.writeToNBT(tNBT));
			if (mAmount > Integer.MAX_VALUE) tNBT.setLong("LAmount", mAmount);
		} else {
			aNBT.removeTag(aKey);
		}
		return aNBT;
	}
	
	public static CompoundNBT writeToNBT(String aKey, FluidStack aFluid) {
		CompoundNBT rNBT = UT.NBT.make();
		if (aFluid != null && aFluid.getAmount() > 0) {
			rNBT.setTag(aKey, aFluid.writeToNBT(UT.NBT.make()));
		}
		return rNBT;
	}
	
	public static CompoundNBT writeToNBT(CompoundNBT aNBT, String aKey, FluidStack aFluid) {
		if (aFluid != null && aFluid.getAmount() > 0) {
			aNBT.setTag(aKey, aFluid.writeToNBT(UT.NBT.make()));
		} else {
			aNBT.removeTag(aKey);
		}
		return aNBT;
	}
	
	public FluidStack drain(int aDrained) {return drain(aDrained, T);}
	@Override
	public FluidStack drain(int aDrained, boolean aDoDrain) {
		if (isEmpty() || aDrained <= 0) return null;
		if (mAmount < aDrained) aDrained = (int)mAmount;
		FluidStack rFluid = new FluidStack(mFluid, aDrained);
		if (aDoDrain) {
			mAmount -= aDrained;
			if (mAmount <= 0) {
				if (!mPreventDraining) {
					mFluid = null;
					mChangedFluids = T;
				}
				mAmount = 0;
			}
		}
		return rFluid;
	}
	
	public boolean drainAll(long aDrained) {
		if (isEmpty() || mAmount < aDrained) return F;
		mAmount -= aDrained;
		if (mAmount <= 0) {
			if (!mPreventDraining) {
				mFluid = null;
				mChangedFluids = T;
			}
			mAmount = 0;
		}
		return T;
	}
	
	public long remove(long aDrained) {
		if (isEmpty() || mAmount <= 0 || aDrained <= 0) return 0;
		if (mAmount < aDrained) aDrained = mAmount;
		mAmount -= aDrained;
		if (mAmount <= 0) {
			if (!mPreventDraining) {
				mFluid = null;
				mChangedFluids = T;
			}
			mAmount = 0;
		}
		return aDrained;
	}
	
	public long add(long aFilled) {
		if (isEmpty() || aFilled <= 0) return 0;
		if (mAmount + aFilled > mCapacity) {
			if (!mVoidExcess) aFilled = mCapacity - mAmount;
			mAmount = mCapacity;
			return aFilled;
		}
		mAmount += aFilled;
		return aFilled;
	}
	
	public int fill(FluidStack aFluid) {return fill(aFluid, T);}
	@Override
	public int fill(FluidStack aFluid, boolean aDoFill) {
		if (aFluid == null) return 0;
		if (aDoFill) {
			if (isEmpty()) {
				mFluid = aFluid.copy();
				mChangedFluids = T;
				mAmount = Math.min(mCapacity, aFluid.getAmount());
				return mVoidExcess ? aFluid.getAmount() : (int)mAmount;
			}
			if (!contains(aFluid)) return 0;
			long tFilled = mCapacity - mAmount;
			if (aFluid.getAmount() < tFilled) {
				mAmount += aFluid.getAmount();
				tFilled = aFluid.getAmount();
			} else mAmount = mCapacity;
			return mVoidExcess ? aFluid.getAmount() : (int)tFilled;
		}
		return UT.Code.bindInt(isEmpty() ? mVoidExcess ? aFluid.getAmount() : Math.min(mCapacity, aFluid.getAmount()) : contains(aFluid) ? mVoidExcess ? aFluid.getAmount() : Math.min(mCapacity - mAmount, aFluid.getAmount()) : 0);
	}
	
	public boolean canFillAll(FluidStack aFluid) {return aFluid == null || aFluid.getAmount() <= 0 || (isEmpty() ? mVoidExcess || aFluid.getAmount() <= mCapacity : contains(aFluid) && (mVoidExcess || mAmount + aFluid.getAmount() <= mCapacity));}
	public boolean canFillAll(long aAmount) {return aAmount <= 0 || mVoidExcess || mAmount + aAmount <= mCapacity;}
	
	public boolean fillAll(FluidStack aFluid) {
		if (aFluid == null || aFluid.getAmount() <= 0) return T;
		if (isEmpty()) {
			if (aFluid.getAmount() <= mCapacity || mVoidExcess) {
				mFluid = aFluid.copy();
				mChangedFluids = T;
				mAmount = aFluid.getAmount();
				if (mAmount > mCapacity) mAmount = mCapacity;
				return T;
			}
			return F;
		}
		if (contains(aFluid)) {
			if (mAmount + aFluid.getAmount() <= mCapacity) {
				mAmount += aFluid.getAmount();
				return T;
			}
			if (mVoidExcess) {
				mAmount = mCapacity;
				return T;
			}
		}
		return F;
	}
	
	public boolean fillAll(FluidStack aFluid, int aMultiplier) {
		if (aMultiplier <= 0) return T;
		if (aMultiplier == 1) return fillAll(aFluid);
		if (aFluid == null || aFluid.getAmount() <= 0) return T;
		if (isEmpty()) {
			if (aFluid.getAmount() * aMultiplier <= mCapacity || mVoidExcess) {
				mFluid = aFluid.copy();
				mChangedFluids = T;
				mAmount = aFluid.getAmount() * aMultiplier;
				if (mAmount > mCapacity) mAmount = mCapacity;
				return T;
			}
			return F;
		}
		if (contains(aFluid)) {
			if (mAmount + aFluid.getAmount() * aMultiplier <= mCapacity) {
				mAmount += aFluid.getAmount() * aMultiplier;
				return T;
			}
			if (mVoidExcess) {
				mAmount = mCapacity;
				return T;
			}
		}
		return F;
	}
	
	public FluidTankGT setEmpty() {mFluid = null; mChangedFluids = T; mAmount = 0; return this;}
	public FluidTankGT setFluid(FluidStack aFluid) {mFluid = aFluid; mChangedFluids = T; mAmount = (aFluid == null ? 0 : aFluid.getAmount()); return this;}
	public FluidTankGT setFluid(FluidStack aFluid, long aAmount) {mFluid = aFluid; mChangedFluids = T; mAmount = (aFluid == null ? 0 : aAmount); return this;}
	public FluidTankGT setCapacity(long aCapacity) {mCapacity = UT.Code.bindInt(aCapacity); return this;}
	public FluidTankGT setPreventDraining() {return setPreventDraining(T);}
	public FluidTankGT setPreventDraining(boolean aPrevent) {mPreventDraining = aPrevent; return this;}
	public FluidTankGT setVoidExcess() {return setVoidExcess(T);}
	public FluidTankGT setVoidExcess(boolean aVoidExcess) {mVoidExcess = aVoidExcess; return this;}
	
	public boolean isEmpty() {return mFluid == null;}
	public boolean isFull() {return mFluid != null && mAmount     >= mCapacity;}
	public boolean isHalf() {return mFluid != null && mAmount * 2 >= mCapacity;}
	
	public boolean contains(Fluid aFluid) {return mFluid != null && mFluid.getFluid() == aFluid;}
	public boolean contains(FluidStack aFluid) {return FL.equal(mFluid, aFluid);}
	
	public boolean has(long aAmount) {return mAmount >= aAmount;}
	public boolean has() {return mAmount > 0;}
	
	public boolean check() {if (mChangedFluids) {mChangedFluids = F; return T;} return F;}
	public boolean update() {return mChangedFluids = T;}
	public boolean changed() {return mChangedFluids;}
	
	public long amount() {return isEmpty() ? 0 : mAmount;}
	public long amount(long aMax) {return isEmpty() || aMax <= 0 ? 0 : mAmount < aMax ? mAmount : aMax;}
	
	public long capacity() {return mCapacity;}
	
	public String name() {return mFluid == null ? null : mFluid.getFluid().getName();}
	public String name(boolean aLocalised) {return FL.name(mFluid, aLocalised);}
	
	public String content() {return content("Empty");}
	public String content(String aEmptyMessage) {return mFluid == null ? aEmptyMessage : amount() + " L of " + name(T) + " (" + (FL.gas(mFluid) ? "Gaseous" : "Liquid") + ")";}
	public String contentcap() {return mFluid == null ? "Capacity: " + mCapacity + " L" : amount() + " L of " + name(T) + " (" + (FL.gas(mFluid) ? "Gaseous" : "Liquid") + "); Max: "+mCapacity+" L)";}
	
	public Fluid fluid() {return mFluid == null ? null : mFluid.getFluid();}
	
	public FluidStack get() {return mFluid;}
	public FluidStack get(long aMax) {return isEmpty() || aMax <= 0 ? null : new FluidStack(mFluid, UT.Code.bindInt(mAmount < aMax ? mAmount : aMax));}
	
	@Override public FluidStack getFluid() {if (mFluid != null) mFluid.getAmount() = UT.Code.bindInt(mAmount); return mFluid;}
	@Override public int getFluidAmount() {return UT.Code.bindInt(mAmount);}
	@Override public int getCapacity() {return UT.Code.bindInt(mCapacity);}
	@Override public FluidTankInfo getInfo() {return new FluidTankInfo(isEmpty() ? null : mFluid.copy(), UT.Code.bindInt(mCapacity));}
}
