package net.fexcraft.mod.fvtm.compat.mts;

import org.lwjgl.opengl.GL11;

import minecrafttransportsimulator.baseclasses.Point3d;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
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
		/*ContainerSlot slot = wrapper.getCapability().getContainerSlot("cargo");
		if(slot != null){
			Point3d point = new Point3d(slot.position.x, slot.position.y, slot.position.z);
			point.rotateFine(tracker.entity.entity.angles);
			point.add(tracker.entity.entity.position);//.add(.375, 0, .375);
			GL11.glPushMatrix();
			GL11.glTranslated(point.x, point.y, point.z);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			DebugModels.CENTERSPHERE.render(.5f);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}*/
		Point3d pos = tracker.wrapper.ent.prevPosition.getInterpolatedPoint(tracker.wrapper.ent.position, ticks);
		Point3d rot = tracker.wrapper.ent.prevAngles.getInterpolatedPoint(tracker.wrapper.ent.angles, ticks);
        GL11.glTranslated(pos.x, pos.y, pos.z);
		cap.render(/*pos.x, pos.y, pos.z,*/ 0, 0, 0, rot.y + 90, rot.x, -rot.z);
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
