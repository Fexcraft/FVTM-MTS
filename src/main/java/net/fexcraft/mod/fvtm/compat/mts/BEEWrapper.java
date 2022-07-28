package net.fexcraft.mod.fvtm.compat.mts;

import mcinterface1122.BuilderEntityExisting;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder.ContainerHolderWrapper;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
import net.fexcraft.mod.fvtm.data.container.ContainerType;
import net.fexcraft.mod.fvtm.util.Axes;
import net.minecraft.util.math.Vec3d;

public class BEEWrapper implements ContainerHolderWrapper {

	private BuilderEntityExisting entity;
	public EntityVehicleF_Physics ent;
	private Tracker tracker;
	private Axes axes;

	public BEEWrapper(BuilderEntityExisting entity, EntityVehicleF_Physics ent){
		this.entity = entity;
		this.ent = ent;
	}

	@Override
	public Vec3d getContainerSlotPosition(String slotid, ContainerHolder capability){
		ContainerSlot slot = capability.getContainerSlot(slotid);
		if(slot == null) return new Vec3d(0, 0, 0);
		Vec3f vec = new Vec3f(slot.position.x, slot.position.y, slot.position.z);
		axes.set_rotation(ent.rotation.angles.x, ent.rotation.angles.y + capability.getContainerSlot(slotid).rotation, ent.rotation.angles.z, true);
		vec = axes.get_vector(vec).add((float)ent.position.x, (float)ent.position.y, (float)ent.position.z);
		return new Vec3d(vec.x, vec.y, vec.z);
	}
	
	@Override
	public Vec3d getContainerInSlotPosition(String slotid, ContainerHolder capability, ContainerType type, int index){
		ContainerSlot slot = capability.getContainerSlot(slotid);
		if(slot == null) return new Vec3d(0, 0, 0);
        float off = index + (type.length() / 2f) - (slot.length / 2f);
		Vec3f vec = new Vec3f(slot.position.x - off, slot.position.y, slot.position.z);
		axes.set_rotation(ent.rotation.angles.x, ent.rotation.angles.y + capability.getContainerSlot(slotid).rotation, ent.rotation.angles.z, true);
		vec = axes.get_vector(vec).add((float)ent.position.x, (float)ent.position.y, (float)ent.position.z);
		return new Vec3d(vec.x, vec.y, vec.z);
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
