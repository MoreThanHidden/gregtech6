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

package gregtech.entities;

import static gregapi.data.CS.*;

import java.util.Collection;
import java.util.List;

import gregapi.data.IL;
import gregapi.data.MD;
import gregapi.data.MT;
import gregapi.data.OP;
import gregapi.util.OM;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;

public class Override_Drops {
	public static void handleDrops(LivingEntity aDead, String aClass, Collection<ItemEntity> aDrops, int aLooting, boolean aBurn, boolean aPlayerKill) {
		if (UT.Code.stringInvalid(aClass) || "EntityTFLichMinion".equalsIgnoreCase(aClass) || "EntitySkeletonBoss".equalsIgnoreCase(aClass)) return;
		final boolean aSpace = aClass.startsWith("entityevolved") || aClass.startsWith("entityalien");
		boolean tReplaceIron = aClass.startsWith("entitygaia");
		
		int tRandomNumber = RNGSUS.nextInt(Math.max(36, 144-aLooting*3)), tIntestinesAmount = 0;
		
		if ("ZombieFarmer".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			ItemStack tGrass = UT.Code.select(IL.Grass, IL.Grass, IL.Grass_Dry, IL.Grass_Moldy, IL.Grass_Rotten).get(1);
			
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, RNGSUS.nextBoolean()?ST.copy(tGrass):OP.rockGt.mat(MT.Stone, 1)));
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, RNGSUS.nextBoolean()?ST.copy(tGrass):ST.make(Items.STICK, 1, 0)));
			if (RNGSUS.nextInt( 5) == 0) aDrops.add(ST.entity(aDead, RNGSUS.nextBoolean()?ST.copy(tGrass):IL.Mud_Ball.get(1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, RNGSUS.nextBoolean()?ST.copy(tGrass):IL.Tool_Matches.get(1)));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(OP.toolHeadHoe.mat(MT.Bronze, 1)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, OP.toolHeadSpade.mat(MT.Bronze, 1)
			, OP.toolHeadSpade.mat(MT.Bronze, 1)
			, OP.toolHeadSpade.mat(MT.Bronze, 1)
			, OP.toolHeadSpade.mat(MT.Bronze, 1)
			, OP.toolHeadHoe.mat(MT.Bronze, 1)
			, OP.toolHeadHoe.mat(MT.Bronze, 1)
			, OP.toolHeadAxe.mat(MT.Bronze, 1)
			, OP.toolHeadPlow.mat(MT.Bronze, 1)
			, OP.toolHeadSense.mat(MT.Bronze, 1)
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Veggie_6.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Fruit_2.get(1+RNGSUS.nextInt(2))
			)));
			}
			
			}
		} else if ("ZombieMiner".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, RNGSUS.nextBoolean()?OP.rockGt.mat(MT.Stone, 1):ST.make(Items.FLINT, 1, 0)));
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, ST.make(Items.STICK, 1, 0)));
			if (RNGSUS.nextInt( 5) == 0) aDrops.add(ST.entity(aDead, OP.rockGt.mat(RNGSUS.nextBoolean()?MT.Ag:MT.Au, 1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1)));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(OP.toolHeadPickaxe.mat(MT.Bronze, 1)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, OP.toolHeadShovel.mat(MT.Bronze, 1)
			, OP.toolHeadShovel.mat(MT.Bronze, 1)
			, OP.toolHeadShovel.mat(MT.Bronze, 1)
			, OP.toolHeadShovel.mat(MT.Bronze, 1)
			, OP.toolHeadPickaxe.mat(MT.Bronze, 1)
			, OP.toolHeadPickaxe.mat(MT.Bronze, 1)
			, OP.toolHeadChisel.mat(MT.Bronze, 1)
			, OP.toolHeadHammer.mat(MT.Bronze, 1)
			, OP.toolHeadFile.mat(MT.Bronze, 1)
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Meat_5.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Fish_4.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Chum_4.get(1+RNGSUS.nextInt(2))
			, IL.Dynamite.get(1+RNGSUS.nextInt(2))
			)));
			}
			
			}
		} else if ("ZombieSoldier".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, ST.make(Items.FLINT, 1, 0)));
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, ST.make(Items.STICK, 1, 0)));
			if (RNGSUS.nextInt( 5) == 0) aDrops.add(ST.entity(aDead, IL.Mud_Ball.get(1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, OP.bulletGtSmall.mat(MT.Steel, 1+RNGSUS.nextInt(2))));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(ST.make(Items.NAME_TAG, 1, 0)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_Lighter_Plastic_Broken.get(1)
			, IL.Tool_Lighter_Plastic_Broken.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Invar_Empty.get(1)
			, IL.Tool_Lighter_Invar_Full.get(1)
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Rotten_1.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Bread_3.get(1+RNGSUS.nextInt(2))
			, IL.Compass_North.get(1)
			, IL.Pill_Iodine.get(1)
			, IL.Duct_Tape.get(1, IL.Tool_MatchBox_Full.get(1))
			)));
			}
			
			}
		} else if ("ZombiePigmanSoldier".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			@SuppressWarnings("rawtypes")
			List tList = aDead.world.getEntitiesWithinAABBExcludingEntity(aDead, aDead.getBoundingBox().expand(32, 32, 32));
			for (int i = 0; i < tList.size(); i++) if (tList.get(i) instanceof PlayerEntity) {for (int j = 0; j < tList.size(); j++) if (tList.get(j) instanceof ZombiePigmanEntity) ((ZombiePigmanEntity)tList.get(j)).attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity)tList.get(i)), 0); break;}
			
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, RNGSUS.nextBoolean()?OP.rockGt.mat(MT.Netherrack, 1):ST.make(Items.FLINT, 1, 0)));
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, Items.BONE, 1, 0));
			if (RNGSUS.nextInt( 5) == 0) aDrops.add(ST.entity(aDead, OP.rockGt.mat(MT.Au, 1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, OP.bulletGtMedium.mat(MT.Steel, 1+RNGSUS.nextInt(2))));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(ST.make(Items.NAME_TAG, 1, 0)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_Lighter_Plastic_Broken.get(1)
			, IL.Tool_Lighter_Plastic_Broken.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Invar_Empty.get(1)
			, IL.Tool_Lighter_Invar_Full.get(1)
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(3))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Rotten_1.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Bread_3.get(1+RNGSUS.nextInt(2))
			, IL.Compass_North.get(1)
			, IL.Pill_Iodine.get(1)
			, IL.Duct_Tape.get(1, IL.Tool_MatchBox_Full.get(1))
			)));
			}
			
			}
		} else if ("SkeletonSoldier".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, Items.BONE, 1, 0));
			if (RNGSUS.nextInt(20) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, OP.bulletGtSmall.mat(MT.Steel, 1+RNGSUS.nextInt(2))));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(ST.make(Items.NAME_TAG, 1, 0)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_Lighter_Plastic_Broken.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Bottle_Milk.get(1+RNGSUS.nextInt(3))
			, IL.Bottle_Milk.get(1+RNGSUS.nextInt(3))
			, IL.Bottle_Milk.get(1+RNGSUS.nextInt(3))
			, IL.Bottle_Milk.get(1+RNGSUS.nextInt(3))
			, IL.Bottle_Milk.get(1+RNGSUS.nextInt(3))
			, IL.Bottle_Milk.get(1+RNGSUS.nextInt(3))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Rotten_1.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Bread_3.get(1+RNGSUS.nextInt(2))
			, IL.Compass_North.get(1)
			, IL.Pill_Iodine.get(1)
			, IL.Duct_Tape.get(1, IL.Tool_MatchBox_Full.get(1))
			)));
			}
			
			}
		} else if ("Bandit".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, ST.make(Items.FLINT, 1, 0)));
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, ST.make(Items.STICK, 1, 0)));
			if (RNGSUS.nextInt(20) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, OP.bulletGtSmall.mat(MT.Steel, 1+RNGSUS.nextInt(2))));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(ST.make(Items.NAME_TAG, 1, 0)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_Lighter_Plastic_Broken.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Fruit_2.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Bread_3.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Fish_4.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Meat_5.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Veggie_6.get(1+RNGSUS.nextInt(2))
			, IL.Compass_North.get(1)
			, IL.Pill_Iodine.get(1)
			, IL.Duct_Tape.get(1, IL.Tool_MatchBox_Full.get(1))
			)));
			}
			
			}
		} else if ("ArmySoldier".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt(20) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, OP.bulletGtMedium.mat(MT.Steel, 1+RNGSUS.nextInt(2))));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(ST.make(Items.NAME_TAG, 1, 0)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_MatchBox_Full.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Invar_Full.get(1)
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Fruit_2.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Bread_3.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Fish_4.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Meat_5.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Veggie_6.get(1+RNGSUS.nextInt(3))
			, IL.Compass_North.get(1)
			, IL.Pill_Iodine.get(1)
			, IL.Duct_Tape.get(1, IL.Tool_MatchBox_Full.get(1))
			)));
			}
			
			}
		} else if ("Commando".equalsIgnoreCase(aClass) || "StormTrooper".equalsIgnoreCase(aClass) || "Outcast".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt(20) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, OP.bulletGtLarge.mat(MT.Steel, 1)));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(ST.make(Items.NAME_TAG, 1, 0)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Lighter_Invar_Full.get(1)
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(2))
			, IL.Food_Can_Fruit_2.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Bread_3.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Fish_4.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Meat_5.get(1+RNGSUS.nextInt(3))
			, IL.Food_Can_Veggie_6.get(1+RNGSUS.nextInt(3))
			, IL.Compass_North.get(1)
			, IL.Pill_Iodine.get(1)
			, IL.Duct_Tape.get(1, IL.Tool_Lighter_Plastic_Full.get(1))
			)));
			}
			
			}
		} else if ("DictatorDave".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, OP.bulletGtSmall.mat(MT.Steel, 1+RNGSUS.nextInt(8))));
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, IL.KEYS_FANCY[RNGSUS.nextInt(IL.KEYS_FANCY.length)].getWithName(1, "Fancy Useless Key")));
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, IL.Compass_North.get(1)));
			if (RNGSUS.nextInt( 4) == 0) aDrops.add(ST.entity(aDead, (RNGSUS.nextBoolean()?IL.Tool_Lighter_Platinum_Empty:IL.Tool_Lighter_Platinum_Full).get(1)));
			}
		} else if ("PsychoSteve".equalsIgnoreCase(aClass)) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, ST.make(Items.FLINT, 1, 0)));
			if (RNGSUS.nextInt( 3) == 0) aDrops.add(ST.entity(aDead, ST.make(Items.STICK, 1, 0)));
			if (RNGSUS.nextInt( 5) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1+RNGSUS.nextInt(4))));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, IL.Dynamite.get(1+RNGSUS.nextInt(4))));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(ST.make(Items.NAME_TAG, 1, 0)
			, IL.KEYS_CHEAP[RNGSUS.nextInt(IL.KEYS_CHEAP.length)].getWithName(1, "Random Useless Key")
			, IL.Tool_Lighter_Plastic_Full.get(1)
			, IL.Tool_Remote_Activator.get(1)
			, IL.Food_Ice_Cream_Bear.get(1+RNGSUS.nextInt(4))
			, IL.Food_Can_Chum_4.get(1+RNGSUS.nextInt(4))
			, IL.Bottle_Beer.get(1+RNGSUS.nextInt(6))
			, IL.Pill_Red.get(1+RNGSUS.nextInt(4))
			, IL.Pill_Blue.get(1+RNGSUS.nextInt(4))
			, IL.Compass_Death.get(1)
			)));
			}
			
			}
		} else if ("CyberDemon".equalsIgnoreCase(aClass)) {// TODO
			tReplaceIron = T;
		} else if ("SuperMutantBasic".equalsIgnoreCase(aClass)) {// TODO
			tReplaceIron = T;
		} else if ("SuperMutantHeavy".equalsIgnoreCase(aClass)) {// TODO
			tReplaceIron = T;
		} else if ("SuperMutantElite".equalsIgnoreCase(aClass)) {// TODO
			tReplaceIron = T;
		} else if (aDead instanceof ZombiePigmanEntity) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, RNGSUS.nextBoolean()?OP.rockGt.mat(MT.Netherrack, 1):ST.make(Items.FLINT, 1, 0)));
			if (RNGSUS.nextInt( 5) == 0) aDrops.add(ST.entity(aDead, Items.BONE, 1, 0));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, OP.rockGt.mat(MT.Au, 1)));
			if (RNGSUS.nextInt(20) == 0) aDrops.add(ST.entity(aDead, IL.Tool_Matches.get(1)));
			}
		} else if (aDead instanceof ZombieEntity) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			if (RNGSUS.nextInt( 2) == 0) aDrops.add(ST.entity(aDead, RNGSUS.nextBoolean()?OP.rockGt.mat(aSpace?MT.SpaceRock:MT.Stone, 1):aSpace?OP.rockGt.mat(MT.MeteoricIron, 1):ST.make(Items.FLINT, 1, 0)));
			if (RNGSUS.nextInt( 5) == 0) aDrops.add(ST.entity(aDead, aSpace?OP.scrapGt.mat(MT.Plastic, 1):ST.make(Items.STICK, 1, 0)));
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, aSpace?OP.stick.mat(MT.Plastic, 1):IL.Mud_Ball.get(1)));
			if (RNGSUS.nextInt(20) == 0) aDrops.add(ST.entity(aDead, aSpace?OP.nugget.mat(MT.MeteoricIron, 1):IL.Tool_Matches.get(1)));
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(IL.Food_Pomeraisins
			, IL.Food_Raisins_Green
			, IL.Food_Raisins_Purple
			, IL.Food_Raisins_White
			, IL.Food_Raisins_Red
			, IL.Food_Pomeraisins
			).get(1)));
			}
			
			if (aDead instanceof ZombieVillagerEntity) for (int i = 0, j = 1+RNGSUS.nextInt(3); i < j; i++) switch(RNGSUS.nextInt(20)) {
			case  0: aDrops.add(ST.entity(aDead, ST.book("Manual_Hunting_Creeper")));   break;
			case  1: aDrops.add(ST.entity(aDead, ST.book("Manual_Hunting_Skeleton")));  break;
			case  2: aDrops.add(ST.entity(aDead, ST.book("Manual_Hunting_Zombie")));    break;
			case  3: aDrops.add(ST.entity(aDead, ST.book("Manual_Hunting_Spider")));    break;
			case  4: aDrops.add(ST.entity(aDead, ST.book("Manual_Hunting_End")));       break;
			case  5: aDrops.add(ST.entity(aDead, ST.book("Manual_Hunting_Blaze")));     break;
			case  6: aDrops.add(ST.entity(aDead, ST.book("Manual_Hunting_Witch")));     break;
			case  7: aDrops.add(ST.entity(aDead, ST.book("Manual_Elements")));          break;
			case  8: aDrops.add(ST.entity(aDead, ST.book("Manual_Alloys")));            break;
			case  9: aDrops.add(ST.entity(aDead, ST.book("Manual_Smeltery")));          break;
			case 10: aDrops.add(ST.entity(aDead, ST.book("Manual_Extenders")));         break;
			case 11: aDrops.add(ST.entity(aDead, ST.book("Manual_Printer")));           break;
			case 12: aDrops.add(ST.entity(aDead, ST.book("Manual_Steam")));             break;
			case 13: aDrops.add(ST.entity(aDead, ST.book("Manual_Random")));            break;
			default: aDrops.add(ST.entity(aDead, ST.book(UT.Books.MATERIAL_DICTIONARIES.get(RNGSUS.nextInt(UT.Books.MATERIAL_DICTIONARIES.size()))))); break;
			}
			}
		} else if (aDead instanceof SpiderEntity) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(IL.Food_Cookie_Chocolate_Raisins
			, IL.Food_Cookie_Raisins
			, IL.Food_Cookie_Chocolate_Raisins
			).getWithName(1, aSpace?"Space Spider Cookie":"Spider Cookie")));
			}
			
			}
		} else if (aDead instanceof SkeletonEntity) {
			tReplaceIron = T;
			
			if (aPlayerKill) {
			
			if (tRandomNumber == 0) {
			aDrops.add(ST.entity(aDead, UT.Code.select(IL.Bottle_Milk.get(1)
			, IL.Bottle_Milk.get(1)
			, IL.Bottle_Milk.get(1)
			, IL.Bottle_Milk.get(1)
			, ST.update(aSpace?OP.arrowGtPlastic.mat(MT.MeteoricIron, 1):OP.arrowGtWood.mat(MT.Bronze, 1))
			, ST.update(aSpace?OP.arrowGtPlastic.mat(MT.MeteoricIron, 1):OP.arrowGtWood.mat(MT.Bronze, 1))
			, ST.update(aSpace?OP.arrowGtPlastic.mat(MT.MeteoricSteel, 1):OP.arrowGtWood.mat(MT.Bronze, 1))
			, ST.update(aSpace?OP.arrowGtPlastic.mat(MT.MeteoricSteel, 1):OP.arrowGtWood.mat(MT.DamascusSteel, 1))
			)));
			}
			
			}
		} else if (aClass.equalsIgnoreCase("EntityTFWraith")) {
			tReplaceIron = T;
			if (RNGSUS.nextInt(10) == 0) aDrops.add(ST.entity(aDead, OP.dust.mat(MT.Ectoplasm, 1)));
		} else if (aClass.equalsIgnoreCase("MoCEntityBoar")) {
			tReplaceIron = T;
			int tAmount = 1+RNGSUS.nextInt(3);
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
			while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?Items.COOKED_PORKCHOP:Items.PORKCHOP, 1, 0));
		} else if (aClass.equalsIgnoreCase("MoCEntityDeer")) {
			tReplaceIron = T;
			ItemStack tRaw      = IL.TF_Venison_Raw     .get(1, ST.make(MD.HaC, "venisonrawItem"    , 1, 0));
			ItemStack tCooked   = IL.TF_Venison_Cooked  .get(1, ST.make(MD.HaC, "venisoncookedItem" , 1, 0));
			if (tRaw != null && tCooked != null) {
				int tAmount = 1+RNGSUS.nextInt(3);
				if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
				while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?tCooked:tRaw));
			}
		} else if (aClass.equalsIgnoreCase("MoCEntityOstrich")) {
			tReplaceIron = T;
			int tAmount = 1+RNGSUS.nextInt(3);
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
			while (tAmount-->0) aDrops.add(ST.entity(aDead, Items.FEATHER, 1, 0));
		} else if (aClass.equalsIgnoreCase("MoCEntityHorse")) {
			tReplaceIron = T;
			int tAmount = 2+RNGSUS.nextInt(3);
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
			while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_Horse_Cooked.get(1):IL.Food_Horse_Raw.get(1)));
		} else if (aClass.equalsIgnoreCase("EntityTFQuestRam")) {
			tReplaceIron = T;
			int tAmount = 24+RNGSUS.nextInt(8);
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting*8+1);
			while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_Mutton_Cooked.get(1):IL.Food_Mutton_Raw.get(1)));
		} else if (aClass.equalsIgnoreCase("EntityWarg") || aClass.equalsIgnoreCase("MoCEntityWWolf") || aClass.equalsIgnoreCase("EntityTFMistWolf") || aClass.equalsIgnoreCase("EntityTFWinterWolf")) {
			tReplaceIron = T;
			int tAmount = 1+RNGSUS.nextInt(4);
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
			while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_DogMeat_Cooked.get(1):IL.Food_DogMeat_Raw.get(1)));
		} else if (aDead instanceof HorseEntity && !aDead.isChild()) {
			tReplaceIron = T;
			int tAmount = 1+RNGSUS.nextInt(3);
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
			if (RNGSUS.nextInt(Math.max(1, 10-(int)(((HorseEntity)aDead).getHorseJumpStrength()*10.0))) == 0) tAmount += 1+RNGSUS.nextInt(aLooting + 1)/2;
			if (RNGSUS.nextInt(Math.max(1, 30-(int)(aDead.getMaxHealth()))) == 0) tAmount += 1+RNGSUS.nextInt(aLooting + 1)/2;
			switch(((HorseEntity)aDead).type) {
			case 0: while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_Horse_Cooked.get(1):IL.Food_Horse_Raw.get(1))); break;
			case 1: while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_Donkey_Cooked.get(1):IL.Food_Donkey_Raw.get(1))); break;
			case 2: while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_Mule_Cooked.get(1):IL.Food_Mule_Raw.get(1))); break;
			case 3: while (tAmount-->0) aDrops.add(ST.entity(aDead, ST.make(Items.ROTTEN_FLESH, 1, 0))); break;
			case 4: while (tAmount-->0) aDrops.add(ST.entity(aDead, ST.make(Items.BONE, 1, 0))); break;
			}
		} else if (aDead instanceof WolfEntity && !aDead.isChild()) {
			tReplaceIron = T;
			int tAmount = RNGSUS.nextInt(3);
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
			while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_DogMeat_Cooked.get(1):IL.Food_DogMeat_Raw.get(1)));
		} else if (aClass.equalsIgnoreCase("EntityAerbunny")) {
			tReplaceIron = T;
		} else if (aClass.equalsIgnoreCase("EntitySheepuff")) {
			tReplaceIron = T;
			int tAmount = RNGSUS.nextInt(3);
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
			while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_Mutton_Cooked.get(1):IL.Food_Mutton_Raw.get(1)));
		} else if (aDead instanceof SheepEntity && !aDead.isChild()) {
			tReplaceIron = T;
			if (!MD.EtFu.mLoaded && !MD.GaSu.mLoaded) {
			int tAmount = RNGSUS.nextInt(3);
			if (MD.HaC.mLoaded) tAmount--;
			if (aLooting > 0) tAmount += RNGSUS.nextInt(aLooting + 1);
			while (tAmount-->0) aDrops.add(ST.entity(aDead, aBurn?IL.Food_Mutton_Cooked.get(1):IL.Food_Mutton_Raw.get(1)));
			}
		}
		
		for (ItemEntity tEntity : aDrops) {ItemStack tStack = tEntity.getItem(); if (ST.valid(tStack)) {
			if (tReplaceIron) {
				if (OM.is("ingotIron", tStack)) {
					ST.set(tStack, OP.ingot.mat(MT.Pb, 1), F, F);
				} else
				if (OM.is("chunkGtIron", tStack)) {
					ST.set(tStack, OP.chunkGt.mat(MT.Pb, 1), F, F);
				} else
				if (OM.is("nuggetIron", tStack)) {
					ST.set(tStack, OP.nugget.mat(MT.Pb, 1), F, F);
				}
			}
			
			if (!OM.is("listAllmeatsubstitute", tStack)) {
				if (RNGSUS.nextInt(3) == 0 && (OM.is("listAllmeatraw", tStack) || OM.is("listAllmeatcooked", tStack))) tIntestinesAmount++;
				if (tStack.getItem() == Items.PORKCHOP) {
					switch(tRandomNumber%3) {
					case 0: ST.set(tStack, (aBurn?IL.Food_Ham_Cooked:IL.Food_Ham_Raw).get(1), F, F); break;
					case 1: ST.set(tStack, (aBurn?IL.Food_Bacon_Cooked:IL.Food_Bacon_Raw).get(UT.Code.bindStack(tStack.getCount() * (3+RNGSUS.nextInt(3)))), T, F); break;
					}
				} else
				if (tStack.getItem() == Items.COOKED_PORKCHOP) {
					switch(tRandomNumber%3) {
					case 0: ST.set(tStack, IL.Food_Ham_Cooked.get(1), F, F); break;
					case 1: ST.set(tStack, IL.Food_Bacon_Cooked.get(UT.Code.bindStack(tStack.getCount() * (3+RNGSUS.nextInt(3)))), T, F); break;
					}
				} else
				if (OM.is("listAllbeefraw", tStack)) {
					switch(tRandomNumber%3) {
					case 0: ST.set(tStack, (aBurn?IL.Food_Rib_Cooked:IL.Food_Rib_Raw).get(1), F, F); break;
					case 1: ST.set(tStack, (aBurn?IL.Food_RibEyeSteak_Cooked:IL.Food_RibEyeSteak_Raw).get(1), F, F); break;
					}
				} else
				if (OM.is("listAllbeefcooked", tStack)) {
					switch(tRandomNumber%3) {
					case 0: ST.set(tStack, IL.Food_Rib_Cooked.get(1), F, F); break;
					case 1: ST.set(tStack, IL.Food_RibEyeSteak_Cooked.get(1), F, F); break;
					}
				} else
				if (OM.is("listAllhorseraw", tStack) || OM.is("listAllvenisonraw", tStack)) {
					switch(tRandomNumber%2) {
					case 0: ST.set(tStack, (aBurn?IL.Food_Rib_Cooked:IL.Food_Rib_Raw).get(1), F, F); break;
					}
				} else
				if (OM.is("listAllhorsecooked", tStack) || OM.is("listAllvenisoncooked", tStack)) {
					switch(tRandomNumber%2) {
					case 0: ST.set(tStack, IL.Food_Rib_Cooked.get(1), F, F); break;
					}
				}
			}
			
			tEntity.setItem(tStack);
			tRandomNumber++;
		}}
		
		while (tIntestinesAmount-->0) aDrops.add(ST.entity(aDead, IL.Food_Scrap_Meat.get(1)));
	}
}
