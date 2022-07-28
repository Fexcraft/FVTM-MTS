package net.fexcraft.mod.fvtm.compat.mts;

import mcinterface1122.BuilderEntityExisting;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder.ContainerHolderWrapper;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
import net.fexcraft.mod.fvtm.data.container.ContainerType;
import net.minecraft.util.math.Vec3d;

public class BEEWrapper implements ContainerHolderWrapper {

	private BuilderEntityExisting entity;
	public EntityVehicleF_Physics ent;
	private Tracker tracker;

	public BEEWrapper(BuilderEntityExisting entity, EntityVehicleF_Physics ent){
		this.entity = entity;
		this.ent = ent;
	}

	@Override
	public Vec3d getContainerSlotPosition(String slotid, ContainerHolder capability){
		ContainerSlot slot = capability.getContainerSlot(slotid);
		if(slot == null) return new Vec3d(0, 0, 0);
		Point3D point = new Point3D(slot.position.x, slot.position.y, slot.position.z);
		point.rotate(ent.rotation);
		point.rotateY(capability.getContainerSlot(slotid).rotation);
		point.add(ent.position);
		return new Vec3d(point.x, point.y, point.z);
	}
	
	@Override
	public Vec3d getContainerInSlotPosition(String slotid, ContainerHolder capability, ContainerType type, int index){
		ContainerSlot slot = capability.getContainerSlot(slotid);
		if(slot == null) return new Vec3d(0, 0, 0);
        float off = index + (type.length() / 2f) - (slot.length / 2f);
		Point3D point = new Point3D(slot.position.x - off, slot.position.y, slot.position.z);
		point.rotate(ent.rotation);
		point.rotateY(capability.getContainerSlot(slotid).rotation);
		point.add(ent.position);
		return new Vec3d(point.x, point.y, point.z);
	}

	public ContainerHolder getCapability(){
		return entity.getCapability(Capabilities.CONTAINER, null);
	}

	public Tracker getTracker(){
		return tracker;
	}
	
	public BEEWrapper setTracker(Tracker inst){
		tracker = inst;
		return this;
	}

	public BuilderEntityExisting getEntity(){
		return entity;
	}

}
