package net.fexcraft.mod.fvtm.compat.mts;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.mcinterface.BuilderEntityExisting;
import minecrafttransportsimulator.mcinterface.WrapperEntity;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.compat.mts.data.ContainerPart;
import net.fexcraft.mod.fvtm.data.container.ContainerData;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
import net.fexcraft.mod.fvtm.util.Resources;
import net.fexcraft.mod.fvtm.util.caps.ContainerHolderUtil;
import net.fexcraft.mod.fvtm.util.caps.RenderCacheHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CompatEvents {
	
	public static ConcurrentLinkedQueue<BuilderEntityExisting> tracked = new ConcurrentLinkedQueue<>();
	public static ConcurrentHashMap<BuilderEntityExisting, BEEWrapper> bee_wrappers = new ConcurrentHashMap<>();
	//we'll track them till we're sure
	
	public CompatEvents(){}
	
	@SubscribeEvent
	public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event){
		if(event.getObject().world == null) return;
		if(event.getObject() instanceof BuilderEntityExisting){
			tracked.add((BuilderEntityExisting)event.getObject());
			event.addCapability(new ResourceLocation("fvtm:container"), new ContainerHolderUtil(event.getObject()));
			if(event.getObject().world.isRemote){
				event.addCapability(new ResourceLocation("fvtm:rendercache"), new RenderCacheHandler());
			}
		}
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent event){
		if(event.phase == Phase.START) return;
		tracked.removeIf(entity -> {
			WrapperEntity ent = WrapperEntity.getWrapperFor(entity);
			if(ent.getBaseEntity() != null){
				if(ent.getBaseEntity() instanceof EntityVehicleF_Physics){
					EntityVehicleF_Physics entf = (EntityVehicleF_Physics)ent.getBaseEntity();
					BEEWrapper wrapper = getWrapper(entity);
					int found = 0;
					for(APart part : entf.parts){
						if(part instanceof ContainerPart){
							ContainerPart conpart = (ContainerPart) part;
							conpart.con_entity = entity;
							if(includeContainer(wrapper, conpart, found)) found++;
						}
					}
				}
				return true;
			}
			return false;
		});
	}

	private boolean includeContainer(BEEWrapper wrapper, ContainerPart part, int found){
		ContainerHolder holder = wrapper.getCapability();
		byte length = (byte) part.size;
		String slotid = "container_" + found;
		Vec3d pos = new Vec3d(part.localOffset.x, part.localOffset.y, part.localOffset.z);
		ContainerSlot slot = new ContainerSlot(slotid, length, pos.add(-.375, 0, -.375), 0, null, null);
		slot.setContainer(0, new ContainerData(Resources.CONTAINERS.getValue(new ResourceLocation("hcp:medium"))));
		holder.addContainerSlot(slot);
		Print.log("Included ContainerSlot(" + length + ") into " + wrapper);
		holder.sync(wrapper.getEntity().world.isRemote);
		if(!wrapper.getEntity().world.isRemote && wrapper.getTracker() == null){
			wrapper.setTracker(new Tracker(wrapper));
			wrapper.getEntity().world.spawnEntity(wrapper.getTracker());
		}
		return true;
	}

	public static void add(ContainerPart part, AEntityF_Multipart<?> entity){
		for(BEEWrapper ent : bee_wrappers.values()){
			if(ent.ent == entity){
				part.con_entity = ent.getEntity();
				break;
			}
		}
	}

	public static BEEWrapper getWrapper(BuilderEntityExisting entity){
		if(!bee_wrappers.contains(entity)){
			WrapperEntity ent = WrapperEntity.getWrapperFor(entity);
			bee_wrappers.put(entity, new BEEWrapper(entity, (EntityVehicleF_Physics)ent.getBaseEntity()));
		}
		return bee_wrappers.get(entity);
	}

}
