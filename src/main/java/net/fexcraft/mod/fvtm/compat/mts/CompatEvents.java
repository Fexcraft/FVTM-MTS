package net.fexcraft.mod.fvtm.compat.mts;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.BuilderEntityExisting;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
import net.fexcraft.mod.fvtm.item.ContainerItem;
import net.fexcraft.mod.fvtm.util.caps.ContainerHolderUtil;
import net.fexcraft.mod.fvtm.util.caps.RenderCacheHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CompatEvents {
	
	public static HashMap<BuilderEntityExisting, BEWrapper> wrappers = new HashMap<>();
	public static ConcurrentLinkedQueue<BuilderEntityExisting> tracked = new ConcurrentLinkedQueue<>();
	//we'll track them till we're sure
	
	public static String SPECIFIC = "fvtm_container", CON_SINGLE = "container_single", CON_DOUBLE = "container_double", CON_EXTENDED = "cotainer_extended";
	
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
			if(entity.entity != null){
				if(entity.entity instanceof EntityVehicleF_Physics){
					EntityVehicleF_Physics ent = (EntityVehicleF_Physics)entity.entity;
					int found = 0;
					for(JSONPartDefinition part : ent.definition.parts){
						for(String str : part.types){
							if(str.endsWith(CON_SINGLE) || str.endsWith(CON_DOUBLE) || str.endsWith(CON_EXTENDED) || str.startsWith(SPECIFIC)){
								if(includeContainer(entity, part, str, found)) found++;
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

	private boolean includeContainer(BuilderEntityExisting entity, JSONPartDefinition part, String str, int found){
		BEWrapper wrapper = wrappers.get(entity);
		if(wrapper == null){
			wrapper = new BEWrapper(entity);
			wrappers.put(entity, wrapper);
		}
		ContainerHolder holder = wrapper.getCapability();
		if(holder.getWrapper() == null) holder.setWrapper(wrapper);
		boolean specific = str.startsWith(SPECIFIC);
		byte length = 6;
		String slotid;
		float rot;
		if(specific){
			String[] info = str.split(":");
			slotid = info[1];
			length = info.length > 2 ? Byte.parseByte(info[2]) : 6;
			rot = info.length > 3 ? Integer.parseInt(info[3]) : 0;
		}
		else{
			if(str.endsWith(CON_SINGLE)) length = 6;
			if(str.endsWith(CON_DOUBLE)) length = 12;
			if(str.endsWith(CON_EXTENDED)) length = 14;
			slotid = "container_" + found;
			rot = 0;
		}
		Vec3d pos = new Vec3d(part.pos.x, part.pos.y, part.pos.z);
		ContainerSlot slot = new ContainerSlot(slotid, length, pos.add(.375, 0, -.375), rot, null, null);
		//slot.setContainer(0, new ContainerData(Resources.CONTAINERS.getValue(new ResourceLocation("hcp:medium"))));
		holder.addContainerSlot(slot);
		Print.log("Included ContainerSlot(" + length + ") into " + entity);
		holder.sync(entity.world.isRemote);
		if(!wrapper.getEntity().world.isRemote && wrapper.getTracker() == null){
			wrapper.setTracker(new Tracker(wrapper, wrapper.getEntity().entity.position));
			wrapper.getEntity().world.spawnEntity(wrapper.getTracker());
		}
		return !specific;
	}

	public static BEWrapper getWrapper(BuilderEntityExisting entity){
		return wrappers.get(entity);
	}
	
	@SubscribeEvent
	public void onInteract(EntityInteractSpecific event){
		if(event.getSide().isClient()) return;
		Print.debug(event.getEntity());
		if((event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ContainerItem || event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemTool) && event.getTarget() instanceof BuilderEntityExisting){
			BuilderEntityExisting ent = (BuilderEntityExisting)event.getTarget();
			if(ent.entity == null) return;
			BEWrapper wrapper = wrappers.get(ent);
			if(wrapper == null || wrapper.getCapability().getContainerSlots().length == 0) return;
			wrapper.getCapability().openGUI(event.getEntityPlayer());
			event.setCanceled(true);
		}
	}

}
