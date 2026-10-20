package xyz.daikirai.forge.client.event;
import net.minecraft.util.IChatComponent;
import xyz.daikirai.forge.fml.common.eventhandler.Event;
public class ClientChatReceivedEvent extends Event {
    public final byte type; public IChatComponent message;
    public ClientChatReceivedEvent(byte type, IChatComponent message) { this.type = type; this.message = message; }
}
