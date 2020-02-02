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

package gregtech;

import static gregapi.data.CS.*;

import java.net.URL;
import java.util.*;

import gregapi.GT_API;
import gregapi.api.Abstract_Mod;
import gregapi.api.Abstract_Proxy;
import gregapi.block.IBlockToolable;
import gregapi.code.ArrayListNoNulls;
import gregapi.code.HashSetNoNulls;
import gregapi.data.CS.BlocksGT;
import gregapi.data.CS.ItemsGT;
import gregapi.data.CS.SFX;
import gregapi.data.FL;
import gregapi.data.IL;
import gregapi.data.MD;
import gregapi.data.OP;
import gregapi.oredict.OreDictItemData;
import gregapi.oredict.OreDictMaterial;
import gregapi.render.IIconContainer;
import gregapi.util.OM;
import gregapi.util.ST;
import gregapi.util.UT;
import gregapi.util.WD;
import gregtech.blocks.fluids.BlockWaterlike;
import gregtech.entities.Override_Drops;
import gregtech.entities.projectiles.EntityArrow_Material;
import gregtech.tileentity.misc.MultiTileEntityCertificate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class GT_Proxy extends Abstract_Proxy {
	public final HashSetNoNulls<String> mSupporterListSilver = new HashSetNoNulls<>();
	public final HashSetNoNulls<String> mSupporterListGold = new HashSetNoNulls<>();
	
	public String mMessage = "";
	
	public boolean mDisableVanillaOres = T, mVersionOutdated = F;
	public int mSkeletonsShootGTArrows = 16, mFlintChance = 30;
	
	public GT_Proxy() {
		MinecraftForge.EVENT_BUS                       .register(this);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}
	
	@Override
	public void onProxyBeforeCommonSetup(Abstract_Mod aMod, FMLCommonSetupEvent aEvent) {
		super.onProxyBeforeCommonSetup(aMod, aEvent);
		new Thread(new Runnable() {@Override public void run() {
		//TODO: Config
		//if (ConfigsGT.CLIENT.get(ConfigCategories.news, "version_checker", T)) try {
			// Using http because Java screws up https on Windows at times.
			//String tVersion = javax.xml.xpath.XPathFactory.newInstance().newXPath().compile("metadata/versioning/release/text()").evaluate(javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse((new URL("http://gregtech.mechaenetia.com/com/gregoriust/gregtech/gregtech_1.7.10/maven-metadata.xml")).openConnection().getInputStream()), javax.xml.xpath.XPathConstants.STRING).toString().substring(0, 7);
			// Check if the first 4 Characters of the Version Number are the same, quick and dirty check that doesn't require Number parsing.
			// And just ignore the first Versions of each Major Release, since that one is usually the buggiest or a quickfix.
			//mVersionOutdated = !tVersion.endsWith("00") && !tVersion.endsWith("01") && !BuildInfo.version.startsWith(tVersion.substring(0, 4));
			
			//OUT.println("GT_Download_Thread: Current Version = '" + BuildInfo.version.substring(0, 7) + "'; Recent Version = '" + tVersion + "'; Majorly Outdated = " + (mVersionOutdated?"Yes":"No"));
		//} catch(Throwable e) {OUT.println("GT_Download_Thread: Failed Downloading Version Number of the latest Major Version!");}
		
		if (downloadSupporterListSilverFromMain()) {
			OUT.println("GT_Download_Thread: Downloaded Silver Supporter List!");
		} else try {
			Scanner tScanner = new Scanner(getClass().getResourceAsStream("/supporterlist.txt"));
			while (tScanner.hasNextLine()) mSupporterListSilver.add(tScanner.nextLine().toLowerCase());
			tScanner.close();
			OUT.println("GT_Download_Thread: Failed downloading Silver Supporter List, using interal List!");
		} catch(Throwable e) {e.printStackTrace(ERR);}
		
		if (downloadSupporterListGoldFromMain()) {
			OUT.println("GT_Download_Thread: Downloaded Gold Supporter List!");
		} else try {
			Scanner tScanner = new Scanner(getClass().getResourceAsStream("/supporterlistgold.txt"));
			while (tScanner.hasNextLine()) mSupporterListGold.add(tScanner.nextLine().toLowerCase());
			tScanner.close();
			OUT.println("GT_Download_Thread: Failed downloading Gold Supporter List, using interal List!");
		} catch(Throwable e) {e.printStackTrace(ERR);}
		
		try {
			// Using http because Java screws up https on Windows at times.
			Scanner tScanner = new Scanner(new URL("http://gregtech.mechaenetia.com/com/gregoriust/gregtech/message.txt").openStream());
			while (tScanner.hasNextLine()) mMessage += tScanner.nextLine() + " ";
			tScanner.close();
			if (mMessage.length() > 5) OUT.println("GT_Download_Thread: Downloaded News.");
		} catch(Throwable e) {OUT.println("GT_Download_Thread: Failed downloading News!");}
		
		mSupporterListSilver.removeAll(mSupporterListGold);
		}}).start();
	}
	
	@SubscribeEvent
	public void onClientLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		//
	}
	
	@SubscribeEvent
	public void onEndermanTeleportEvent(EnderTeleportEvent aEvent) {
		if (aEvent.getEntityLiving() instanceof EndermanEntity && aEvent.getEntityLiving().getActivePotionEffect(Effects.WEAKNESS) != null) aEvent.setCanceled(T);
	}

	

	private static final List<Block> PREVENTED_ORES = Arrays.asList(Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE, Blocks.REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.NETHER_QUARTZ_ORE);

	//TODO: Make this happen somewhere
	private static void removeVanillaOreFromGeneration() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).clear();
		}
	}

	/*
	TODO: Terrain and Village Generation Fixes
	
	@SubscribeEvent
	public void onTerrainGenEvent(DecorateBiomeEvent.Decorate aEvent) {
		if (aEvent.world.provider.dimensionId == 0) {
			if (MD.RTG.mLoaded) {
				String tClassName = UT.Reflection.getLowercaseClass(aEvent.world.provider.terrainType);
				if ("WorldProviderSurfaceRTG".equalsIgnoreCase(tClassName) || "WorldTypeRTG".equalsIgnoreCase(tClassName)) return;
			}
			if (GENERATE_STREETS && (UT.Code.inside(-48, 47, aEvent.chunkX) || UT.Code.inside(-48, 47, aEvent.chunkZ))) aEvent.setResult(Result.DENY);
			if (GENERATE_BIOMES  && (UT.Code.inside(-96, 95, aEvent.chunkX) && UT.Code.inside(-96, 95, aEvent.chunkZ))) aEvent.setResult(Result.DENY);
		}
	}
	@SubscribeEvent
	public void onTerrainGenEvent(PopulateChunkEvent.Populate aEvent) {
		if (aEvent.world.provider.dimensionId == 0) {
			if (MD.RTG.mLoaded) {
				String tClassName = UT.Reflection.getLowercaseClass(aEvent.world.provider.terrainType);
				if ("WorldProviderSurfaceRTG".equalsIgnoreCase(tClassName) || "WorldTypeRTG".equalsIgnoreCase(tClassName)) return;
			}
			if (GENERATE_STREETS && (UT.Code.inside(-48, 47, aEvent.chunkX) || UT.Code.inside(-48, 47, aEvent.chunkZ))) aEvent.setResult(Result.DENY);
			if (GENERATE_BIOMES  && (UT.Code.inside(-96, 95, aEvent.chunkX) && UT.Code.inside(-96, 95, aEvent.chunkZ))) aEvent.setResult(Result.DENY);
		}
	}
	@SubscribeEvent
	public void onGetVillageBlockIDEvent(BiomeEvent.GetVillageBlockID aEvent) {
		if (aEvent.original == Blocks.cobblestone) {
			aEvent.replacement = aEvent.biome == null ? BlocksGT.Andesite : BlocksGT.stones[(aEvent.biome.biomeID+6) % BlocksGT.stones.length];
			aEvent.setResult(Result.DENY);
		}
	}
	@SubscribeEvent
	public void onGetVillageBlockMetaEvent(BiomeEvent.GetVillageBlockMeta aEvent) {
		if (aEvent.original == Blocks.cobblestone || aEvent.original instanceof BlockStones) {
			aEvent.replacement = BlockStones.SBRIK;
			aEvent.setResult(Result.DENY);
		}
		if (aEvent.original == Blocks.sandstone) {
			aEvent.replacement = 2; // That's smooth Sandstone.
			aEvent.setResult(Result.DENY);
		}
	}*/
	
	private static final HashSetNoNulls<String> CHECKED_PLAYERS = new HashSetNoNulls<>();
	
	@SubscribeEvent
	public void onPlayerInteraction(PlayerInteractEvent aEvent) {
		if (aEvent.getPlayer() == null || aEvent.getPlayer().world == null) return;
		String aName = aEvent.getPlayer().getCommandSource().getName(), aNameLowercase = aName.toLowerCase();
		if (!aEvent.getWorld().isRemote && CHECKED_PLAYERS.add(aName)) {
			if (mSupporterListSilver.contains(aEvent.getPlayer().getUniqueID().toString()) || mSupporterListGold.contains(aEvent.getPlayer().getUniqueID().toString()) || mSupporterListSilver.contains(aNameLowercase) || mSupporterListGold.contains(aNameLowercase)) {
				if (!MultiTileEntityCertificate.ALREADY_RECEIVED.contains(aNameLowercase)) {
					if (UT.Inventories.addStackToPlayerInventoryOrDrop(aEvent.getPlayer(), MultiTileEntityCertificate.getCertificate(1, aName), F)) {
						MultiTileEntityCertificate.ALREADY_RECEIVED.add(aNameLowercase);
						UT.Entities.sendchat(aEvent.getPlayer(), CHAT_GREG + " Thank you, " + aName + ", for Supporting GregTech! Here, have a Certificate. ;)");
					}
				}
			}
		}
		
		ItemStack aStack = aEvent.getPlayer().getHeldItemMainhand();
		if (!aStack.isEmpty()) {
			if (aEvent instanceof PlayerInteractEvent.RightClickEmpty) {
				if (aStack.getItem() == Items.GLASS_BOTTLE) {
					aEvent.setCanceled(T);
					if (aEvent.getWorld().isRemote) {
						GT_API.api_proxy.sendUseItemPacket(aEvent.getPlayer(), aEvent.getWorld(), aStack);
						return;
					}

					RayTraceResult tTarget = WD.getMOP(aEvent.getWorld(), aEvent.getPlayer(), T);
					if (tTarget == null || tTarget.getType() != RayTraceResult.Type.BLOCK || !aEvent.getWorld().canMineBlockBody(aEvent.getPlayer(), ((BlockRayTraceResult)tTarget).getPos()) || !aEvent.getPlayer().canPlayerEdit(((BlockRayTraceResult)tTarget).getPos(), ((BlockRayTraceResult)tTarget).getFace(), aStack)) return;
					Block tBlock = aEvent.getWorld().getBlockState(((BlockRayTraceResult)tTarget).getPos()).getBlock();
					
					if (tBlock == Blocks.WATER) {
						if (aEvent.getWorld().getBlockState(((BlockRayTraceResult)tTarget).getPos()).get(FlowingFluidBlock.LEVEL) < 15) return;
						for (int i = 0; i < 3 && aStack.getCount() > 0; i++) {
							if (aStack.getCount() == 1) {
								aEvent.getPlayer().setHeldItem(Hand.MAIN_HAND, ST.make(Items.POTION, 1, 0));
							} else {
								ST.use(aEvent.getPlayer(), aStack);
								UT.Inventories.addStackToPlayerInventoryOrDrop(aEvent.getPlayer(), ST.make(Items.POTION, 1, 0), F);
							}
						}
						if (!WD.infiniteWater(aEvent.getWorld(), ((BlockRayTraceResult)tTarget).getPos())) aEvent.getWorld().removeBlock(((BlockRayTraceResult)tTarget).getPos(), false);
						if (aEvent.getPlayer().openContainer != null) aEvent.getPlayer().openContainer.detectAndSendChanges();
						return;
					}
					if (tBlock == BlocksGT.River || WD.waterstream(tBlock)) {
						ItemStack tStack = FL.fill(FL.Water.make(Integer.MAX_VALUE), aStack, F, T, F, T);
						if (tStack == null) return;
						ST.use(aEvent.getPlayer(), aStack);
						UT.Inventories.addStackToPlayerInventoryOrDrop(aEvent.getPlayer(), tStack, F);
						return;
					}
					if (tBlock == BlocksGT.Ocean) {
						ItemStack tStack = FL.fill(FL.Ocean.make(Integer.MAX_VALUE), aStack, F, T, F, T);
						if (tStack == null) return;
						ST.use(aEvent.getPlayer(), aStack);
						UT.Inventories.addStackToPlayerInventoryOrDrop(aEvent.getPlayer(), tStack, F);
						return;
					}
					if (tBlock == BlocksGT.Swamp) {
						ItemStack tStack = FL.fill(FL.Dirty_Water.make(Integer.MAX_VALUE), aStack, F, T, F, T);
						if (tStack == null) return;
						ST.use(aEvent.getPlayer(), aStack);
						UT.Inventories.addStackToPlayerInventoryOrDrop(aEvent.getPlayer(), tStack, F);
						return;
					}
					return;
				}
				if (aStack.getItem() == Items.BUCKET) {
					RayTraceResult tTarget = WD.getMOP(aEvent.getWorld(), aEvent.getPlayer(), T);
					if (tTarget != null && tTarget.getType() == RayTraceResult.Type.BLOCK && aEvent.getWorld().getBlockState(((BlockRayTraceResult)tTarget).getPos()).getBlock() instanceof BlockWaterlike) aEvent.setCanceled(T);
					return;
				}
			}
			if (aEvent instanceof PlayerInteractEvent.RightClickBlock) {
				if (IL.ERE_Spray_Repellant.equal(aStack, T, T)) {
					if (!aEvent.getWorld().isRemote && aStack.getItem().onItemUse(new ItemUseContext(aEvent.getPlayer(), aEvent.getHand(), new BlockRayTraceResult(new Vec3d(0.5F, 0.5F, 0.5F), aEvent.getFace(), aEvent.getPos() , false))).isSuccess()){
						aEvent.setCancellationResult(ActionResultType.FAIL);
						UT.Inventories.addStackToPlayerInventoryOrDrop(aEvent.getPlayer(), IL.Spray_Empty.get(1), aEvent.getWorld(), aEvent.getPos());
						return;
					}
				} else if (aStack.getItem() == Items.FLINT_AND_STEEL) {
					if (!aEvent.getWorld().isRemote && !UT.Entities.hasInfiniteItems(aEvent.getPlayer()) && RNGSUS.nextInt(100) >= mFlintChance) {
						aEvent.setCanceled(T);
						aStack.damageItem(1, aEvent.getPlayer(), (i -> {}));
						if (aStack.getDamage() >= aStack.getMaxDamage()) ST.use(aEvent.getPlayer(), aStack);
						return;
					}
					List<String> tChatReturn = new ArrayListNoNulls<>();
					long tDamage = IBlockToolable.Util.onToolClick(TOOL_igniter, aStack.getDamage()*10000, 1, aEvent.getPlayer(), tChatReturn, aEvent.getPlayer().inventory, aEvent.getPlayer().isCrouching(), aStack, aEvent.getWorld(), aEvent.getFace(), aEvent.getPos(), 0.5F, 0.5F, 0.5F);
					UT.Entities.sendchat(aEvent.getPlayer(), tChatReturn, F);
					if (tDamage > 0) {
						aEvent.setCanceled(T);
						UT.Sounds.send(aEvent.getWorld(), SFX.MC_IGNITE, 1.0F, 1.0F, aEvent.getPos().getX(), aEvent.getPos().getY(), aEvent.getPos().getZ());
						if (!UT.Entities.hasInfiniteItems(aEvent.getPlayer())) {
							aStack.damageItem(UT.Code.bindInt(UT.Code.units(tDamage, 10000, 1, T)), aEvent.getPlayer(), c -> {});
							if (aStack.getCount() >= aStack.getMaxDamage()) ST.use(aEvent.getPlayer(), aStack);
						}
						return;
					}
				} else if (aStack.getItem() == Items.STICK) {
					//TODO: See if this is still required Meta
					//if (!aEvent.getWorld().isRemote && aEvent.getPlayer().isCrouching() && MultiTileEntityRegistry.getRegistry("gt.multitileentity").getItem(32073).onItemUse(aEvent.getPlayer(), aEvent.world, aEvent.x, aEvent.y, aEvent.z, (byte)aEvent.face, 0.5F, 0.5F, 0.5F)) {
						//ST.use(aEvent.getPlayer(), aStack);
						//aEvent.setCanceled(T);
					//}
				} else if (aStack.getItem() == Items.FLINT) {
					//TODO: See if this is still required Meta
					//if (!aEvent.getWorld().isRemote && aEvent.getPlayer().isCrouching() && MultiTileEntityRegistry.getRegistry("gt.multitileentity").getItem(32074, ST.save(null, NBT_VALUE, ST.amount(1, aStack))).tryPlaceItemIntoWorld(aEvent.getPlayer(), aEvent.world, aEvent.x, aEvent.y, aEvent.z, (byte)aEvent.face, 0.5F, 0.5F, 0.5F)) {
						//ST.use(aEvent.getPlayer(), aStack);
						//aEvent.setCanceled(T);
					//}
				} else {
					if (!aEvent.getWorld().isRemote && aEvent.getPlayer().isCrouching()) {
						OreDictItemData tData = OM.anyassociation_(aStack);
						if (tData != null) {
							if (tData.mPrefix == OP.rockGt) {
								//TODO: See if this is still required Meta
								//if (MultiTileEntityRegistry.getRegistry("gt.multitileentity").getItem(32074, ST.save(null, NBT_VALUE, ST.amount(1, aStack))).tryPlaceItemIntoWorld(aEvent.getPlayer(), aEvent.world, aEvent.x, aEvent.y, aEvent.z, (byte)aEvent.face, 0.5F, 0.5F, 0.5F)) {
									//ST.use(aEvent.getPlayer(), aStack);
									//aEvent.setCanceled(T);
								//}
							}
							if (tData.mPrefix == OP.ingot) if (!MD.BOTA.mLoaded || tData.mMaterial.mMaterial.mOriginalMod != MD.BOTA || Blocks.BEACON != aEvent.getWorld().getBlockState(aEvent.getPos()).getBlock()) {
								//TODO: See if this is still required Meta
								//if (MultiTileEntityRegistry.getRegistry("gt.multitileentity").getItem(32084, ST.save(null, NBT_VALUE, ST.copy(aStack))).tryPlaceItemIntoWorld(aEvent.getPlayer(), aEvent.world, aEvent.x, aEvent.y, aEvent.z, (byte)aEvent.face, 0.5F, 0.5F, 0.5F)) {
									//ST.use(aEvent.getPlayer(), aStack, aStack.getCount());
									//aEvent.setCanceled(T);
								//}
							}
							if (tData.mPrefix == OP.plate) {
								//TODO: See if this is still required Meta
								//if (MultiTileEntityRegistry.getRegistry("gt.multitileentity").getItem(32085, ST.save(null, NBT_VALUE, ST.copy(aStack))).tryPlaceItemIntoWorld(aEvent.getPlayer(), aEvent.world, aEvent.x, aEvent.y, aEvent.z, (byte)aEvent.face, 0.5F, 0.5F, 0.5F)) {
									//ST.use(aEvent.getPlayer(), aStack, aStack.getCount());
									//aEvent.setCanceled(T);
								//}
							}
							if (tData.mPrefix == OP.plateGem) {
								//TODO: See if this is still required Meta
								//if (MultiTileEntityRegistry.getRegistry("gt.multitileentity").getItem(32086, ST.save(null, NBT_VALUE, ST.copy(aStack))).tryPlaceItemIntoWorld(aEvent.getPlayer(), aEvent.world, aEvent.x, aEvent.y, aEvent.z, (byte)aEvent.face, 0.5F, 0.5F, 0.5F)) {
									//ST.use(aEvent.getPlayer(), aStack, aStack.getCount());
									//aEvent.setCanceled(T);
								//}
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntitySpawningEvent(EntityJoinWorldEvent aEvent) {
		if (aEvent.getEntity() != null && !aEvent.getEntity().world.isRemote) {
			if (mSkeletonsShootGTArrows > 0 && aEvent.getEntity().getClass() == ArrowEntity.class && !OP.arrowGtWood.mRegisteredItems.isEmpty() && RNGSUS.nextInt(mSkeletonsShootGTArrows) == 0) {
				if (((ArrowEntity)aEvent.getEntity()).getShooter() instanceof SkeletonEntity) {
					aEvent.getEntity().world.addEntity(new EntityArrow_Material((ArrowEntity)aEvent.getEntity(), new ArrayListNoNulls<>(OP.arrowGtWood.mRegisteredItems).get(RNGSUS).toStack()));
					aEvent.getEntity().remove();
				}
			}
		}
	}

// Not gonna do that one due to exploitiness.
//  @SubscribeEvent
//  public void onItemExpireEvent(ItemExpireEvent aEvent) {
//      ItemStack aStack = aEvent.entityItem.getEntityItem();
//      if (aStack != null) {
//          if (aStack.stackSize <= 0) {aEvent.entityItem.setDead(); return;}
//          
//          if (!aEvent.entityItem.world.isRemote && aEvent.entityItem.onGround) {
//              if (aStack.getItem() == Items.stick) {
//                  MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry("gt.multitileentity");
//                  for (byte tSide : ALL_SIDES_MIDDLE_DOWN) {
//                      if (aEvent.entityItem.world.canPlaceEntityOnSide(tRegistry.mBlock, MathHelper.floor_double(aEvent.entityItem.posX)+OFFSETS_X[tSide], MathHelper.floor_double(aEvent.entityItem.posY)+OFFSETS_Y[tSide], MathHelper.floor_double(aEvent.entityItem.posZ)+OFFSETS_Z[tSide], F, SIDE_TOP, aEvent.entityItem, aStack)) {
//                          if (tRegistry.mBlock.placeBlock(aEvent.entityItem.world, MathHelper.floor_double(aEvent.entityItem.posX)+OFFSETS_X[tSide], MathHelper.floor_double(aEvent.entityItem.posY)+OFFSETS_Y[tSide], MathHelper.floor_double(aEvent.entityItem.posZ)+OFFSETS_Z[tSide], SIDE_UNKNOWN, (short)32756, null, T, F)) {
//                              aStack.stackSize--;
//                              if (aStack.stackSize <= 0) {aEvent.entityItem.setDead(); return;}
//                          }
//                      }
//                  }
//                  aEvent.entityItem.setEntityItemStack(aStack);
//                  aEvent.extraLife = 200;
//                  aEvent.setCanceled(T);
//                  return;
//              }
//              OreDictItemData tData = OM.data(aStack);
//              if (tData != null && tData.mPrefix == OP.rockGt) {
//                  MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry("gt.multitileentity");
//                  for (byte tSide : ALL_SIDES_MIDDLE_DOWN) {
//                      if (aEvent.entityItem.world.canPlaceEntityOnSide(tRegistry.mBlock, MathHelper.floor_double(aEvent.entityItem.posX)+OFFSETS_X[tSide], MathHelper.floor_double(aEvent.entityItem.posY)+OFFSETS_Y[tSide], MathHelper.floor_double(aEvent.entityItem.posZ)+OFFSETS_Z[tSide], F, SIDE_TOP, aEvent.entityItem, aStack)) {
//                          if (tRegistry.mBlock.placeBlock(aEvent.entityItem.world, MathHelper.floor_double(aEvent.entityItem.posX)+OFFSETS_X[tSide], MathHelper.floor_double(aEvent.entityItem.posY)+OFFSETS_Y[tSide], MathHelper.floor_double(aEvent.entityItem.posZ)+OFFSETS_Z[tSide], SIDE_UNKNOWN, (short)32757, ST.save(null, NBT_VALUE, aStack), T, F)) {
//                              aStack.stackSize = 0;
//                              aEvent.entityItem.setDead();
//                              return;
//                          }
//                      }
//                  }
//                  aEvent.entityItem.setEntityItemStack(aStack);
//                  aEvent.extraLife = 200;
//                  aEvent.setCanceled(T);
//                  return;
//              }
//          }
//      }
//  }
	
	@SubscribeEvent
	public void onEntityLivingDropsEventEvent(LivingDropsEvent aEvent) {
		if (aEvent.getEntity().world.isRemote || aEvent.getEntity() instanceof PlayerEntity || aEvent.getEntityLiving() == null) return;
		Override_Drops.handleDrops(aEvent.getEntityLiving(), UT.Reflection.getLowercaseClass(aEvent.getEntityLiving()), aEvent.getDrops(), aEvent.getLootingLevel(), aEvent.getEntity().isBurning(), aEvent.isRecentlyHit());
	}
	
	@SubscribeEvent
	public void onEntityLivingFallEvent(LivingFallEvent aEvent) {
		if (!aEvent.getEntity().world.isRemote && aEvent.getEntity() instanceof PlayerEntity) {
			//if (ST.equal(((PlayerEntity)aEvent.getEntity()).getHeldEquipment(), ToolsGT.sMetaTool, ToolsGT.SCISSORS) || ST.equal(((PlayerEntity)aEvent.getEntity()).getHeldEquipment(), ToolsGT.sMetaTool, ToolsGT.POCKET_SCISSORS)) aEvent.getDistance() *= 2;
		}
	}
	
	public ArrayListNoNulls<OcelotEntity> mOcelots = new ArrayListNoNulls<>();
	
	@SubscribeEvent
	public void onEntityConstructingEvent(EntityConstructing aEvent) {
		if (Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI) return;
		if (aEvent.getEntity() instanceof OcelotEntity) mOcelots.add(((OcelotEntity)aEvent.getEntity()));
	}
	
	@SubscribeEvent
	public void onServerTickEvent(TickEvent.ServerTickEvent aEvent) {
		if (aEvent.side.isServer() && aEvent.phase == TickEvent.Phase.START && SERVER_TIME > 20) {
			try {
				Iterator<OcelotEntity> tIterator = mOcelots.iterator();
				while (tIterator.hasNext()) {
					OcelotEntity tOcelot = tIterator.next();
					if (tOcelot != null && tOcelot.goalSelector != null) tOcelot.goalSelector.addGoal(3, new TemptGoal(tOcelot, 0.6D, Ingredient.fromItems(ItemsGT.CANS), T));
					tIterator.remove();
				}
				mOcelots.clear();
			} catch(Throwable e) {
				e.printStackTrace(ERR);
			}
		}
	}
	
	@SafeVarargs public final Fluid addAutogeneratedLiquid(OreDictMaterial aMaterial, Set<String>... aFluidList) {return FL.createLiquid(aMaterial, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedLiquid(OreDictMaterial aMaterial, IIconContainer aTexture, Set<String>... aFluidList) {return FL.createPlasma(aMaterial, aTexture, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedGas(OreDictMaterial aMaterial, Set<String>... aFluidList) {return FL.createGas(aMaterial, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedGas(OreDictMaterial aMaterial, IIconContainer aTexture, Set<String>... aFluidList) {return FL.createGas(aMaterial, aTexture, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedMolten(OreDictMaterial aMaterial, Set<String>... aFluidList) {return FL.createMolten(aMaterial, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedMolten(OreDictMaterial aMaterial, IIconContainer aTexture, Set<String>... aFluidList) {return FL.createMolten(aMaterial, aTexture, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedVapor(OreDictMaterial aMaterial, Set<String>... aFluidList) {return FL.createVapour(aMaterial, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedVaporized(OreDictMaterial aMaterial, IIconContainer aTexture, Set<String>... aFluidList) {return FL.createVapour(aMaterial, aTexture, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedPlasma(OreDictMaterial aMaterial, Set<String>... aFluidList) {return FL.createPlasma(aMaterial, aFluidList);}
	@SafeVarargs public final Fluid addAutogeneratedPlasma(OreDictMaterial aMaterial, IIconContainer aTexture, Set<String>... aFluidList) {return FL.createPlasma(aMaterial, aTexture, aFluidList);}
	@SafeVarargs public final Fluid addFluid(String aName, String aLocalized, OreDictMaterial aMaterial, int aState, long aAmountPerUnit, long aTemperatureK, Set<String>... aFluidList) {return FL.create(aName, aLocalized, aMaterial, aState, aAmountPerUnit, aTemperatureK, aFluidList);}    
	@SafeVarargs public final Fluid addFluid(String aName, String aLocalized, OreDictMaterial aMaterial, int aState, long aAmountPerUnit, long aTemperatureK, ItemStack aFullContainer, ItemStack aEmptyContainer, int aFluidAmount, Set<String>... aFluidList) {return FL.create(aName, aLocalized, aMaterial, aState, aAmountPerUnit, aTemperatureK, aFullContainer, aEmptyContainer, aFluidAmount, aFluidList);}
	@SafeVarargs public final Fluid addFluid(String aName, IIconContainer aTexture, String aLocalized, OreDictMaterial aMaterial, short[] aRGBa, int aState, long aAmountPerUnit, long aTemperatureK, ItemStack aFullContainer, ItemStack aEmptyContainer, int aFluidAmount, Set<String>... aFluidList) {return FL.create(aName, aTexture, aLocalized, aMaterial, aRGBa, aState, aAmountPerUnit, aTemperatureK, aFullContainer, aEmptyContainer, aFluidAmount, aFluidList);}
	
	public boolean downloadSupporterListSilverFromMain() {
		try {
			// Using http because Java screws up https on Windows at times.
			Scanner tScanner = new Scanner(new URL("http://gregtech.mechaenetia.com/com/gregoriust/gregtech/supporterlist.txt").openStream());
			while (tScanner.hasNextLine()) mSupporterListSilver.add(tScanner.nextLine().toLowerCase());
			tScanner.close();
			return mSupporterListSilver.size() > 3;
		} catch(Throwable e) {e.printStackTrace(DEB);}
		return F;
	}
	
	public boolean downloadSupporterListGoldFromMain() {
		try {
			// Using http because Java screws up https on Windows at times.
			Scanner tScanner = new Scanner(new URL("http://gregtech.mechaenetia.com/com/gregoriust/gregtech/supporterlistgold.txt").openStream());
			while (tScanner.hasNextLine()) mSupporterListGold.add(tScanner.nextLine().toLowerCase());
			tScanner.close();
			return mSupporterListGold.size() > 3;
		} catch(Throwable e) {e.printStackTrace(DEB);}
		return F;
	}
}
