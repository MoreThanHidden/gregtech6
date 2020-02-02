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

package gregtech;

import static gregapi.data.CS.*;
import static gregapi.util.CR.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import gregapi.api.Abstract_Mod;
import gregapi.api.Abstract_Proxy;
import gregapi.block.prefixblock.PrefixBlockItem;
import gregapi.code.ArrayListNoNulls;
import gregapi.code.IItemContainer;
import gregapi.code.ItemStackContainer;
import gregapi.code.TagData;
import gregapi.compat.CompatMods;
import gregapi.data.ANY;
import gregapi.data.CS.FluidsGT;
import gregapi.data.CS.ItemsGT;
import gregapi.data.CS.ModIDs;
import gregapi.data.FL;
import gregapi.data.IL;
import gregapi.data.MD;
import gregapi.data.MT;
import gregapi.data.OD;
import gregapi.data.OP;
import gregapi.data.RM;
import gregapi.data.TD;
import gregapi.item.multiitem.MultiItem;
import gregapi.item.multiitem.MultiItemRandom;
import gregapi.item.multiitem.behaviors.Behavior_Turn_Into;
import gregapi.item.multiitem.behaviors.IBehavior;
import gregapi.item.prefixitem.PrefixItem;
import gregapi.network.NetworkHandler;
import gregapi.oredict.OreDictManager;
import gregapi.oredict.OreDictMaterial;
import gregapi.oredict.OreDictMaterialStack;
import gregapi.oredict.configurations.IOreDictConfigurationComponent;
import gregapi.recipes.Recipe;
import gregapi.recipes.maps.RecipeMapReplicator;
import gregapi.util.CR;
import gregapi.util.OM;
import gregapi.util.ST;
import gregapi.util.UT;
import gregtech.compat.*;
import gregtech.loaders.a.*;
import gregtech.loaders.b.Loader_Books;
import gregtech.loaders.b.Loader_Fuels;
import gregtech.loaders.b.Loader_ItemIterator;
import gregtech.loaders.b.Loader_Late_Items_And_Blocks;
import gregtech.loaders.b.Loader_MultiTileEntities;
import gregtech.loaders.b.Loader_OreProcessing;
import gregtech.loaders.b.Loader_Worldgen;
import gregtech.loaders.c.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Gregorius Techneticies
 */
@Mod(ModIDs.GT)
public class GT6_Main extends Abstract_Mod {
	public static GT_Proxy gt_proxy = DistExecutor.runForDist(() -> GT_Client::new,() -> GT_Server::new);
	
	public GT6_Main() {
		GT = this;
		NW_GT = new NetworkHandler(MD.GT.mID, "GREG");
	}

	@Override
	public void onModCommonSetup(FMLCommonSetupEvent aEvent) {
		try {
			//OUT.println(getModNameForLog() + ": Sorting GregTech to the end of the Mod List for further processing.");
			//TODO: Move Gregtech to the end of the mod list

			// LoadController tLoadController = ((LoadController)UT.Reflection.getFieldContent(Loader.instance(), "modController", T, T));
			//List<ModContainer> tModList = tLoadController.getActiveModList(), tNewModsList = new ArrayList<>(tModList.size());
			//ModContainer tGregTech = null;
			//for (short i = 0; i < tModList.size(); i++) {
			//	ModContainer tMod = tModList.get(i);
			//	if (tMod.getModId().equalsIgnoreCase(MD.GT.mID)) tGregTech = tMod; else tNewModsList.add(tMod);
			//}
			//if (tGregTech != null) tNewModsList.add(tGregTech);
			//UT.Reflection.getField(tLoadController, "activeModList", T, T).set(tLoadController, tNewModsList);
		} catch(Throwable e) {
			if (D1) e.printStackTrace(ERR);
		}

		//TODO: New Config System
		//File tFile = new File(DirectoriesGT.CONFIG_GT, "GregTech.cfg");
		//if (!tFile.exists()) tFile = new File(DirectoriesGT.CONFIG_GT, "gregtech.cfg");
		//Configuration tMainConfig = new Configuration(tFile);

		//gt_proxy.mSkeletonsShootGTArrows = tMainConfig.get("general", "SkeletonsShootGTArrows", 16).getInt(16);
		//gt_proxy.mFlintChance            = tMainConfig.get("general", "FlintAndSteelChance"   , 30).getInt(30);
		//gt_proxy.mDisableVanillaOres     = tMainConfig.get("general", "DisableVanillaOres"    , T).getBoolean(T);
		//mDisableIC2Ores                  = tMainConfig.get("general", "DisableIC2Ores"        , T).getBoolean(T);

		//if (tMainConfig.get("general", "IncreaseDungeonLoot", T).getBoolean(T)) {
			//TODO: Loot Tables
			//OUT.println(getModNameForLog() + ": Increasing general amount of Loot in Dungeon Chests and alike");
			//ChestGenHooks tChest;
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST                ); tChest.setMax(tChest.getMax()+ 8); tChest.setMin(tChest.getMin()+ 4);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST              ); tChest.setMax(tChest.getMax()+12); tChest.setMin(tChest.getMin()+ 6);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST       ); tChest.setMax(tChest.getMax()+ 8); tChest.setMin(tChest.getMin()+ 4);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST       ); tChest.setMax(tChest.getMax()+16); tChest.setMin(tChest.getMin()+ 8);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER   ); tChest.setMax(tChest.getMax()+ 2); tChest.setMin(tChest.getMin()+ 1);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR         ); tChest.setMax(tChest.getMax()+ 4); tChest.setMin(tChest.getMin()+ 2);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH         ); tChest.setMax(tChest.getMax()+12); tChest.setMin(tChest.getMin()+ 6);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING        ); tChest.setMax(tChest.getMax()+ 8); tChest.setMin(tChest.getMin()+ 4);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR        ); tChest.setMax(tChest.getMax()+ 6); tChest.setMin(tChest.getMin()+ 3);
			//tChest = ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY         ); tChest.setMax(tChest.getMax()+16); tChest.setMin(tChest.getMin()+ 8);
		//}
		//if (tMainConfig.get("general", "SmallerVanillaToolDurability", T).getBoolean(T)) {
			OUT.println(getModNameForLog() + ": Nerfing Vanilla Tool Durability");
			Items.WOODEN_SWORD   .maxDamage = 4;
			Items.WOODEN_PICKAXE .maxDamage = 4;
			Items.WOODEN_SHOVEL  .maxDamage = 4;
			Items.WOODEN_AXE     .maxDamage = 4;
			Items.WOODEN_HOE     .maxDamage = 4;
			Items.STONE_SWORD    .maxDamage = 16;
			Items.STONE_PICKAXE  .maxDamage = 16;
			Items.STONE_SHOVEL   .maxDamage = 16;
			Items.STONE_AXE      .maxDamage = 16;
			Items.STONE_HOE      .maxDamage = 16;
			Items.IRON_SWORD     .maxDamage = 80;
			Items.IRON_PICKAXE   .maxDamage = 80;
			Items.IRON_SHOVEL    .maxDamage = 80;
			Items.IRON_AXE       .maxDamage = 80;
			Items.IRON_HOE       .maxDamage = 80;
			Items.GOLDEN_SWORD   .maxDamage = 8;
			Items.GOLDEN_PICKAXE .maxDamage = 8;
			Items.GOLDEN_SHOVEL  .maxDamage = 8;
			Items.GOLDEN_AXE     .maxDamage = 8;
			Items.GOLDEN_HOE     .maxDamage = 8;
			Items.DIAMOND_SWORD  .maxDamage = 240;
			Items.DIAMOND_PICKAXE.maxDamage = 240;
			Items.DIAMOND_SHOVEL .maxDamage = 240;
			Items.DIAMOND_AXE    .maxDamage = 240;
			Items.DIAMOND_HOE    .maxDamage = 240;
		//}


		//BlockOcean.SPREAD_TO_AIR = tMainConfig.get("general", "OceanBlocksSpreadToAir", T).getBoolean(T);

		//tMainConfig.save();

		if (COMPAT_IC2 != null && !MD.IC2C.mLoaded) {
			OUT.println(getModNameForLog() + ": Removing all original Scrapbox Drops.");
			try {
				UT.Reflection.getField("ic2.core.item.ItemScrapbox$Drop", "topChance", T, T).set(null, 0);
				((List<?>)UT.Reflection.getFieldContent(UT.Reflection.getFieldContent("ic2.api.recipe.Recipes", "scrapboxDrops", T, T), "drops", T, T)).clear();
			} catch(Throwable e) {
				if (D1) e.printStackTrace(ERR);
			}

			OUT.println(getModNameForLog() + ": Adding Scrap with a Weight of 200.0F to the Scrapbox Drops.");
			COMPAT_IC2.scrapbox(200.0F, IL.IC2_Scrap.get(1));
		}

		//TODO: Register Arrow Entity
		//EntityRegistry.registerModEntity(EntityArrow_Material.class , "GT_Entity_Arrow"         , 1, GT, 160, 1, T);
		//EntityRegistry.registerModEntity(EntityArrow_Potion.class   , "GT_Entity_Arrow_Potion"  , 2, GT, 160, 1, T);

		for (OreDictMaterial tWood : ANY.Wood.mToThis) OP.plate.disableItemGeneration(tWood);
		OP.ingot        .disableItemGeneration(MT.Butter, MT.ButterSalted, MT.Chocolate, MT.Cheese, MT.MeatRaw, MT.MeatCooked, MT.FishRaw, MT.FishCooked, MT.Tofu, MT.SoylentGreen);
		OP.gemChipped   .disableItemGeneration(MT.EnergiumRed, MT.EnergiumCyan);
		OP.gemFlawed    .disableItemGeneration(MT.EnergiumRed, MT.EnergiumCyan);
		OP.gem          .disableItemGeneration(MT.EnergiumRed, MT.EnergiumCyan);
		OP.gemFlawless  .disableItemGeneration(MT.EnergiumRed, MT.EnergiumCyan);
		OP.gemExquisite .disableItemGeneration(MT.EnergiumRed, MT.EnergiumCyan);
		OP.gemLegendary .disableItemGeneration(MT.EnergiumRed, MT.EnergiumCyan);


		RM.pulverizing(ST.make(Blocks.COBBLESTONE   , 1, W), ST.make(Blocks.SAND, 1, 0), null, 0, F);
		RM.pulverizing(ST.make(Blocks.STONE         , 1, W), ST.make(Blocks.COBBLESTONE, 1, 0), null, 0, F);
		RM.pulverizing(ST.make(Blocks.GRAVEL        , 1, W), ST.make(Items.FLINT, 1, 0), OP.dustSmall.mat(MT.Flint, 1), 10, F);
		RM.pulverizing(ST.make(Blocks.FURNACE       , 1, W), ST.make(Blocks.SAND, 6, 0), null, 0, F);
		RM.pulverizing(ST.make(Items.BONE           , 1, W), IL.Dye_Bonemeal.get(2), IL.Dye_Bonemeal.get(1), 50, T);
		RM.pulverizing(ST.make(Items.BLAZE_ROD      , 1, W), ST.make(Items.BLAZE_POWDER, 3, 0), ST.make(Items.BLAZE_POWDER, 1, 0), 50, T);
		RM.pulverizing(ST.make(Blocks.PUMPKIN       , 1, W), ST.make(Items.PUMPKIN_SEEDS, 4, 0), null, 0, F);
		RM.pulverizing(ST.make(Items.MELON          , 1, W), ST.make(Items.MELON_SEEDS, 1, 0), null, 0, F);
		RM.pulverizing(ST.make(Blocks.WHITE_WOOL          , 1, W), ST.make(Items.STRING, 2, 0), ST.make(Items.STRING, 1, 0), 50, F);

		new Loader_Fluids().run();
		new Loader_Tools().run();
		new Loader_Items().run();
		new Loader_PrefixBlocks().run();
		new Loader_Rocks().run();
		new Loader_Blocks().run();
		new Loader_Woods().run();
		new Loader_Rails().run();
		new Loader_Ores().run();
		new Loader_Others().run();

//      new Loader_CircuitBehaviors().run();
//      new Loader_CoverBehaviors().run();
//      new Loader_Sonictron().run();

		new CompatMods(MD.MC, this) {@Override public void onLoadComplete(FMLLoadCompleteEvent aInitEvent) {
			// Clearing the AE Grindstone Recipe List, so we don't need to worry about pre-existing Recipes.
			//TODO: Re-add AE Support
			//if (MD.AE.mLoaded) AEApi.instance().registries().grinder().getRecipes().clear();
			// We ain't got Water in that Water Bottle. That would be an infinite Water Exploit.
			//TODO: Fluid Container Exploit Fix
			//for (FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) if (tData.filledContainer.getItem() == Items.potionitem && ST.meta_(tData.filledContainer) == 0) {tData.fluid.amount = 0; break;}

			ArrayListNoNulls<Runnable> tList = new ArrayListNoNulls<>(F,
				new Loader_BlockResistance(),
				new Loader_Fuels(),
				new Loader_Loot(),

				new Loader_Recipes_Furnace(), // has to be before everything else!
				new Loader_Recipes_Woods(), // has to be before Vanilla!
				new Loader_Recipes_Vanilla(), // has to be after Woods!
				new Loader_Recipes_Temporary(),
				new Loader_Recipes_Chem(),
				new Loader_Recipes_Crops(),
				new Loader_Recipes_Potions(),
				new Loader_Recipes_Food(),
				new Loader_Recipes_Ores(),
				new Loader_Recipes_Alloys(),
				new Loader_Recipes_Other(),

				new Loader_Recipes_Extruder()
			);

			for (Runnable tRunnable : tList) try {tRunnable.run();} catch(Throwable e) {e.printStackTrace(ERR);}
		}};

		new Compat_Recipes_Ganys                (MD.GAPI          , this);
		new Compat_Recipes_Chisel               (MD.CHSL          , this);
		new Compat_Recipes_FunkyLocomotion      (MD.FUNK          , this);
		new Compat_Recipes_BetterBeginnings     (MD.BB            , this);
		new Compat_Recipes_IndustrialCraft      (MD.IC2           , this);
		new Compat_Recipes_IndustrialCraft_Scrap(MD.IC2           , this);
		new Compat_Recipes_BuildCraft           (MD.BC            , this);
		new Compat_Recipes_Railcraft            (MD.RC            , this); // has to be before MFR!
		new Compat_Recipes_ThermalExpansion     (MD.TE_FOUNDATION , this);
		new Compat_Recipes_Forestry             (MD.FR            , this);
		new Compat_Recipes_MagicBees            (MD.FRMB          , this);
		new Compat_Recipes_Binnie               (MD.BINNIE        , this);
		new Compat_Recipes_BetterRecords        (MD.BETTER_RECORDS, this);
		new Compat_Recipes_BalkonsWeaponMod     (MD.BWM           , this);
		new Compat_Recipes_OpenModularTurrets   (MD.OMT           , this);
		new Compat_Recipes_TechGuns             (MD.TG            , this);
		new Compat_Recipes_Atum                 (MD.ATUM          , this);
		new Compat_Recipes_Tropicraft           (MD.TROPIC        , this);
		new Compat_Recipes_CandyCraft           (MD.CANDY         , this);
		new Compat_Recipes_JABBA                (MD.JABBA         , this);
		new Compat_Recipes_Factorization        (MD.FZ            , this);
		new Compat_Recipes_MineFactoryReloaded  (MD.MFR           , this); // Has to be after RC!
		new Compat_Recipes_AppliedEnergistics   (MD.AE            , this);
		new Compat_Recipes_Bluepower            (MD.BP            , this);
		new Compat_Recipes_ProjectRed           (MD.PR            , this);
		new Compat_Recipes_ProjectE             (MD.PE            , this);
		new Compat_Recipes_OpenComputers        (MD.OC            , this);
		new Compat_Recipes_GrowthCraft          (MD.GrC           , this);
		new Compat_Recipes_HarvestCraft         (MD.HaC           , this);
		new Compat_Recipes_MoCreatures          (MD.MoCr          , this);
		new Compat_Recipes_Lycanites            (MD.LycM          , this);
		new Compat_Recipes_Erebus               (MD.ERE           , this);
		new Compat_Recipes_Betweenlands         (MD.BTL           , this);
		new Compat_Recipes_TwilightForest       (MD.TF            , this);
		new Compat_Recipes_Enviromine           (MD.ENVM          , this);
		new Compat_Recipes_ExtraBiomesXL        (MD.EBXL          , this);
		new Compat_Recipes_BiomesOPlenty        (MD.BoP           , this);
		new Compat_Recipes_Highlands            (MD.HiL           , this);
		new Compat_Recipes_Mariculture          (MD.MaCu          , this);
		new Compat_Recipes_ImmersiveEngineering (MD.IE            , this);
		new Compat_Recipes_Reika                (MD.DRGN          , this);
		new Compat_Recipes_Voltz                (MD.VOLTZ         , this);
		new Compat_Recipes_Mekanism             (MD.Mek           , this);
		new Compat_Recipes_GalactiCraft         (MD.GC            , this);
		new Compat_Recipes_Mystcraft            (MD.MYST          , this);
		new Compat_Recipes_Witchery             (MD.WTCH          , this);
		new Compat_Recipes_Thaumcraft           (MD.TC            , this);
		new Compat_Recipes_ForbiddenMagic       (MD.TCFM          , this);
		new Compat_Recipes_ArsMagica            (MD.ARS           , this);
		new Compat_Recipes_Botania              (MD.BOTA          , this);
		new Compat_Recipes_Aether               (MD.AETHER        , this);
		new Compat_Recipes_RandomThings         (MD.RT            , this);
		new Compat_Recipes_ActuallyAdditions    (MD.AA            , this);
		new Compat_Recipes_ExtraUtilities       (MD.ExU           , this);
		new Compat_Recipes_WRCBE                (MD.WR_CBE_C      , this);

		new CompatMods(MD.GT, this) {@Override public void onLoadComplete(FMLLoadCompleteEvent aInitEvent) {
			ArrayListNoNulls<Runnable> tList = new ArrayListNoNulls<>(F,
				new Loader_Recipes_Replace(),
				new Loader_Recipes_Copy(),
				new Loader_Recipes_Decomp(),
				new Loader_Recipes_Handlers()
			);
			for (Runnable tRunnable : tList) try {tRunnable.run();} catch(Throwable e) {e.printStackTrace(ERR);}
		}};

		//for (FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) if (tData.filledContainer.getItem() == Items.potionitem && ST.meta_(tData.filledContainer) == 0) {tData.fluid.amount = 0; break;}
		OP.chemtube.mContainerItem = OP.chemtube.mat(MT.Empty, 1);

		new Loader_Late_Items_And_Blocks().run();

		//if (MD.IC2C.mLoaded) for (int i = 0; i <= 6; i++) FMLInterModComms.sendMessage(MD.IC2C.mID, "generatorDrop", ST.save(UT.NBT.makeInt("Key", i), "Value", IL.IC2_Machine.get(1)));

		ArrayListNoNulls<Runnable> tList = new ArrayListNoNulls<>(F,
			new Loader_MultiTileEntities(),
			new Loader_Books(),
			new Loader_OreProcessing(),
			new Loader_Worldgen(),
			new Loader_ItemIterator()
		);
		for (Runnable tRunnable : tList) try {tRunnable.run();} catch(Throwable e) {e.printStackTrace(ERR);}
	}

	@Override
	public void onModLoadComplete(FMLLoadCompleteEvent aEvent) {
		if (!MD.RC.mLoaded) {
			CR.shaped(ST.make(Blocks.RAIL          ,  4, 0), DEF_REV_NCC | DEL_OTHER_SHAPED_RECIPES, "RSR", "RSR", "RSR", 'R', OP.railGt.dat(ANY.Fe), 'S', OP.stick.dat(MT.WoodSealed));
			CR.shaped(ST.make(Blocks.POWERED_RAIL   ,  4, 0), DEF_REV_NCC | DEL_OTHER_SHAPED_RECIPES, "RSR", "GDG", "RSR", 'R', OP.railGt.dat(ANY.Fe), 'S', OP.stick.dat(MT.WoodSealed), 'D', OD.itemRedstone, 'G', OP.railGt.dat(MT.Au));
			CR.shaped(ST.make(Blocks.DETECTOR_RAIL ,  4, 0), DEF_REV_NCC | DEL_OTHER_SHAPED_RECIPES, "RSR", "RPR", "RDR", 'R', OP.railGt.dat(ANY.Fe), 'S', OP.stick.dat(MT.WoodSealed), 'D', OD.itemRedstone, 'P', ST.make(Blocks.STONE_PRESSURE_PLATE, 1, W));

			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL,  1, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(MT.Al             ), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL,  1, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(MT.Bronze         ), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL,  2, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(ANY.Fe            ), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL,  3, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(ANY.Steel         ), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL,  4, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(MT.StainlessSteel ), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL,  6, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(MT.Ti             ), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL,  6, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(ANY.W             ), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL, 12, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(MT.TungstenSteel  ), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
			CR.shaped(ST.make(Blocks.ACTIVATOR_RAIL, 12, 0), DEF | DEL_OTHER_SHAPED_RECIPES, "RSR", "RTR", "RSR", 'R', OP.railGt.dat(MT.TungstenCarbide), 'S', OP.stick.dat(MT.WoodSealed), 'T', OD.craftingRedstoneTorch);
		}

		ItemStack tLignite = ST.make(MD.UB, "ligniteCoal", 1, 0);
		if (ST.valid(tLignite)) CR.remove(tLignite, tLignite, tLignite, tLignite, tLignite, tLignite, tLignite, tLignite, tLignite);

		//Block tBlock = ST.block(MD.FR, "beehives", NB);
		//if (tBlock != NB) {tBlock.setHarvestLevel("scoop", 0); GT_Tool_Scoop.sBeeHiveMaterial = tBlock.getMaterial();}

//      if (IL.FR_Tree_Sapling  .get(1) != null)    RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.FR_Tree_Sapling  .getWildcard(1)}                                , ST.array(IL.FR_Tree_Sapling   .getWithName(1, "Scanned Sapling"       )}, null                                                    , FL.array(MT.Honey.liquid(U/20, T)}, null, 500, 2, 0);
//      if (IL.FR_Butterfly     .get(1) != null)    RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.FR_Butterfly     .getWildcard(1)}                                , ST.array(IL.FR_Butterfly      .getWithName(1, "Scanned Butterfly"     )}, null                                                    , FL.array(MT.Honey.liquid(U/20, T)}, null, 500, 2, 0);
//      if (IL.FR_Larvae        .get(1) != null)    RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.FR_Larvae        .getWildcard(1)}                                , ST.array(IL.FR_Larvae         .getWithName(1, "Scanned Larvae"        )}, null                                                    , FL.array(MT.Honey.liquid(U/20, T)}, null, 500, 2, 0);
//      if (IL.FR_Serum         .get(1) != null)    RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.FR_Serum         .getWildcard(1)}                                , ST.array(IL.FR_Serum          .getWithName(1, "Scanned Serum"         )}, null                                                    , FL.array(MT.Honey.liquid(U/20, T)}, null, 500, 2, 0);
//      if (IL.FR_Caterpillar   .get(1) != null)    RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.FR_Caterpillar   .getWildcard(1)}                                , ST.array(IL.FR_Caterpillar    .getWithName(1, "Scanned Caterpillar"   )}, null                                                    , FL.array(MT.Honey.liquid(U/20, T)}, null, 500, 2, 0);
//      if (IL.FR_PollenFertile .get(1) != null)    RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.FR_PollenFertile .getWildcard(1)}                                , ST.array(IL.FR_PollenFertile  .getWithName(1, "Scanned Pollen"        )}, null                                                    , FL.array(MT.Honey.liquid(U/20, T)}, null, 500, 2, 0);
//      if (IL.IC2_Crop_Seeds   .get(1) != null)    RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.IC2_Crop_Seeds   .getWildcard(1)}                                , ST.array(IL.IC2_Crop_Seeds    .getWithName(1, "Scanned Seeds"         )}, null                                                    , null, null, 160,  8, 0);
//                                                  RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(ST.make(Items.written_book, 1, W)}                          , ST.array(IL.Tool_DataStick    .getWithName(1, "Scanned Book Data"     )}, IL.Tool_DataStick.getWithName(1, "Stick to save it to") , null, null, 128, 32, 0);
//                                                  RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(ST.make(Items.filled_map, 1, W)}                                , ST.array(IL.Tool_DataStick    .getWithName(1, "Scanned Map Data"      )}, IL.Tool_DataStick.getWithName(1, "Stick to save it to") , null, null, 128, 32, 0);
//                                                  RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.Tool_DataOrb     .getWithName(1, "Orb to overwrite")}            , ST.array(IL.Tool_DataOrb      .getWithName(1, "Copy of the Orb"       )}, IL.Tool_DataOrb.getWithName(0, "Orb to copy")           , null, null, 512, 32, 0);
//                                                  RecipeMap.sScannerFakeRecipes.addFakeRecipe(F, ST.array(IL.Tool_DataStick   .getWithName(1, "Stick to overwrite")}          , ST.array(IL.Tool_DataStick    .getWithName(1, "Copy of the Stick"     )}, IL.Tool_DataStick.getWithName(0, "Stick to copy")       , null, null, 128, 32, 0);
		
		for (IItemContainer tBee : new IItemContainer[] {IL.FR_Bee_Drone, IL.FR_Bee_Princess, IL.FR_Bee_Queen}) if (tBee.exists()) {
		for (String tFluid : FluidsGT.HONEY) if (FL.exists(tFluid))
		RM.Bumblelyzer.addFakeRecipe(F, ST.array(tBee.wild(1)), ST.array(tBee.getWithName(1, "Scanned Bee")), null, null, FL.array(FL.make(tFluid, 50)) , null, 64, 16, 0);
		RM.Bumblelyzer.addFakeRecipe(F, ST.array(tBee.wild(1)), ST.array(tBee.getWithName(1, "Scanned Bee")), null, null, FL.array(FL.Honeydew.make(50))       , null, 64, 16, 0);
		}
		for (IItemContainer tPlant : new IItemContainer[] {IL.FR_Tree_Sapling, IL.IC2_Crop_Seeds}) if (tPlant.exists()) {
		RM.Plantalyzer.addFakeRecipe(F, ST.array(tPlant.wild(1)), ST.array(tPlant.getWithName(1, "Scanned Plant")), null, null, null, null, 64, 16, 0);
		}
		
		
		for (ItemStack tStack : OreDictManager.getOres("bookWritten", F))
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(tStack, IL.USB_Stick_1.get(1))                                              , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Book"                  ), tStack), null, null, ZL_FS, ZL_FS, 512, 16, 0);
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(IL.Paper_Printed_Pages.get(1), IL.USB_Stick_1.get(1))                       , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Book"                  ), IL.Paper_Printed_Pages.get(1)), null, null, ZL_FS, ZL_FS, 512, 16, 0);
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(IL.Paper_Printed_Pages_Many.get(1), IL.USB_Stick_1.get(1))                  , ST.array(IL.USB_Stick_1.getWithName(1, "Containing large scanned Book"            ), IL.Paper_Printed_Pages_Many.get(1)), null, null, ZL_FS, ZL_FS, 512, 16, 0);
		for (ItemStack tStack : OreDictManager.getOres("gt:canvas", F))
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(tStack, IL.USB_Stick_1.get(1))                                              , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Block"                 ), tStack), null, null, ZL_FS, ZL_FS, 64, 16, 0);
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(ST.make(Blocks.CRAFTING_TABLE, 1, 0, "ANY BLOCK"), IL.USB_Stick_1.get(1))   , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Block"                 ), ST.make(Blocks.CRAFTING_TABLE, 1, 0, "ANY BLOCK")), null, null, ZL_FS, ZL_FS, 512, 16, 0);
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(ST.make(Items.FILLED_MAP, 1, W), IL.USB_Stick_1.get(1))                     , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Map"                   ), ST.make(Items.FILLED_MAP, 1, W)), null, null, ZL_FS, ZL_FS, 64, 16, 0);
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(IL.Paper_Blueprint_Used.get(1), IL.USB_Stick_1.get(1))                      , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Blueprint"             ), IL.Paper_Blueprint_Used.get(1)), null, null, ZL_FS, ZL_FS, 64, 16, 0);
		if (IL.GC_Schematic_1.exists())
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(IL.GC_Schematic_1.wild(1), IL.USB_Stick_1.get(1))                           , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Schematics"            ), IL.GC_Schematic_1.wild(1)), null, null, ZL_FS, ZL_FS, 1024, 16, 0);
		if (IL.GC_Schematic_2.exists())
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(IL.GC_Schematic_2.wild(1), IL.USB_Stick_1.get(1))                           , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Schematics"            ), IL.GC_Schematic_2.wild(1)), null, null, ZL_FS, ZL_FS, 1024, 16, 0);
		if (IL.GC_Schematic_3.exists())
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(IL.GC_Schematic_3.wild(1), IL.USB_Stick_1.get(1))                           , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Schematics"            ), IL.GC_Schematic_3.wild(1)), null, null, ZL_FS, ZL_FS, 1024, 16, 0);
		if (IL.IE_Blueprint_Projectiles_Common.exists())
		RM.ScannerVisuals.addFakeRecipe(F, ST.array(IL.IE_Blueprint_Projectiles_Common.wild(1), IL.USB_Stick_1.get(1))          , ST.array(IL.USB_Stick_1.getWithName(1, "Containing scanned Engineer's Blueprint"  ), IL.IE_Blueprint_Projectiles_Common.wild(1)), null, null, ZL_FS, ZL_FS, 1024, 16, 0);
		
		RM.Printer.addRecipe1(T, 16, 256, ST.make(Items.BOOK, 1, W), DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], NF, ST.book("Manual_Printer", ST.make(Items.WRITABLE_BOOK, 1, 0)));
		
		for (ItemStack tStack : OreDictManager.getOres("gt:canvas", F))
		RM.Printer.addFakeRecipe(F, ST.array(tStack                             , IL.USB_Stick_1.getWithName(0, "Containing scanned Block"               )), ST.array(tStack                                    ), null, null, FL.array(FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Yellow], 1, 9, T), FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Magenta], 1, 9, T), FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Cyan], 1, 9, T), FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], 1, 9, T)), ZL_FS,   64, 16, 0);
//      RM.Printer.addFakeRecipe(F, ST.array(IL.Paper_Punch_Card_Empty.get(1)   , IL.USB_Stick_1.getWithName(0, "Containing scanned Punchcard"           )), ST.array(IL.Paper_Punch_Card_Encoded.get(1)        ), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], 1, 9, T)), ZL_FS,   32, 16, 0);
		RM.Printer.addFakeRecipe(F, ST.array(IL.Paper_Blueprint_Empty.get(1)    , IL.USB_Stick_1.getWithName(0, "Containing scanned Blueprint"           )), ST.array(IL.Paper_Blueprint_Used.get(1)            ), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_White], 1, 9, T)), ZL_FS,   32, 16, 0);
		RM.Printer.addFakeRecipe(F, ST.array(ST.make(Items.PAPER, 1, W)         , IL.USB_Stick_1.getWithName(0, "Containing scanned Blueprint"           )), ST.array(IL.Paper_Blueprint_Used.get(1)            ), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Blue ], 1, 1, T)), ZL_FS,  128, 16, 0);
		RM.Printer.addFakeRecipe(F, ST.array(ST.make(Items.PAPER, 3, W)         , IL.USB_Stick_1.getWithName(0, "Containing scanned Book"                )), ST.array(IL.Paper_Printed_Pages.get(1)             ), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], 1, 2, T)), ZL_FS,  512, 16, 0);
		RM.Printer.addFakeRecipe(F, ST.array(ST.make(Items.PAPER, 6, W)         , IL.USB_Stick_1.getWithName(0, "Containing large scanned Book"          )), ST.array(IL.Paper_Printed_Pages_Many.get(1)        ), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], 1, 1, T)), ZL_FS, 1024, 16, 0);
		RM.Printer.addFakeRecipe(F, ST.array(ST.make(Items.MAP, 1, W)           , IL.USB_Stick_1.getWithName(0, "Containing scanned Map"                 )), ST.array(ST.make(Items.FILLED_MAP, 1, 0)           ), null, null, FL.array(FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Yellow], 1, 9, T), FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Magenta], 1, 9, T), FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Cyan], 1, 9, T), FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], 1, 9, T)), ZL_FS,   64, 16, 0);
		if (IL.GC_Schematic_1.exists())
		RM.Printer.addFakeRecipe(F, ST.array(ST.make(Items.PAPER, 8, W)         , IL.USB_Stick_1.getWithName(0, "Containing scanned Schematics"          )), ST.array(IL.GC_Schematic_1.wild(1)                 ), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], 4, 1, T)), ZL_FS, 2048, 16, 0);
		if (IL.GC_Schematic_2.exists())
		RM.Printer.addFakeRecipe(F, ST.array(ST.make(Items.PAPER, 8, W)         , IL.USB_Stick_1.getWithName(0, "Containing scanned Schematics"          )), ST.array(IL.GC_Schematic_2.wild(1)                 ), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], 4, 1, T)), ZL_FS, 2048, 16, 0);
		if (IL.GC_Schematic_3.exists())
		RM.Printer.addFakeRecipe(F, ST.array(ST.make(Items.PAPER, 8, W)         , IL.USB_Stick_1.getWithName(0, "Containing scanned Schematics"          )), ST.array(IL.GC_Schematic_3.wild(1)                 ), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Black], 4, 1, T)), ZL_FS, 2048, 16, 0);
		if (IL.IE_Blueprint_Projectiles_Common.exists())
		RM.Printer.addFakeRecipe(F, ST.array(ST.make(Items.PAPER, 3, W)         , IL.USB_Stick_1.getWithName(0, "Containing scanned Engineer's Blueprint")), ST.array(IL.IE_Blueprint_Projectiles_Common.wild(1)), null, null, FL.array(                                                                                                                                                                       FL.mul(DYE_FLUIDS_CHEMICAL[DYE_INDEX_Blue ], 3, 1, T)), ZL_FS, 2048, 16, 0);
		
		if (IL.IE_Treated_Stairs.exists())  RM.Bath.addFakeRecipe(F, ST.array(ST.make(Blocks.OAK_STAIRS , 1, W)), ST.array(IL.IE_Treated_Stairs.get(1                               )), null, null, FL.array(FL.Oil_Creosote.make( 75)), ZL_FS, 102, 0, 0);
											RM.Bath.addFakeRecipe(F, ST.array(ST.make(Blocks.OAK_SLAB, 1, W)), ST.array(IL.IE_Treated_Slab  .get(1, IL.Treated_Planks_Slab.get(1))), null, null, FL.array(FL.Oil_Creosote.make( 50)), ZL_FS,  72, 0, 0);
											RM.Bath.addFakeRecipe(F, ST.array(IL.Plank_Slab             .get(1)), ST.array(IL.IE_Treated_Slab  .get(1, IL.Treated_Planks_Slab.get(1))), null, null, FL.array(FL.Oil_Creosote.make( 50)), ZL_FS,  72, 0, 0);
		if (IL.ERE_White_Planks.exists())   RM.Bath.addFakeRecipe(F, ST.array(IL.Plank                  .get(1)), ST.array(IL.ERE_White_Planks .get(1                               )), null, null, FL.array(       DYE_FLUIDS_WATER[DYE_INDEX_White]          ), ZL_FS, 144, 0, 0);
		if (IL.ERE_White_Slab.exists())     RM.Bath.addFakeRecipe(F, ST.array(IL.Plank_Slab             .get(1)), ST.array(IL.ERE_White_Slab   .get(1                               )), null, null, FL.array(FL.mul(DYE_FLUIDS_WATER[DYE_INDEX_White], 1, 2, T)), ZL_FS,  72, 0, 0);
		if (IL.ERE_White_Planks.exists())   RM.Bath.addFakeRecipe(F, ST.array(ST.make(Blocks.OAK_PLANKS     , 1, W)), ST.array(IL.ERE_White_Planks .get(1                               )), null, null, FL.array(       DYE_FLUIDS_WATER[DYE_INDEX_White]          ), ZL_FS, 144, 0, 0);
		if (IL.ERE_White_Stairs.exists())   RM.Bath.addFakeRecipe(F, ST.array(ST.make(Blocks.OAK_STAIRS , 1, W)), ST.array(IL.ERE_White_Stairs .get(1                               )), null, null, FL.array(FL.mul(DYE_FLUIDS_WATER[DYE_INDEX_White], 3, 4, T)), ZL_FS, 102, 0, 0);
		if (IL.ERE_White_Slab.exists())     RM.Bath.addFakeRecipe(F, ST.array(ST.make(Blocks.OAK_SLAB, 1, W)), ST.array(IL.ERE_White_Slab   .get(1                               )), null, null, FL.array(FL.mul(DYE_FLUIDS_WATER[DYE_INDEX_White], 1, 2, T)), ZL_FS,  72, 0, 0);
		
		if (IL.LOOTBAGS_Bag_0.exists())     RM.Unboxinator.addFakeRecipe(F, ST.array(IL.LOOTBAGS_Bag_0.get(1)), ST.array(IL.LOOTBAGS_Bag_0.getWithName(1, "Random Drops depending on Config")), null, ZL_LONG, ZL_FS, ZL_FS, 16, 16, 0);
		if (IL.LOOTBAGS_Bag_1.exists())     RM.Unboxinator.addFakeRecipe(F, ST.array(IL.LOOTBAGS_Bag_1.get(1)), ST.array(IL.LOOTBAGS_Bag_1.getWithName(1, "Random Drops depending on Config")), null, ZL_LONG, ZL_FS, ZL_FS, 16, 16, 0);
		if (IL.LOOTBAGS_Bag_2.exists())     RM.Unboxinator.addFakeRecipe(F, ST.array(IL.LOOTBAGS_Bag_2.get(1)), ST.array(IL.LOOTBAGS_Bag_2.getWithName(1, "Random Drops depending on Config")), null, ZL_LONG, ZL_FS, ZL_FS, 16, 16, 0);
		if (IL.LOOTBAGS_Bag_3.exists())     RM.Unboxinator.addFakeRecipe(F, ST.array(IL.LOOTBAGS_Bag_3.get(1)), ST.array(IL.LOOTBAGS_Bag_3.getWithName(1, "Random Drops depending on Config")), null, ZL_LONG, ZL_FS, ZL_FS, 16, 16, 0);
		if (IL.LOOTBAGS_Bag_4.exists())     RM.Unboxinator.addFakeRecipe(F, ST.array(IL.LOOTBAGS_Bag_4.get(1)), ST.array(IL.LOOTBAGS_Bag_4.getWithName(1, "Random Drops depending on Config")), null, ZL_LONG, ZL_FS, ZL_FS, 16, 16, 0);
		
		
		if (CODE_CLIENT) {
			for (OreDictMaterial aMaterial : OreDictMaterial.ALLOYS) {
				for (IOreDictConfigurationComponent tAlloy : aMaterial.mAlloyCreationRecipes) {
					boolean temp = T;
					ArrayListNoNulls<ItemStack> tDusts = new ArrayListNoNulls<>(), tIngots = new ArrayListNoNulls<>();
					ArrayListNoNulls<Long> tMeltingPoints = new ArrayListNoNulls<>();
					for (OreDictMaterialStack tMaterial : tAlloy.getUndividedComponents()) {
						if (tMaterial.mMaterial == MT.Air) {
							if (!tDusts.add(FL.Air.display(UT.Code.units(tMaterial.mAmount, U, 1000, T)))) {temp = F; break;}
							tIngots.add(FL.Air.display(UT.Code.units(tMaterial.mAmount, U, 1000, T)));
						} else {
							tMeltingPoints.add(tMaterial.mMaterial.mMeltingPoint);
							if (!tDusts.add(OM.dustOrIngot(tMaterial.mMaterial, tMaterial.mAmount))) {temp = F; break;}
							tIngots.add(OM.ingotOrDust(tMaterial.mMaterial, tMaterial.mAmount));
						}
					}
					Collections.sort(tMeltingPoints);
					if (temp) {
						RM.CrucibleAlloying.addFakeRecipe(F, tDusts .toArray(ZL_IS), ST.array(OM.ingotOrDust(aMaterial, tAlloy.getCommonDivider() * U)), null, null, null, null, 0, 0, tMeltingPoints.size()>1?Math.max(tMeltingPoints.get(tMeltingPoints.size()-2), aMaterial.mMeltingPoint):aMaterial.mMeltingPoint);
						RM.CrucibleAlloying.addFakeRecipe(F, tIngots.toArray(ZL_IS), ST.array(OM.ingotOrDust(aMaterial, tAlloy.getCommonDivider() * U)), null, null, null, null, 0, 0, tMeltingPoints.size()>1?Math.max(tMeltingPoints.get(tMeltingPoints.size()-2), aMaterial.mMeltingPoint):aMaterial.mMeltingPoint);
					}
				}
			}
			for (OreDictMaterial aMaterial : OreDictMaterial.MATERIAL_ARRAY) if (aMaterial != null) {
				Recipe tRecipe = RecipeMapReplicator.getReplicatorRecipe(aMaterial, IL.USB_Stick_3.getWithName(0, "Mat Data: "+aMaterial.getLocal()));
				if (tRecipe != null) RM.Replicator.addFakeRecipe(F, tRecipe);
			}
		}

		for (MultiItemRandom tItem : ItemsGT.ALL_MULTI_ITEMS) for (Entry<Short, ArrayList<IBehavior<MultiItem>>> tEntry : tItem.mItemBehaviors.entrySet()) for (IBehavior<MultiItem> tBehavior : tEntry.getValue()) if (tBehavior instanceof Behavior_Turn_Into) if (((Behavior_Turn_Into)tBehavior).mTurnInto.exists()) tItem.mVisibleItems.set(tEntry.getKey(), F);
	}

	@Override
	public void onModServerStarting2(FMLServerStartingEvent aEvent) {
		//for (FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) if (tData.filledContainer.getItem() == Items.potionitem && ST.meta_(tData.filledContainer) == 0) {tData.fluid.amount = 0; break;}

		ORD.println("============================");
		ORD.println("Outputting Unknown Materials");
		ORD.println("============================");
		for (String tUnknown : OreDictManager.INSTANCE.getUnknownMaterials()) ORD.println(tUnknown);
		ORD.println("============================");

		if (CODE_CLIENT) {try {
		ORD.println("============================");
		ORD.println("Outputting Colors of unknown Materials");
		ORD.println("============================");
		for (OreDictMaterial tUnknown : OreDictMaterial.MATERIAL_MAP.values()) if (tUnknown != null && tUnknown.contains(TD.Properties.UNUSED_MATERIAL) && !tUnknown.contains(TD.Properties.IGNORE_IN_COLOR_LOG)) {
			for (ItemStackContainer aStack : tUnknown.mRegisteredItems) {
				ItemStack tStack = aStack.toStack();
				if (ST.valid(tStack) && ST.block(tStack) == NB && !(tStack.getItem() instanceof PrefixItem) && !(tStack.getItem() instanceof PrefixBlockItem)) {
					short[] tRGB = UT.Code.color(tStack);
					if (tRGB != null && tRGB != UNCOLOURED) ORD.println(tUnknown.mNameInternal + "  -  RGB: " + tRGB[0]+", "+tRGB[1]+", "+tRGB[2] + "  -  " + ST.names(tStack));
				}
			}
		}
		ORD.println("============================");
		} catch(Throwable e) {e.printStackTrace(ERR);}}

		ORD.println("================================");
		ORD.println("Outputting Unknown OreDict Names");
		ORD.println("================================");
		for (String tUnknown : OreDictManager.INSTANCE.getUnknownNames()) ORD.println(tUnknown);
		ORD.println("================================");

		/*
		try {((CommandHandler)aEvent.getServer().getCommandManager()).registerCommand(new CommandBase() {
			@Override public String getCommandName() {return "xyzd";}
			@Override public String getCommandUsage(ICommandSender aSender) {return E;}
			@Override public int getRequiredPermissionLevel() {return 0;}
			@Override public boolean canCommandSenderUseCommand(ICommandSender aSender) {return T;}
			@Override public void processCommand(ICommandSender aSender, String[] aParameters) {
				if (aParameters.length >= 3) {
					EntityPlayerMP aPlayer = getCommandSenderAsPlayer(aSender);
					if (aPlayer != null && (aPlayer.username.equals("GregoriusT") || aPlayer.username.equals("Player"))) {
						try {
							if (aPlayer.ridingEntity != null) aPlayer.mountEntity(null);
							if (aPlayer.riddenByEntity != null) aPlayer.riddenByEntity.mountEntity(null);

							if (aParameters.length >= 4) {
								GT_Utility.moveEntityToDimensionAtCoords(aPlayer, Integer.parseInt(aParameters[3]), Integer.parseInt(aParameters[0])+0.5, Integer.parseInt(aParameters[1])+0.5, Integer.parseInt(aParameters[2])+0.5);
							} else {
								aPlayer.setPositionAndUpdate(Integer.parseInt(aParameters[0]), Integer.parseInt(aParameters[1]), Integer.parseInt(aParameters[2]));
							}
						} catch(Throwable e) {/*Do nothing}
					}
				}
			}
		});} catch(Throwable e) {/*Do nothing}
		*/
		//TODO: IC2 Support
		//if (MD.IC2.mLoaded && !MD.IC2C.mLoaded) try {if (mDisableIC2Ores) Ic2Items.tinOre = Ic2Items.leadOre = Ic2Items.copperOre = Ic2Items.uraniumOre = null;} catch (Throwable e) {e.printStackTrace(ERR);}
		if (MD.TE.mLoaded) {
			ItemStack tPyrotheum = OP.dust.mat(MT.Pyrotheum, 1);
			for (ItemStackContainer tStack : OP.ore.mRegisteredItems) CR.remove(tStack.toStack(), tPyrotheum);
		}
	}

	public boolean mDisableIC2Ores = T;

	@Override
	public void onModServerStopping2(FMLServerStoppingEvent aEvent) {
		try {
		if (D1 || ORD != System.out) {
			ORD.println("*");
			ORD.println("TagData:");
			ORD.println("*"); ORD.println("*"); ORD.println("*");

			for (TagData tData : TagData.TAGS) ORD.println(tData.mName);

			ORD.println("*");
			ORD.println("ItemRegistry:");
			ORD.println("*"); ORD.println("*"); ORD.println("*");

			Object[] tList = ForgeRegistries.ITEMS.getKeys().toArray();

			Arrays.sort(tList);
			for (Object tItemName : tList) ORD.println(tItemName);

			ORD.println("*");
			ORD.println("Tags:");
			ORD.println("*"); ORD.println("*"); ORD.println("*");

			tList = ItemTags.getCollection().getTagMap().keySet().toArray();
			Arrays.sort(tList);
			//for (Object tOreName : tList) {
				//int tAmount = ItemTags..getOres(tOreName.toString()).size();
				//if (tAmount > 0) ORD.println((tAmount<10?" ":"") + tAmount + "x " + tOreName);
			//}

			ORD.println("*");
			ORD.println("Materials:");
			ORD.println("*"); ORD.println("*"); ORD.println("*");

			for (int i = 0; i < OreDictMaterial.MATERIAL_ARRAY.length; i++) {
				OreDictMaterial tMaterial = OreDictMaterial.MATERIAL_ARRAY[i];
				if (tMaterial == null) {
					if (i >= 8000 && i < 10000) {
						ORD.println(i + ": <RESERVED>");
					}
				} else {
					if (tMaterial.mToolTypes > 0) {
						ORD.println(i + ": " + tMaterial.mNameInternal + "; T:" + tMaterial.mToolTypes + "; Q:" + tMaterial.mToolQuality + "; D:" + tMaterial.mToolDurability + "; S:" + tMaterial.mToolSpeed);
					} else {
						ORD.println(i + ": " + tMaterial.mNameInternal);
					}
				}
			}

			ORD.println("*");
			ORD.println("Fluids:");
			ORD.println("*"); ORD.println("*"); ORD.println("*");

			tList = ForgeRegistries.FLUIDS.getKeys().stream().map(ResourceLocation::toString).collect(Collectors.toList()).toArray(ZL_STRING);
			Arrays.sort(tList);
			for (Object tFluidName : tList) ORD.println(tFluidName);

			ORD.println("*"); ORD.println("*"); ORD.println("*");
			ORD.println("Biomes:");
			ORD.println("*"); ORD.println("*"); ORD.println("*");

			ForgeRegistries.BIOMES.getKeys().forEach(e -> ORD.println(e.toString()));

			ORD.println("*"); ORD.println("*"); ORD.println("*");
			ORD.println("Enchantments:");
			ORD.println("*"); ORD.println("*"); ORD.println("*");

			ForgeRegistries.ENCHANTMENTS.getKeys().forEach(e -> ORD.println(e.toString()));


			ORD.println("*"); ORD.println("*"); ORD.println("*");
			ORD.println("END GregTech-Debug");
			ORD.println("*"); ORD.println("*"); ORD.println("*");
		}
		} catch(Throwable e) {if (D1) e.printStackTrace(ERR);}
	}

	@Override public void onModServerStarted2(FMLServerStartedEvent aEvent) {/**/}
	@Override public void onModServerStopped2(FMLServerStoppedEvent aEvent) {/**/}

	@Override public String getModID() {return MD.GT.mID;}
	@Override public String getModName() {return MD.GT.mName;}
	@Override public String getModNameForLog() {return "GT_Mod";}
	@Override public Abstract_Proxy getProxy() {return gt_proxy;}

	@SubscribeEvent public void onFMLCommonSetup  (FMLCommonSetupEvent  	  aEvent) {onModPreInit(aEvent);}
	@SubscribeEvent public void onLoadComplete    (FMLLoadCompleteEvent 	  aEvent) {onModPostInit(aEvent);}
	@SubscribeEvent public void onServerStarting  (FMLServerStartingEvent     aEvent) {onModServerStarting(aEvent);}
	@SubscribeEvent public void onServerStarted   (FMLServerStartedEvent      aEvent) {onModServerStarted(aEvent);}
	@SubscribeEvent public void onServerStopping  (FMLServerStoppingEvent     aEvent) {onModServerStopping(aEvent);}
	@SubscribeEvent public void onServerStopped   (FMLServerStoppedEvent      aEvent) {onModServerStopped(aEvent);}
}
