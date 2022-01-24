package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.baseclasses.Point3d;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.mcinterface.InterfaceClient;
import minecrafttransportsimulator.rendering.instances.RenderPart;
import net.fexcraft.mod.fvtm.compat.mts.CompatEvents;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.minecraft.entity.Entity;

public class ContainerRenderPart extends RenderPart {
	
	@Override
	public boolean disableRendering(APart part, float partialTicks){
		return super.disableRendering(part, partialTicks) || part.isFake() || part.isDisabled;
	}
	
	@Override
	public void adjustPositionRotation(APart part, Point3d entityPositionDelta, Point3d entityRotationDelta, float partialTicks){
		//Rotate the part according to its rendering rotation if we need to do so.
		entityRotationDelta.add(part.getRenderingRotation(partialTicks));
	}
	
	@Override
	protected void renderBoundingBoxes(APart part, Point3d entityPositionDelta){
		if(!part.entityOn.areVariablesBlocking(part.placementDefinition, InterfaceClient.getClientPlayer())){
			super.renderBoundingBoxes(part, entityPositionDelta);
		}
	}
	
	@Override
	protected void renderModel(APart entity, boolean blendingEnabled, float partialTicks){
		entity.world.beginProfiling("LightStateUpdates", true);
        entity.updateLightBrightness(partialTicks);
        entity.world.beginProfiling("RenderingMainModel", false);
        Entity ent = CompatEvents.EXISTING_CLIENT.get(entity);
        if(ent != null) ent.getCapability(Capabilities.CONTAINER, null).render(0, 0, 0, 0, 0, 0);
	}

}
