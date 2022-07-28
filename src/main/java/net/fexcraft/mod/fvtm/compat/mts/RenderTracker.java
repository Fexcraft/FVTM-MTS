package net.fexcraft.mod.fvtm.compat.mts;

import org.lwjgl.opengl.GL11;

import minecrafttransportsimulator.baseclasses.Point3D;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.data.container.ContainerSlot;
import net.fexcraft.mod.fvtm.model.DebugModels;
import net.fexcraft.mod.fvtm.render.EffectRenderer;
import net.fexcraft.mod.fvtm.util.Command;
import net.fexcraft.mod.fvtm.util.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTracker extends Render<Tracker> implements IRenderFactory<Tracker> {
	
	private static ContainerHolder cap;
	private static Entity camera;
	private static double x, y, z;

    public RenderTracker(RenderManager renderManager){
        super(renderManager);
        shadowSize = 0.0F;
    }

    @Override
    public void doRender(Tracker tracker, double x, double y, double z, float entity_yaw, float ticks){
    	if(tracker.entity == null){
    		Print.debug(tracker.wrapper);
    		return;
    	}
    	if(tracker.wrapper == null || tracker.wrapper.ent == null|| (cap = tracker.wrapper.getCapability()) == null || cap.getContainerSlots().length == 0){
    		Print.debug(tracker.wrapper);
    		Print.debug(tracker.wrapper.ent);
    		Print.debug(cap = tracker.wrapper.getCapability());
    		Print.debug(cap.getContainerSlots().length);
    		return;
    	}
        GL11.glPushMatrix();
        translate(ticks);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(Command.CONTAINER){
    		for(ContainerSlot slot : cap.getContainerSlots()){
    			Point3D point = new Point3D(slot.position.x, slot.position.y, slot.position.z);
    			point.rotate(tracker.wrapper.ent.rotation);
    			point.add(tracker.wrapper.ent.position);//.add(.375, 0, .375);
    			GL11.glPushMatrix();
    			GL11.glTranslated(point.x, point.y, point.z);
    			GL11.glDisable(GL11.GL_TEXTURE_2D);
    			DebugModels.CENTERSPHERE.render(.5f);
    			GL11.glEnable(GL11.GL_TEXTURE_2D);
    			GL11.glPopMatrix();
    		}
        }
		Point3D pos = tracker.wrapper.ent.prevPosition.interpolate(tracker.wrapper.ent.position, ticks);
		Point3D rot = tracker.wrapper.ent.prevOrientation.angles.interpolate(tracker.wrapper.ent.orientation.angles, ticks);
		//cap.render(pos.x, pos.y, pos.z, rot.y, rot.x, -rot.z);
		GL11.glTranslated(pos.x, pos.y, pos.z);
		EffectRenderer.renderContainerInfo(tracker.world, tracker.wrapper, cap, new Vec3f(rot.y, rot.x, rot.z));
		GL11.glPopMatrix();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Tracker entity){
        return Resources.NULL_TEXTURE;
    }
    
    @Override
    public Render<Tracker> createRenderFor(RenderManager manager){
        return new RenderTracker(manager);
    }
    
    public static void translate(float ticks){
        camera = Minecraft.getMinecraft().getRenderViewEntity();
        x = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * ticks;
        y = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * ticks;
        z = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * ticks;
        GL11.glTranslated(-x, -y, -z);
    }

}
