package xyz.daikirai.forge.event.world;
import net.minecraft.world.World; import xyz.daikirai.forge.fml.common.eventhandler.Event;
public class WorldEvent extends Event {
    public final World world; public WorldEvent(World w) { world = w; }
    public static class Unload extends WorldEvent { public Unload(World w) { super(w); } }
}
