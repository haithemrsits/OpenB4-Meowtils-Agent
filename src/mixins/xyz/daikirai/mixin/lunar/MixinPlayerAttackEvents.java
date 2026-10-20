package xyz.daikirai.mixin.lunar;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import xyz.daikirai.lunar.Events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Mixin(value = EntityPlayer.class)
public abstract class MixinPlayerAttackEvents {
    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"), cancellable = true, require = 0)
    private void ravenb4$attack(Entity target, CallbackInfo ci) {
        if (Events.attack((EntityPlayer) (Object) this, target)) ci.cancel();
    }
}
