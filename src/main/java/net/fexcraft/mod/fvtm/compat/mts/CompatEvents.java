package net.fexcraft.mod.fvtm.compat.mts;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import mcinterface1122.BuilderEntityExisting;
import mcinterface1122.InterfaceInterface;
import mcinterface1122.WrapperEntity;
import minecrafttransportsimulator.entities.components.AEntityB_Existing;
import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.compat.mts.data.ContainerPart;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
import net.fexcraft.mod.fvtm.util.caps.ContainerHolderUtil;
import net.fexcraft.mod.fvtm.util.caps.ContainerHolderUtil.Implementation;
import net.fexcraft.mod.fvtm.util.caps.RenderCacheHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CompatEvents {

	//we'll track them till we're sure
	public static ConcurrentLinkedQueue<BuilderEntityExisting> tracked = new ConcurrentLinkedQueue<>();
	public static ConcurrentLinkedQueue<AEntityF_Multipart<?>> cotracked = new ConcurrentLinkedQueue<>();
	public static ConcurrentHashMap<BuilderEntityExisting, BEEWrapper> bee_wrappers = new ConcurrentHashMap<>();
	
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
		if(event.phase == Phase.START || event.world.isRemote) return;
		tracked.removeIf(entity -> {
			AEntityB_Existing ent = InterfaceInterface.toInternal(entity);
			if(ent != null && ent instanceof EntityVehicleF_Physics){
				EntityVehicleF_Physics entf = (EntityVehicleF_Physics)ent;
				if(bee_wrappers.contains(entity)) return true;
				BEEWrapper wrapper = new BEEWrapper(entity, (EntityVehicleF_Physics)InterfaceInterface.toInternal(entity));
				bee_wrappers.put(entity, wrapper);
				int found = 0;
				for(APart part : entf.parts){
					if(part instanceof ContainerPart){
						if(includeContainer(wrapper, (ContainerPart)part, found)) found++;
					}
				}
				return true;
			}
			return false;
		});
		cotracked.removeIf(entity -> {
			BuilderEntityExisting ent = InterfaceInterface.toExternal(entity);
			if(ent != null){
				tracked.add(ent);
				return true;
			}
			return false;
		});
	}

	private boolean includeContainer(BEEWrapper wrapper, ContainerPart part, int found){
		ContainerHolder holder = wrapper.getCapability();
		ContainerHolderUtil.Implementation impl = (Implementation)holder;
		if(impl.setup) impl.setup = false;
		byte length = (byte)part.size();
		String slotid = "container_" + found;
		Vec3d pos = new Vec3d(part.localOffset.x, part.localOffset.y, part.localOffset.z);
		ContainerSlot slot = new ContainerSlot(slotid, length, pos, 90, null, null);
		//slot.setContainer(0, new ContainerData(Resources.CONTAINERS.getValue(new ResourceLocation("hcp:medium"))));
		holder.addContainerSlot(slot);
		Print.log("Included ContainerSlot(" + length + ") into " + wrapper.getEntity());
		holder.setWrapper(wrapper);
		holder.sync(wrapper.getEntity().world.isRemote);
		Print.debug(wrapper.getEntity().world.isRemote, wrapper.getTracker());
		if(wrapper.getTracker() == null){
			wrapper.setTracker(new Tracker(wrapper));
			wrapper.getEntity().world.spawnEntity(wrapper.getTracker());
		}
		impl.setup = true;
		return true;
	}

	public static BEEWrapper getWrapper(BuilderEntityExisting entity){
		if(!bee_wrappers.contains(entity)){
			bee_wrappers.put(entity, new BEEWrapper(entity, (EntityVehicleF_Physics)InterfaceInterface.toInternal(entity)));
		}
		return bee_wrappers.get(entity);
	}
	
	private static Field entfield;
	private static boolean entfailed = false;
	
	public static Map<Entity, WrapperEntity> getWrapperEntities(){
		if(entfield == null && !entfailed){
			try{
				entfield = WrapperEntity.class.getDeclaredField("entityWrappers");
				entfield.setAccessible(true);
			}
			catch(Exception e){
				Print.log("Failed to get field. [ENTFIELD:ERR:0]");
				e.printStackTrace();
				entfailed = true;
			}
		}
		try{
			return (Map<Entity, WrapperEntity>)entfield.get(null);
		}
		catch(IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
			return null;
		}
	}

}
