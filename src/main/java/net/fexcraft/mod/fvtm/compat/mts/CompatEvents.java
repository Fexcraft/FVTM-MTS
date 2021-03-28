package net.fexcraft.mod.fvtm.compat.mts;

import java.util.ArrayList;
import java.util.HashMap;

import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.BuilderEntity;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.data.container.ContainerData;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
import net.fexcraft.mod.fvtm.util.Resources;
import net.fexcraft.mod.fvtm.util.caps.ContainerHolderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CompatEvents {
	
	public static HashMap<BuilderEntity, BEWrapper> wrappers = new HashMap<>();
	public static ArrayList<BuilderEntity> tracked = new ArrayList<>();
	//we'll track them till we're sure
	
	public CompatEvents(){}
	
	@SubscribeEvent
	public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event){
		if(event.getObject() instanceof BuilderEntity){
			tracked.add((BuilderEntity)event.getObject());
			event.addCapability(new ResourceLocation("fvtm:container"), new ContainerHolderUtil(event.getObject()));
		}
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent event){
		if(event.phase == Phase.START) return;
		tracked.removeIf(entity -> {
			if(entity.entity != null){
				if(entity.entity instanceof EntityVehicleF_Physics){
					EntityVehicleF_Physics ent = (EntityVehicleF_Physics)entity.entity;
					for(JSONPartDefinition part : ent.definition.parts){
						for(String str : part.types){
							if(str.contains("fvtm:container")){
								includeContainer(entity, part, str);
								break;
							}
						}
					}
				}
				return true;
			}
			return false;
		});
	}

	private void includeContainer(BuilderEntity entity, JSONPartDefinition part, String str){
		BEWrapper wrapper = wrappers.get(entity);
		if(wrapper == null){
			wrapper = new BEWrapper(entity);
			wrappers.put(entity, wrapper);
		}
		ContainerHolder holder = wrapper.getCapability();
		if(holder.getWrapper() == null) holder.setWrapper(wrapper);
		String[] info = str.split(":");
		byte length = info.length > 2 ? Byte.parseByte(info[2]) : 6;
		Vec3d pos = new Vec3d(part.pos.x, part.pos.y, part.pos.z);
		float rot = info.length > 3 ? Integer.parseInt(info[3]) : 0;
		ContainerSlot slot = new ContainerSlot("generic_mts_" + holder.getContainerSlots().length, length, pos, rot, null, null);
		slot.setContainer(0, new ContainerData(Resources.CONTAINERS.getValue(new ResourceLocation("hcp:medium"))));
		holder.addContainerSlot(slot);
		Print.log("Included ContainerSlot(" + length + ") into " + entity);
		holder.sync(entity.world.isRemote);
	}

}
