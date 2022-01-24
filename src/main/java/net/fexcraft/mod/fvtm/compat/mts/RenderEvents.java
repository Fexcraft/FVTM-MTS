package net.fexcraft.mod.fvtm.compat.mts;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderEvents {
	
	private static Entity camera;
	private static double x, y, z;
	
    @SubscribeEvent
    public void render(RenderWorldLastEvent event){
        /*float ticks = Minecraft.getMinecraft().getRenderPartialTicks();
        Entity camera = Minecraft.getMinecraft().getRenderViewEntity();
        x = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * ticks;
        y = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * ticks;
        z = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * ticks;
        //
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslated(-x, -y, -z);
        //
		GL11.glPopMatrix();*/
    }
    
    public static void translate(float ticks){
        camera = Minecraft.getMinecraft().getRenderViewEntity();
        x = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * ticks;
        y = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * ticks;
        z = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * ticks;
        GL11.glTranslated(-x, -y, -z);
    }

}
