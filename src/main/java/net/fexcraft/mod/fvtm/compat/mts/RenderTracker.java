package net.fexcraft.mod.fvtm.compat.mts;

import org.lwjgl.opengl.GL11;

import minecrafttransportsimulator.baseclasses.Point3d;
import net.fexcraft.mod.fvtm.data.container.ContainerHolder;
import net.fexcraft.mod.fvtm.util.Resources;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTracker extends Render<Tracker> implements IRenderFactory<Tracker> {
	
	private static ContainerHolder cap;

    public RenderTracker(RenderManager renderManager){
        super(renderManager);
        shadowSize = 0.0F;
    }

    @Override
    public void doRender(Tracker tracker, double x, double y, double z, float entity_yaw, float ticks){
    	if(tracker.entity == null || tracker.entity.entity == null) return;
    	BEWrapper wrapper = CompatEvents.wrappers.get(tracker.entity);
    	if(wrapper == null || (cap = wrapper.getCapability()) == null || cap.getContainerSlots().length == 0) return;
        GL11.glPushMatrix();
        RenderEvents.translate(ticks);
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
		Point3d pos = tracker.entity.entity.prevPosition.getInterpolatedPoint(tracker.entity.entity.position, ticks);
		Point3d rot = tracker.entity.entity.prevAngles.getInterpolatedPoint(tracker.entity.entity.angles, ticks);
		cap.render(pos.x, pos.y, pos.z, rot.y - 90, rot.x, rot.z);
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

}
