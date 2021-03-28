package net.fexcraft.mod.fvtm.compat.mts;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MTSCompat.MODID, name = MTSCompat.NAME, version = MTSCompat.VERSION)
public class MTSCompat {
	
    public static final String MODID = "fvtm_mts";
    public static final String NAME = "FVTM MTS Compat";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new CompatEvents());
        if(event.getSide().isClient()){
        	MinecraftForge.EVENT_BUS.register(new net.fexcraft.mod.fvtm.compat.mts.RenderEvents());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
    	//
    }
}
