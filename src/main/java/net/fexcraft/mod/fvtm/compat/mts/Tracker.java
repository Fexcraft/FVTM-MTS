package net.fexcraft.mod.fvtm.compat.mts;

import io.netty.buffer.ByteBuf;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.mcinterface.BuilderEntityExisting;
import minecrafttransportsimulator.mcinterface.WrapperEntity;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class Tracker extends Entity implements IEntityAdditionalSpawnData {

	public int entityid = -1;
	public BuilderEntityExisting entity;
	public BEEWrapper wrapper;
	
	public Tracker(World world){
		super(world);
		this.setSize(3, 3);;
	}

	public Tracker(BEEWrapper wrapper){
		this(wrapper.getEntity().world);
		entityid = (entity = (this.wrapper = wrapper).getEntity()).getEntityId();
		Print.debug("Adding Tracker for " + entity);
		setPosition(entity.posX, entity.posY, entity.posZ);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer){
		buffer.writeInt(entityid);
		buffer.writeDouble(posX);
		buffer.writeDouble(posY);
		buffer.writeDouble(posZ);
	}

	@Override
	public void readSpawnData(ByteBuf buffer){
		entityid = buffer.readInt();
		Entity ent = world.getEntityByID(entityid);
		if(ent != null){
			entity = (BuilderEntityExisting)ent;
			wrapper = CompatEvents.getWrapper(entity);
			wrapper.setTracker(this);
		}
		Print.debug("[0] Linked Tracker for " + entity);
		setPosition(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
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
				return;
			}
			if(entity != null && entity.isDead) this.setDead();
		}
		if(entity == null || wrapper == null){
			Entity ent = world.getEntityByID(entityid);
			if(ent != null){
				entity = (BuilderEntityExisting)ent;
				wrapper = CompatEvents.getWrapper(entity);
			}
			Print.debug("[1] Linked Tracker to " + entity);
		}
		if(entity == null) return;
		if(wrapper != null && wrapper.ent == null){
			WrapperEntity we = WrapperEntity.getWrapperFor(entity);
			wrapper.ent = (EntityVehicleF_Physics)we.getBaseEntity();
		}
		this.setPosition(entity.posX, entity.posY, entity.posZ);
	}
	
    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand){
    	this.canBeCollidedWith();
        return false;
    }
    
    @Override
    public void setDead(){
    	super.setDead();
    }

    @Override
    public boolean canBeCollidedWith(){
        return false;
    }

}
