package nekiplay.meteorplus.features.modules.spider;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.MeteorPlus;
import nekiplay.meteorplus.features.modules.spider.modes.Eclip;
import nekiplay.meteorplus.features.modules.spider.modes.Matrix;
import nekiplay.meteorplus.features.modules.spider.modes.Vulcan;

public class SpiderPlus extends Module {
	public SpiderPlus() {
		super(MeteorPlus.CATEGORY, "蜘蛛+", "绕过反作弊的蜘蛛爬墙");
		onSpiderModeChanged(spiderMode.get());
	}
	private final SettingGroup sgGeneral = settings.getDefaultGroup();


	public final Setting<SpiderModes> spiderMode = sgGeneral.add(new EnumSetting.Builder<SpiderModes>()
		.name("模式")
		.description("应用蜘蛛模式的方法")
		.defaultValue(SpiderModes.Matrix)
		.onModuleActivated(spiderModesSetting -> onSpiderModeChanged(spiderModesSetting.get()))
		.onChanged(this::onSpiderModeChanged)
		.build()
	);

	public final Setting<Integer> Interval = sgGeneral.add(new IntSetting.Builder()
		.name("间隔(不了解勿操作)")
		.defaultValue(3)
		.description("如果不了解它的作用，请勿操作。")
		.range(0, 10)
		.build()
	);

	public final Setting<Integer> Blocks = sgGeneral.add(new IntSetting.Builder()
		.name("方块(不了解勿操作)")
		.defaultValue(3)
		.description("如果不了解它的作用，请勿操作。")
		.range(0, 10)
		.build()
	);

	private SpiderMode currentMode;

	@Override
	public void onActivate() {
		currentMode.onActivate();
	}

	@Override
	public void onDeactivate() {
		currentMode.onDeactivate();
	}

	@EventHandler
	private void onPreTick(TickEvent.Pre event) {
		currentMode.onTickEventPre(event);
	}

	@EventHandler
	private void onPostTick(TickEvent.Post event) {
		currentMode.onTickEventPost(event);
	}
	@EventHandler
	public void onSendPacket(PacketEvent.Send event) {
		currentMode.onSendPacket(event);
	}
	@EventHandler
	public void onSentPacket(PacketEvent.Sent event) {
		currentMode.onSentPacket(event);
	}


	private void onSpiderModeChanged(SpiderModes mode) {
		switch (mode) {
			case Matrix:   currentMode = new Matrix(); break;
			case Vulcan:   currentMode = new Vulcan(); break;
			case Elytra_clip:   currentMode = new Eclip(); break;
		}
	}
}
