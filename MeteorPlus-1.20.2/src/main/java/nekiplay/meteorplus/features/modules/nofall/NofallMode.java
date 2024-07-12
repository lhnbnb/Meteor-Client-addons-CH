package nekiplay.meteorplus.features.modules.nofall;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;

public class NofallMode {
	protected final MinecraftClient mc;
	protected final NoFallPlus settings;
	private final NoFallModes type;

	public NofallMode(NoFallModes type) {
		this.settings = Modules.get().get(NoFallPlus.class);
		this.mc = MinecraftClient.getInstance();
		this.type = type;
	}

	public void onSendPacket(PacketEvent.Send event) {}
	public void onSentPacket(PacketEvent.Sent event) {}
	public void onReceivePacket(PacketEvent.Receive event) {}

	public void onTickEventPre(TickEvent.Pre event) {}
	public void onTickEventPost(TickEvent.Post event) {}

	public void onActivate() {}
	public void onDeactivate() {}
}
