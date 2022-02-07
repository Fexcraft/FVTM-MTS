package net.fexcraft.mod.fvtm.compat.mts.data;

import java.lang.reflect.Field;

import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.InterfaceInterface;
import minecrafttransportsimulator.mcinterface.WrapperEntity;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import minecrafttransportsimulator.mcinterface.WrapperPlayer;
import minecrafttransportsimulator.rendering.instances.RenderPart;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.compat.mts.CompatEvents;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ContainerPart extends APart {

	public ContainerPart(AEntityF_Multipart<?> entityOn, WrapperPlayer player, JSONPartDefinition placementDefinition, WrapperNBT data, APart parentPart){
		super(entityOn, player, placementDefinition, data, parentPart);
		CompatEvents.cotracked.add(entityOn);
	}
	
	@Override
	public RenderPart getRenderer(){
		return new ContainerRenderPart();
	}
	
	@Override
	public void remove(){
		super.remove();
	}
	
	@Override
	public boolean interact(WrapperPlayer player){
		Entity entity = InterfaceInterface.toExternal(entityOn);
		ContainerHolder holder = entity.getCapability(Capabilities.CONTAINER, null);
		if(holder == null){
			Print.chat(getEntity(player), "Container Holder is Null, this seems to be an error.");
			return true;
		}
		holder.openGUI((EntityPlayer)getEntity(player));
		return true;
	}
	
	/*@Override
	public WrapperNBT save(WrapperNBT data){
		super.save(data);
		return data;
	}*/
	
	private static Field entfield;
	private static boolean entfailed = false;
	
	public static Entity getEntity(WrapperEntity ent){
		if(entfield == null && !entfailed){
			try{
				entfield = WrapperEntity.class.getDeclaredField("entity");
				entfield.setAccessible(true);
			}
			catch(Exception e){
				Print.log("Failed to get field. [ENTFIELD:ERR:0]");
				e.printStackTrace();
				entfailed = true;
			}
		}
		try{
			return (Entity)entfield.get(ent);
		}
		catch(IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public abstract int size();
	
	public static class Single extends ContainerPart {

		public Single(AEntityF_Multipart<?> entityOn, WrapperPlayer player, JSONPartDefinition placementDefinition, WrapperNBT data, APart parentPart){
			super(entityOn, player, placementDefinition, data, parentPart);
		}

		@Override
		public int size(){
			return 6;
		}
		
	}
	
	public static class Double extends ContainerPart {

		public Double(AEntityF_Multipart<?> entityOn, WrapperPlayer player, JSONPartDefinition placementDefinition, WrapperNBT data, APart parentPart){
			super(entityOn, player, placementDefinition, data, parentPart);
		}

		@Override
		public int size(){
			return 12;
		}
		
	}

}
