package xyz.daikirai.mixin.lunar;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import xyz.daikirai.lunar.Events;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

@Mixin(value = RendererLivingEntity.class)
public abstract class MixinRendererLivingEntityEvents {
    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void ravenb4$specials(EntityLivingBase e, double x, double y, double z, CallbackInfo ci) {
        if (Events.renderSpecialsPre(e, (RendererLivingEntity) (Object) this, x, y, z)) ci.cancel();
    }
}
