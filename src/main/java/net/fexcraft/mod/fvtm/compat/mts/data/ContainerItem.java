package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.items.components.AItemBase;
import minecrafttransportsimulator.mcinterface.IBuilderItemInterface;
import net.minecraft.item.Item;

public class ContainerItem extends Item implements IBuilderItemInterface {
	
	private AItemBase base;

	public ContainerItem(ContainerPartItem conpart){
		super();
		this.base = conpart;
	}

	@Override
	public AItemBase getItem(){
		return base;
	}

}
