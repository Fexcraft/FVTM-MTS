package net.fexcraft.mod.fvtm.compat.mts;

import minecrafttransportsimulator.baseclasses.Point3d;
import minecrafttransportsimulator.mcinterface.BuilderEntity;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder.ContainerHolderWrapper;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
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
		ContainerSlot slot = capability.getContainerSlot(slotid);
		if(slot == null) return new Vec3d(0, 0, 0);
		Point3d point = new Point3d(slot.position.x, slot.position.y, slot.position.z);
		point.rotateFine(entity.entity.angles);
		point.add(entity.entity.position);
		return new Vec3d(point.x, point.y, point.z);
	}
	
	@Override
	public Vec3d getContainerInSlotPosition(String slotid, ContainerHolder capability, ContainerType type, int index){
		ContainerSlot slot = capability.getContainerSlot(slotid);
		if(slot == null) return new Vec3d(0, 0, 0);
        float off = index + (type.length() / 2f) - (slot.length / 2f);
		Point3d point = new Point3d(slot.position.x - off, slot.position.y, slot.position.z);
		point.rotateFine(entity.entity.angles);
		point.add(entity.entity.position);
		return new Vec3d(point.x, point.y, point.z);
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
