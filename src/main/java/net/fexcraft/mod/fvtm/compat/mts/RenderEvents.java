package net.fexcraft.mod.fvtm.compat.mts;

import org.lwjgl.opengl.GL11;

import minecrafttransportsimulator.baseclasses.Point3d;
import minecrafttransportsimulator.mcinterface.BuilderEntity;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderEvents {
	
	private static BuilderEntity ent;
	private static ContainerHolder cap;
	
    @SubscribeEvent
    public void renderRails(RenderWorldLastEvent event){
        float ticks = Minecraft.getMinecraft().getRenderPartialTicks();
        Entity camera = Minecraft.getMinecraft().getRenderViewEntity();
        double x = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * ticks;
        double y = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * ticks;
        double z = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * ticks;
        //
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslated(-x, -y, -z);
        World world = Minecraft.getMinecraft().world;
        for(Entity entity : world.loadedEntityList){
        	if(entity instanceof BuilderEntity == false) continue;
        	if((ent = (BuilderEntity)entity).entity == null) continue;
        	BEWrapper wrapper = CompatEvents.wrappers.get(ent);
        	if(wrapper == null || (cap = wrapper.getCapability()) == null || cap.getContainerSlots().length == 0) continue;
        	//
			Point3d entpos = ent.entity.prevPosition.getInterpolatedPoint(ent.entity.position, ticks);
			Point3d entrot = ent.entity.prevAngles.getInterpolatedPoint(ent.entity.angles, ticks);
        	for(ContainerSlot slot : cap.getContainerSlots()){
        		GL11.glPushMatrix();
        		GL11.glTranslated(entpos.x, entpos.y, entpos.z);
        		slot.render(entity, entrot.y - 90, entrot.x, entrot.z);
        		GL11.glPopMatrix();
        	}
        }
		GL11.glPopMatrix();
    }

}
