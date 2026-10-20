package xyz.daikirai.mixin.lunar;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import xyz.daikirai.lunar.Events;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

@Mixin(value = RenderGlobal.class)
public abstract class MixinRenderGlobalEvents {
    @Inject(method = "func_72731_b(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/MovingObjectPosition;IF)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void ravenb4$highlight(EntityPlayer p, MovingObjectPosition mop, int sub, float pt, CallbackInfo ci) {
        if (Events.drawBlockHighlight((RenderGlobal) (Object) this, p, mop, sub, pt)) ci.cancel();
    }
}
