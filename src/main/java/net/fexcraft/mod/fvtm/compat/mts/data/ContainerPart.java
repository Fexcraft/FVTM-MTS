package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import minecrafttransportsimulator.mcinterface.WrapperPlayer;
import minecrafttransportsimulator.rendering.instances.RenderPart;

public class ContainerPart extends APart {

	public ContainerPart(AEntityF_Multipart<?> entityOn, WrapperPlayer player, JSONPartDefinition placementDefinition, WrapperNBT data, APart parentPart){
		super(entityOn, player, placementDefinition, data, parentPart);
	}
	
	@Override
	public RenderPart getRenderer(){
		return new ContainerRenderPart();
	}

}
