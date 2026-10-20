package xyz.daikirai.forge.event.entity.player;
import net.minecraft.entity.player.EntityPlayer; import xyz.daikirai.forge.event.entity.living.LivingEvent;
public class PlayerEvent extends LivingEvent { public final EntityPlayer entityPlayer; public PlayerEvent(EntityPlayer p) { super(p); entityPlayer = p; } }
