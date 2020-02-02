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

package gregtech.items.tools.early;

import static gregapi.data.CS.*;

import java.util.List;

import gregapi.code.ArrayListNoNulls;
import gregapi.data.CS.SFX;
import gregapi.data.MT;
import gregapi.data.OP;
import gregapi.data.RM;
import gregapi.item.multiitem.MultiItemTool;
import gregapi.item.multiitem.behaviors.Behavior_Tool;
import gregapi.item.multiitem.tools.ToolStats;
import gregapi.recipes.Recipe;
import gregapi.render.IIconContainer;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class GT_Tool_HardHammer extends ToolStats {
	public static final List<String> mEffectiveList = new ArrayListNoNulls<>(F
		, "EntityTowerGuardian".toLowerCase()
		, "EntityStoneSoldier".toLowerCase()
		, "EntityStone".toLowerCase()
		, "EntityGeonach".toLowerCase()
	);
	
	public static GT_Tool_HardHammer INSTANCE;
	
	public GT_Tool_HardHammer() {
		if (INSTANCE == null && getClass() == GT_Tool_HardHammer.class) INSTANCE = this;
	}
	
	@Override
	public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, PlayerEntity aPlayer) {
		String tName = UT.Reflection.getLowercaseClass(aEntity);
		return aEntity instanceof EntityIronGolem || mEffectiveList.contains(tName) || tName.contains("golem") ? aOriginalDamage*2 : aOriginalDamage;
	}
	
	@Override public int getToolDamagePerBlockBreak()                                       {return  25;}
	@Override public int getToolDamagePerDropConversion()                                   {return  50;}
	@Override public int getToolDamagePerContainerCraft()                                   {return 400;}
	@Override public int getToolDamagePerEntityAttack()                                     {return 200;}
	@Override public int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity) {return aOriginalHurtResistance * 2;}
	@Override public float getBaseDamage()                                                  {return 5.0F;}
	@Override public float getSpeedMultiplier()                                             {return 0.75F;}
	@Override public float getExhaustionPerAttack(Entity aEntity)                           {return 0.6F;}
	@Override public String getCraftingSound()                                              {return SFX.MC_ANVIL_LAND;}
	@Override public String getBreakingSound()                                              {return SFX.MC_ANVIL_BREAK;}
	@Override public boolean canBlock()                                                     {return T;}
	@Override public boolean isWeapon()                                                     {return T;}
	@Override public boolean isMiningTool()                                                 {return T;}
	
	@Override
	public boolean isMinableBlock(Block aBlock, byte aMetaData) {
		String tTool = aBlock.getHarvestTool(aMetaData);
		return (tTool != null && (tTool.equalsIgnoreCase(TOOL_hammer) || tTool.equalsIgnoreCase(TOOL_pickaxe))) || aBlock == Blocks.monster_egg || aBlock == Blocks.mob_spawner || aBlock.getMaterial() == Material.rock || aBlock.getMaterial() == Material.glass || aBlock.getMaterial() == Material.ice || aBlock.getMaterial() == Material.packedIce || RM.Hammer.containsInput(ST.make(aBlock, 1, aMetaData), null, NI);
	}
	
	@Override
	public float getMiningSpeed(Block aBlock, byte aMetaData, float aDefault, PlayerEntity aPlayer, World aWorld, int aX, int aY, int aZ) {
		return aBlock == Blocks.mob_spawner ? aDefault * 16 : aDefault;
	}
	
	@Override
	public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, PlayerEntity aPlayer, Block aBlock, long aAvailableDurability, int aX, int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
		int rConversions = 0;
		Recipe tRecipe;
		if (aBlock.hasTileEntity(aMetaData) || null == (tRecipe = RM.Hammer.findRecipe(null, null, T, Integer.MAX_VALUE, null, ZL_FS, ST.make(aBlock, 1, aMetaData)))) {
			List<ItemStack> tDrops = new ArrayListNoNulls<>();
			for (int i = 0; i < aDrops.size(); i++) {
				tRecipe = RM.Hammer.findRecipe(null, null, T, Integer.MAX_VALUE, null, ZL_FS, ST.amount(1, aDrops.get(i)));
				if (tRecipe != null) {
					byte tStackSize = (byte)aDrops.get(i).getCount();
					rConversions += tStackSize;
					aDrops.remove(i--);
					if (tRecipe.mOutputs.length > 0) for (byte j = 0; j < tStackSize; j++) {
						ItemStack[] tHammeringOutput = tRecipe.getOutputs();
						for (int k = 0; k < tHammeringOutput.length; k++) if (tHammeringOutput[k] != null) tDrops.add(tHammeringOutput[k]);
					}
				}
			}
			aDrops.addAll(tDrops);
		} else {
			aDrops.clear();
			ItemStack[] tHammeringOutput = tRecipe.getOutputs(RNGSUS);
			for (int k = 0; k < tHammeringOutput.length; k++) if (tHammeringOutput[k] != null) aDrops.add(tHammeringOutput[k]);
			rConversions++;
		}
		return rConversions;
	}
	
	@Override
	public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
		return aIsToolHead ? MultiItemTool.getPrimaryMaterial(aStack, MT.Steel).mTextureSetsItems.get(OP.toolHeadHammer.mIconIndexItem) : MultiItemTool.getSecondaryMaterial(aStack, MT.Wood).mTextureSetsItems.get(OP.stick.mIconIndexItem);
	}
	
	@Override
	public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
		return aIsToolHead ? MultiItemTool.getPrimaryMaterial(aStack, MT.Steel).mRGBaSolid : MultiItemTool.getSecondaryMaterial(aStack, MT.Wood).mRGBaSolid;
	}
	
	@Override
	public void onStatsAddedToTool(MultiItemTool aItem, int aID) {
		aItem.addItemBehavior(aID, new Behavior_Tool(TOOL_hammer        , SFX.MC_ANVIL_LAND, 100, T));
		aItem.addItemBehavior(aID, new Behavior_Tool(TOOL_prospector    , SFX.MC_ANVIL_USE ,  10, T));
	}
	
	@Override
	public String getDeathMessage() {
		return "[VICTIM] was squashed by [KILLER]";
	}
}
