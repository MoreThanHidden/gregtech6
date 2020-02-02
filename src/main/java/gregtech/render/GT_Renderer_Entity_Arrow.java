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

package gregtech.render;

import static gregapi.data.CS.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class GT_Renderer_Entity_Arrow extends ArrowRenderer implements IRenderFactory {
	private final ResourceLocation mTexture;
	
	public GT_Renderer_Entity_Arrow(EntityType aArrowClass, String aTextureName) {
		super(Minecraft.getInstance().getRenderManager());
		mTexture = new ResourceLocation(RES_PATH_ENTITY+aTextureName);
		RenderingRegistry.registerEntityRenderingHandler(aArrowClass, this);
	}

	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return mTexture;
	}

	@Override
	public EntityRenderer createRenderFor(EntityRendererManager manager) {
		return this;
	}
}
