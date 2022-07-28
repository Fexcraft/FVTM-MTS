package net.fexcraft.mod.fvtm.compat.mts.data;

import java.lang.reflect.Field;

import mcinterface1122.InterfaceInterface;
import mcinterface1122.WrapperEntity;
import minecrafttransportsimulator.baseclasses.TransformationMatrix;
import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.IWrapperNBT;
import minecrafttransportsimulator.mcinterface.IWrapperPlayer;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.compat.mts.CompatEvents;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ContainerPart extends APart {

	public ContainerPart(AEntityF_Multipart<?> entityOn, IWrapperPlayer placingPlayer, JSONPartDefinition placementDefinition, IWrapperNBT data, APart parentPart){
		super(entityOn, placingPlayer, placementDefinition, data, parentPart);
		CompatEvents.cotracked.add(entityOn);
	}
	
    @Override
    protected void renderModel(TransformationMatrix transform, boolean blendingEnabled, float partialTicks) {
    	//
    }
	
    @Override
    public boolean disableRendering(float partialTicks) {
        return true;
    }
	
	@Override
	public void remove(){
		super.remove();
	}
	
	@Override
	public boolean interact(IWrapperPlayer player){
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
	
	public static Entity getEntity(IWrapperPlayer player){
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
			return (Entity)entfield.get(player);
		}
		catch(IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public abstract int size();
	
	public static class Single extends ContainerPart {

		public Single(AEntityF_Multipart<?> entityOn, IWrapperPlayer placing, JSONPartDefinition placementDefinition, IWrapperNBT data, APart parentPart){
			super(entityOn, placing, placementDefinition, data, parentPart);
		}

		@Override
		public int size(){
			return 6;
		}
		
	}
	
	public static class Double extends ContainerPart {

		public Double(AEntityF_Multipart<?> entityOn, IWrapperPlayer player, JSONPartDefinition placementDefinition, IWrapperNBT data, APart parentPart){
			super(entityOn, player, placementDefinition, data, parentPart);
		}

		@Override
		public int size(){
			return 12;
		}
		
	}

}
