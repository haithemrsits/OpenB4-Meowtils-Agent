package xyz.daikirai.mixin.lunar;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import xyz.daikirai.lunar.Events;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import xyz.daikirai.forge.client.ClientCommandHandler;

@Mixin(value = EntityPlayerSP.class)
public abstract class MixinClientCommandEvents {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true, require = 0)
    private void ravenb4$command(String message, CallbackInfo ci) {
        if (ClientCommandHandler.instance.func_71556_a((ICommandSender) (Object) this, message) != 0) ci.cancel();
    }
}
