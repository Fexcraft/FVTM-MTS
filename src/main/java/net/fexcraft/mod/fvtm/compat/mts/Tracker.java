package net.fexcraft.mod.fvtm.compat.mts;

import io.netty.buffer.ByteBuf;
import minecrafttransportsimulator.baseclasses.Point3d;
import minecrafttransportsimulator.mcinterface.BuilderEntity;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fvtm.data.Capabilities;
import net.fexcraft.mod.fvtm.item.ContainerItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class Tracker extends Entity implements IEntityAdditionalSpawnData {

	public int entityid = -1;
	public BuilderEntity entity;
	public BEWrapper wrapper;
	
	public Tracker(World world){
		super(world);
		this.setSize(1.5f, 1.25f);
	}

	public Tracker(BEWrapper wrapper, Point3d pos){
		this(wrapper.getEntity().world);
		entityid = (entity = (this.wrapper = wrapper).getEntity()).getEntityId();
		Print.debug("Adding Tracker for " + entity);
		setPosition(pos.x, pos.y, pos.z);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer){
		buffer.writeInt(entityid);
	}

	@Override
	public void readSpawnData(ByteBuf buffer){
		entityid = buffer.readInt();
		Entity ent = world.getEntityByID(entityid);
		if(ent != null) entity = (BuilderEntity)ent;
		Print.debug("Linked Tracker for " + entity);
	}

	@Override
	protected void entityInit(){
		//
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound){
		//
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound){
		//
	}
	
	@Override
	public void onUpdate(){
		if(!world.isRemote){
			if(wrapper == null || entityid == -1){
				this.setDead();
				Print.debug("Removing invalid Tracker - " + wrapper + " " + entity + " " + entityid);
			}
			if(entity != null && entity.isDead) this.setDead();
		}
		if(entity == null){
			Entity ent = world.getEntityByID(entityid);
			if(ent != null) entity = (BuilderEntity)ent;
			Print.debug("Linked Tracker to " + entity);
		}
		if(entity == null) return;
		this.setPosition(entity.posX, entity.posY, entity.posZ);
	}
	
    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand){
        if(isDead || world.isRemote || hand == EnumHand.OFF_HAND || entity == null) return false;
        ItemStack stack = player.getHeldItem(hand);
        Print.debug(stack);
        if(!stack.isEmpty() && (stack.getItem() instanceof ContainerItem || stack.getItem() instanceof ItemTool)){
        	entity.getCapability(Capabilities.CONTAINER, null).openGUI(player);
        	return true;
        }
        return false;
    }
    
    @Override
    public void setDead(){
    	super.setDead();
    }

}
