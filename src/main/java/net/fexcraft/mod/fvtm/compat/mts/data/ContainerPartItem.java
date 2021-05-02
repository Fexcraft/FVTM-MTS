package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.entities.components.AEntityE_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.jsondefs.JSONPart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import net.fexcraft.mod.fvtm.compat.mts.MTSCompat;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerPartItem extends AItemPart {

	public static ContainerPartItem INSTANCE = new ContainerPartItem();

	public ContainerPartItem(){
		super(new JSONPart(){}, "", MTSCompat.MODID);
	}

	@Override
	public APart createPart(AEntityE_Multipart<?> entity, JSONPartDefinition packVehicleDef, WrapperNBT partData, APart parentPart){
		return new ContainerPart(entity, packVehicleDef, partData, parentPart);
	}
	
	public static class Creator extends AItemPartCreator {

		@Override
		public boolean isCreatorValid(JSONPart definition){
			return false;
		}

		@Override
		public AItemPart createItem(JSONPart definition, String subName, String sourcePackID){
			return INSTANCE;
		}
		
	}
	
	@Override
	public ItemStack getNewStack(){
		return new ItemStack(Items.STONE_AXE);//MTSCompat.ITEM);
	}

}
