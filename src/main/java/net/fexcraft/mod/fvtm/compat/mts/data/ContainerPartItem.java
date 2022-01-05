package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.entities.components.AEntityE_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import minecrafttransportsimulator.mcinterface.WrapperPlayer;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.mod.fvtm.compat.mts.MTSCompat;
import net.minecraft.item.ItemStack;

public class ContainerPartItem extends AItemPart {

	private boolean single;
	
	public ContainerPartItem(ContainerJsonPart conpart, boolean single){
		super(conpart, "", MTSCompat.MODID);
		this.single = single;
	}

	@Override
	public APart createPart(AEntityE_Multipart<?> entity, WrapperPlayer placingPlayer, JSONPartDefinition packVehicleDef, WrapperNBT partData, APart parentPart){
		return new ContainerPart(entity, placingPlayer, packVehicleDef, partData, parentPart);
	}
	
	@Override
	public ItemStack getNewStack(){
		return new ItemStack(FCLRegistry.getItem("fvtm_mts:container_" + (single ? "single" : "double")));
	}
	
	@Override
	public boolean autoGenerate(){
		return false;
	}

}
