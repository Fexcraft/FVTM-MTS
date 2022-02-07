package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.InterfaceInterface;
import minecrafttransportsimulator.mcinterface.WrapperItemStack;
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
		//partData.setString("packID", MTSCompat.MODID);
		//partData.setString("systemName", "container_holder_" + (single ? "single" : "double"));
		//partData.setString("subName", "");
		return single ? new ContainerPart.Single(entity, placingPlayer, packVehicleDef, partData, parentPart) : new ContainerPart.Double(entity, placingPlayer, packVehicleDef, partData, parentPart);
	}
	
	@Override
	public WrapperItemStack getNewStack(WrapperNBT data){
		WrapperItemStack stack = InterfaceInterface.toInternal(new ItemStack(single ? MTSCompat.CON_SINGLE : MTSCompat.CON_DOUBLE));
		data.setString("packID", MTSCompat.MODID);
		data.setString("systemName", "container_holder_" + (single ? "single" : "double"));
		data.setString("subName", "");
		stack.setData(data);
		return stack;
	}
	
	@Override
	public boolean autoGenerate(){
		return false;
	}

}
