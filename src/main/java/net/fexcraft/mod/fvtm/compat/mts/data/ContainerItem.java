package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.mcinterface.BuilderItem;
import net.fexcraft.mod.fvtm.InternalAddon;

public class ContainerItem extends BuilderItem {

	public ContainerItem(ContainerPartItem conpart){
		super(conpart);
		setCreativeTab(InternalAddon.INSTANCE.getDefaultCreativeTab());
	}

}
