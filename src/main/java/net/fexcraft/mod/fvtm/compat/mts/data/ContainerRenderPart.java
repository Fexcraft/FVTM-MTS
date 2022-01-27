package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.baseclasses.Point3d;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.mcinterface.InterfaceClient;
import minecrafttransportsimulator.rendering.instances.RenderPart;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;

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
		ContainerPart part = (ContainerPart)entity;
		if(part.con_entity != null){
			ContainerHolder holder = part.con_entity.getCapability(Capabilities.CONTAINER, null);
			if(holder != null) holder.render(0, 0, 0, 0, 0, 0);
		}
	}

}
