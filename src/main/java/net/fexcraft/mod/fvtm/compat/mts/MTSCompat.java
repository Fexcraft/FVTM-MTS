package net.fexcraft.mod.fvtm.compat.mts;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = MTSCompat.MODID, name = MTSCompat.NAME, version = MTSCompat.VERSION)
public class MTSCompat {
	
    public static final String MODID = "fvtm_mts";
    public static final String NAME = "FVTM MTS Compat";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new CompatEvents());
		EntityRegistry.registerModEntity(new ResourceLocation("fvtm_mts:tracker"), Tracker.class, "fvtm_mts.tracker", 0, this, 512, 1, false);
        if(event.getSide().isClient()){
        	MinecraftForge.EVENT_BUS.register(new net.fexcraft.mod.fvtm.compat.mts.RenderEvents());
			net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(Tracker.class, RenderTracker::new);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
    	//
    }
}
