package net.fexcraft.mod.fvtm.compat.mts;

import minecrafttransportsimulator.mcinterface.BuilderEntity;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder.ContainerHolderWrapper;
import net.fexcraft.mod.fvtm.data.container.ContainerType;
import net.minecraft.util.math.Vec3d;

public class BEWrapper implements ContainerHolderWrapper {

	private BuilderEntity entity;
	private Tracker tracker;

	public BEWrapper(BuilderEntity entity){
		this.entity = entity;
	}

	@Override
	public Vec3d getContainerSlotPosition(String slotid, ContainerHolder capability){
		return null;
	}

	@Override
	public Vec3d getContainerInSlotPosition(String slot, ContainerHolder capability, ContainerType type, int index){
		// TODO Auto-generated method stub
		return null;
	}

	public ContainerHolder getCapability(){
		return entity.getCapability(Capabilities.CONTAINER, null);
	}

	public Tracker getTracker(){
		return tracker;
	}
	
	public BEWrapper setTracker(Tracker inst){
		tracker = inst;
		return this;
	}

	public BuilderEntity getEntity(){
		return entity;
	}

}
