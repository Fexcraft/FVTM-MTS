package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import minecrafttransportsimulator.mcinterface.WrapperPlayer;
import net.fexcraft.mod.fvtm.compat.mts.MTSCompat;
import net.minecraft.item.ItemStack;

public class ContainerPartItem extends AItemPart {

	private boolean single;
	
	public ContainerPartItem(ContainerJsonPart conpart, boolean single){
		super(conpart, "", MTSCompat.MODID);
		this.single = single;
	}

	@Override
	public APart createPart(AEntityF_Multipart<?> entity, WrapperPlayer placingPlayer, JSONPartDefinition packVehicleDef, WrapperNBT partData, APart parentPart){
		partData.setString("packID", MTSCompat.MODID);
		partData.setString("systemName", "container_holder_" + (single ? "single" : "double"));
		partData.setString("subName", "");
		partData.setInteger("fvtm_mts_size", single ? 6 : 12);
		return new ContainerPart(entity, placingPlayer, packVehicleDef, partData, parentPart);
	}
	
	@Override
	public ItemStack getNewStack(){
		return new ItemStack(single ? MTSCompat.CON_SINGLE : MTSCompat.CON_DOUBLE);
	}
	
	@Override
	public boolean autoGenerate(){
		return false;
	}

}
