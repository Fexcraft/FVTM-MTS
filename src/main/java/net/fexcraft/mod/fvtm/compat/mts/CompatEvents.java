package net.fexcraft.mod.fvtm.compat.mts;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mojang.realmsclient.util.Pair;

import mcinterface1122.BuilderEntityExisting;
import mcinterface1122.InterfaceInterface;
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
	public static ConcurrentLinkedQueue<Pair<BuilderEntityExisting, AEntityF_Multipart<?>>> tracked = new ConcurrentLinkedQueue<>();
	public static ConcurrentLinkedQueue<AEntityF_Multipart<?>> cotracked = new ConcurrentLinkedQueue<>();
	public static ConcurrentHashMap<BuilderEntityExisting, BEEWrapper> bee_wrappers = new ConcurrentHashMap<>();
	private static final ArrayList<Pair<BuilderEntityExisting, AEntityF_Multipart<?>>> torem = new ArrayList<>();
	private static final ArrayList<AEntityF_Multipart<?>> cotorem = new ArrayList<>();
	
	public CompatEvents(){}
	
	@SubscribeEvent
	public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event){
		if(event.getObject().world == null) return;
		if(event.getObject() instanceof BuilderEntityExisting){
			event.addCapability(new ResourceLocation("fvtm:container"), new ContainerHolderUtil(event.getObject()));
			//if(!event.getObject().world.isRemote) tracked.add((BuilderEntityExisting)event.getObject());
			if(event.getObject().world.isRemote){
				event.addCapability(new ResourceLocation("fvtm:rendercache"), new RenderCacheHandler());
			}
		}
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent event){
		if(event.phase == Phase.START || event.world.isRemote) return;
		if(tracked.size() > 0){
			//Print.debug(tracked);
			//Print.debug(cotracked);
			for(Pair<BuilderEntityExisting, AEntityF_Multipart<?>> pair : tracked){
				if(pair.first().isDead){
					torem.add(pair);
					continue;
				}
				if(pair.second() instanceof EntityVehicleF_Physics == false){
					torem.add(pair);
					continue;
				}
				EntityVehicleF_Physics entf = (EntityVehicleF_Physics)pair.second();
				BEEWrapper wrapper = getWrapper(pair.first());
				int found = 0;
				for(APart part : entf.parts){
					if(part instanceof ContainerPart){
						if(includeContainer(wrapper, (ContainerPart)part, found)) found++;
					}
				}
				torem.add(pair);
			}
			//Print.debug(torem);
			//Print.debug(bee_wrappers);
			//Print.debug("=====");
			tracked.removeAll(torem);
			torem.clear();
		}
		if(cotracked.size() > 0){
			for(AEntityF_Multipart<?> entity : cotracked){
				BuilderEntityExisting ent = InterfaceInterface.toExternal(entity);
				if(ent != null){
					tracked.add(Pair.of(ent, entity));
					cotorem.add(entity);
				}
			}
			cotracked.removeAll(cotorem);
			cotorem.clear();
		}
	}

	private boolean includeContainer(BEEWrapper wrapper, ContainerPart part, int found){
		ContainerHolder holder = wrapper.getCapability();
		byte length = (byte)part.size();
		String slotid = "container_" + found;
		if(holder.getContainerSlot(slotid) != null){
			Print.log("ContainerSlot(" + length + "/" + slotid + ") present in " + wrapper.getEntity() + ", skipping.");
			checkTracker(wrapper, holder);
			return true;
		}
		ContainerHolderUtil.Implementation impl = (Implementation)holder;
		if(impl.setup) impl.setup = false;
		Vec3d pos = new Vec3d(part.localOffset.x, part.localOffset.y, part.localOffset.z);
		ContainerSlot slot = new ContainerSlot(slotid, length, pos, 90, null, null);
		//slot.setContainer(0, new ContainerData(Resources.CONTAINERS.getValue(new ResourceLocation("hcp:medium"))));
		holder.addContainerSlot(slot);
		Print.log("Included ContainerSlot(" + length + "/" + slotid + ") into " + wrapper.getEntity());
		checkTracker(wrapper, holder);
		impl.setup = true;
		return true;
	}

	private void checkTracker(BEEWrapper wrapper, ContainerHolder holder){
		if(wrapper.getTracker() == null){
			wrapper.setTracker(new Tracker(wrapper));
			wrapper.getEntity().world.spawnEntity(wrapper.getTracker());
		}
		holder.setWrapper(wrapper);
		holder.sync(false);
	}

	public static BEEWrapper getWrapper(BuilderEntityExisting entity){
		if(!bee_wrappers.containsKey(entity)){
			bee_wrappers.put(entity, new BEEWrapper(entity, (EntityVehicleF_Physics)InterfaceInterface.toInternal(entity)));
		}
		return bee_wrappers.get(entity);
	}

	/*public static BEEWrapper getWrapper(AEntityF_Multipart<?> entity){
		for(BEEWrapper wrapper : bee_wrappers.values()){
			if(wrapper.ent == entity) return wrapper;
		}
		return getWrapper(InterfaceInterface.toExternal(entity));
	}*/

}
