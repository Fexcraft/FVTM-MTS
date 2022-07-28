package net.fexcraft.mod.fvtm.compat.mts.data;

import mcinterface1122.InterfaceInterface;
import mcinterface1122.WrapperItemStack;
import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.IWrapperNBT;
import minecrafttransportsimulator.mcinterface.IWrapperPlayer;
import net.fexcraft.mod.fvtm.compat.mts.MTSCompat;
import net.minecraft.item.ItemStack;

public class ContainerPartItem extends AItemPart {

	private boolean single;
	
	public ContainerPartItem(ContainerJsonPart conpart, boolean single){
		super(conpart, "", MTSCompat.MODID);
		this.single = single;
	}

	@Override
	public APart createPart(AEntityF_Multipart<?> entity, IWrapperPlayer placingPlayer, JSONPartDefinition packVehicleDef, IWrapperNBT partData, APart parentPart) {
		return single ? new ContainerPart.Single(entity, placingPlayer, packVehicleDef, partData, parentPart) : new ContainerPart.Double(entity, placingPlayer, packVehicleDef, partData, parentPart);
	}
	
	@Override
	public WrapperItemStack getNewStack(IWrapperNBT data){
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
