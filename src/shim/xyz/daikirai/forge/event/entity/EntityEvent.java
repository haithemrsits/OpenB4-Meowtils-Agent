package xyz.daikirai.forge.event.entity;
import net.minecraft.entity.Entity; import xyz.daikirai.forge.fml.common.eventhandler.Event;
public class EntityEvent extends Event { public final Entity entity; public EntityEvent(Entity entity) { this.entity = entity; } }
