package net.fexcraft.mod.fvtm.compat.mts.data;

import static net.fexcraft.mod.fvtm.compat.mts.CompatEvents.EXISTING_CLIENT;
import static net.fexcraft.mod.fvtm.compat.mts.CompatEvents.EXISTING_SERVER;

import java.lang.reflect.Field;

import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.BuilderEntityExisting;
import minecrafttransportsimulator.mcinterface.WrapperEntity;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import minecrafttransportsimulator.mcinterface.WrapperPlayer;
import minecrafttransportsimulator.rendering.instances.RenderPart;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPart extends APart {

	public ContainerPart(AEntityF_Multipart<?> entityOn, WrapperPlayer player, JSONPartDefinition placementDefinition, WrapperNBT data, APart parentPart){
		super(entityOn, player, placementDefinition, data, parentPart);
		//(entityOn.world.isClient() ? CompatEvents.EXISTING_CLIENT : CompatEvents.EXISTING_SERVER).add(this);
	}
	
	@Override
	public RenderPart getRenderer(){
		return new ContainerRenderPart();
	}
	
	@Override
	public void remove(){
		(entityOn.world.isClient() ? EXISTING_CLIENT : EXISTING_SERVER).remove(this);
		super.remove();
	}
	
	@Override
	public boolean interact(WrapperPlayer player){
		BuilderEntityExisting ent = EXISTING_SERVER.get(this);
		ContainerHolder holder = ent.getCapability(Capabilities.CONTAINER, null);
		holder.openGUI((EntityPlayer)getEntity(player));
		Print.debug("< open gui >");
		return true;
	}
	
	private static Field entfield;
	private static boolean entfailed = false;
	
	public static Entity getEntity(WrapperEntity ent){
		if(entfield == null && !entfailed){
			try{
				entfield = APart.class.getField("entity");
				entfield.setAccessible(true);
			}
			catch(Exception e){
				Print.log("Failed to get field. [ENTFIELD:ERR:0]");
				entfailed = true;
			}
		}
		try {
			return (Entity)entfield.get(ent);
		}
		catch(IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
			return null;
		}
	}

}
