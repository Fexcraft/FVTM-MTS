package net.fexcraft.mod.fvtm.compat.mts;

import minecrafttransportsimulator.systems.PackParserSystem;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.mod.fvtm.InternalAddon;
import net.fexcraft.mod.fvtm.compat.mts.data.ContainerItem;
import net.fexcraft.mod.fvtm.compat.mts.data.ContainerJsonPart;
import net.fexcraft.mod.fvtm.compat.mts.data.ContainerPartItem;
import net.fexcraft.mod.fvtm.compat.mts.data.ContainerPartItemCreator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = MTSCompat.MODID, name = MTSCompat.NAME, version = MTSCompat.VERSION, dependencies = "required-after:fcl;required-after:fvtm;required-after:mts")
public class MTSCompat {
	
    public static final String MODID = "fvtm_mts";
    public static final String NAME = "FVTM MTS Compat";
    public static final String VERSION = "1.2";
    //
	public static ContainerJsonPart CON_PART_SINGLE = new ContainerJsonPart("single");
	public static ContainerJsonPart CON_PART_DOUBLE = new ContainerJsonPart("double");
	public static ContainerPartItem CON_ITEM_SINGLE = new ContainerPartItem(CON_PART_SINGLE, true);
	public static ContainerPartItem CON_ITEM_DOUBLE = new ContainerPartItem(CON_PART_DOUBLE, false);
	public static Item CON_SINGLE, CON_DOUBLE;
	//TODO "extended" type support in future?

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
    	PackParserSystem.addItemPartCreator(new ContainerPartItemCreator());
    	PackParserSystem.registerItem(CON_PART_SINGLE);
    	PackParserSystem.registerItem(CON_PART_DOUBLE);
    	FCLRegistry.newAutoRegistry(MODID).addItem("container_single", CON_SINGLE = new ContainerItem(CON_ITEM_SINGLE), 0, null);
    	FCLRegistry.getAutoRegistry(MODID).addItem("container_double", CON_DOUBLE = new ContainerItem(CON_ITEM_DOUBLE), 0, null);
        MinecraftForge.EVENT_BUS.register(new CompatEvents());
		EntityRegistry.registerModEntity(new ResourceLocation("fvtm_mts:tracker"), Tracker.class, "fvtm_mts.tracker", 0, this, 512, 1, false);
        if(event.getSide().isClient()){
			net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(Tracker.class, RenderTracker::new);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
    	if(event.getSide().isClient()){
            CON_SINGLE.setCreativeTab(InternalAddon.INSTANCE.getDefaultCreativeTab());
            CON_DOUBLE.setCreativeTab(InternalAddon.INSTANCE.getDefaultCreativeTab());
    	}
    }
}
