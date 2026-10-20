package xyz.daikirai.forge.client.event;
import net.minecraft.client.gui.GuiScreen;
import xyz.daikirai.forge.fml.common.eventhandler.Event;
public class GuiOpenEvent extends Event { public GuiScreen gui; public GuiOpenEvent(GuiScreen gui) { this.gui = gui; } }
