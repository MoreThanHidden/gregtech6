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

package gregapi.compat;

import java.util.Collection;

import gregapi.code.ArrayListNoNulls;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;


public interface ICompat {
	Collection<ICompat> COMPAT_CLASSES = new ArrayListNoNulls<>();
	
	default void onCommonSetup       (FMLCommonSetupEvent aEvent){}
	default void onLoadComplete      (FMLLoadCompleteEvent aEvent){}
	default void onServerStarting    (FMLServerStartingEvent aEvent){}
	default void onServerStarted     (FMLServerStartedEvent aEvent){}
	default void onServerStopping    (FMLServerStoppingEvent aEvent){}
	default void onServerStopped     (FMLServerStoppedEvent aEvent){}
	default void onIDChanging        (FMLModIdMappingEvent aEvent){}
}
