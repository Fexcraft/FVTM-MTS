package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.baseclasses.Point3d;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.mcinterface.InterfaceClient;
import minecrafttransportsimulator.rendering.instances.RenderPart;

public class ContainerRenderPart extends RenderPart {
	
	@Override
	public boolean disableRendering(APart part, float partialTicks){
		return true;
	}
	
	@Override
	public void adjustPositionRotation(APart part, Point3d entityPositionDelta, Point3d entityRotationDelta, float partialTicks){
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
		//
	}

}
