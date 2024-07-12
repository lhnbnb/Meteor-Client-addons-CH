package nekiplay.meteorplus.features.modules.autoobsidianmine;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.CollisionShapeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.MeteorPlus;
import nekiplay.meteorplus.features.modules.autoobsidianmine.modes.Cauldrons;
import nekiplay.meteorplus.features.modules.autoobsidianmine.modes.Portals;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

public class AutoObsidianFarm extends Module {
	public AutoObsidianFarm() {
		super(MeteorPlus.CATEGORY, "自动采集黑曜石(崩溃)", "在你离开电脑状态下自动采集黑曜石。");
	}

	private final SettingGroup sgGeneral = settings.getDefaultGroup();

	public final Setting<AutoObsidianFarmModes> workingMode = sgGeneral.add(new EnumSetting.Builder<AutoObsidianFarmModes>()
		.name("模式")
		.description("工作模式")
		.defaultValue(AutoObsidianFarmModes.Portals_Vanila)
		.onModuleActivated(modesSetting -> onModeChanged(modesSetting.get()))
		.onChanged(this::onModeChanged)
		.build()
	);

	public final Setting<BlockPos> mainPortalPosition = sgGeneral.add(new BlockPosSetting.Builder()
		.name("portal location 1")
		.description("地狱传送门的位置")
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Portals_Vanila)
		.build()
	);

	public final Setting<BlockPos> twoPortalPosition = sgGeneral.add(new BlockPosSetting.Builder()
		.name("portal location 2")
		.description("新传送门生成中地狱传送门的位置")
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Portals_Vanila)
		.build()
	);

	public final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
		.name("command")
		.description("Send command.")
		.defaultValue("/home")
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Portal_Homes)
		.build()
	);

	public final Setting<BlockPos> lavaPlaceLocation = sgGeneral.add(new BlockPosSetting.Builder()
		.name("lava-place-location")
		.description("the position placing lava")
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Cauldrons)
		.build()
	);

	public final Setting<Integer> delayCommand = sgGeneral.add(new IntSetting.Builder()
		.name("command-delay")
		.description("Ticks delay.")
		.defaultValue(700)
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Portal_Homes)
		.build()
	);

	public final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
		.name("mining-delay")
		.description("Mining delay.")
		.defaultValue(3)
		.build()
	);

	public final Setting<Integer> collectDelay = sgGeneral.add(new IntSetting.Builder()
		.name("collect-delay")
		.description("Cauldron collecting lava delay.")
		.defaultValue(8)
		.build()
	);

	public final Setting<Integer> lavaPlaceDelay = sgGeneral.add(new IntSetting.Builder()
		.name("lava-place-delay")
		.description("Delay for placing lava.")
			.defaultValue(8)
		.build()
	);

	public final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
		.name("range")
		.description("Cauldron range's.")
		.defaultValue(5)
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Cauldrons)
		.build()
	);

	public final Setting<Boolean> solidCauldrons = sgGeneral.add(new BoolSetting.Builder()
		.name("solid-cauldrons")
		.description("Solid cauldrons.")
		.defaultValue(false)
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Cauldrons)
		.build()
	);

	public final Setting<Boolean> bypassSneak = sgGeneral.add(new BoolSetting.Builder()
		.name("bypass-sneak")
		.description("bypass-sneak-interact.")
		.defaultValue(false)
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Cauldrons)
		.build()
	);

	public final Setting<Boolean> noBaritoneBreaking = sgGeneral.add(new BoolSetting.Builder()
		.name("disable-baritone-breaking-if-not-mine-portal")
		.description("No break blocks if is not mining portal.")
		.defaultValue(true)
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Portals_Vanila)
		.build()
	);

	public final Setting<Boolean> noBaritonePlacing = sgGeneral.add(new BoolSetting.Builder()
		.name("disable-baritone-place")
		.description("No place blocks.")
		.defaultValue(true)
		.visible(() -> workingMode.get() == AutoObsidianFarmModes.Portals_Vanila)
		.build()
	);

	public final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
		.name("rotate")
		.description("Rotate to breaking block.")
		.defaultValue(false)
		.visible(() -> workingMode.get() != AutoObsidianFarmModes.Cauldrons)
		.build()
	);

	public final Setting<Boolean> swingHand = sgGeneral.add(new BoolSetting.Builder()
		.name("swing-hand")
		.description("Swing hand client side.")
		.defaultValue(true)
		.visible(() -> workingMode.get() != AutoObsidianFarmModes.Cauldrons)
		.build()
	);

	private AutoObsidianFarmMode currentMode;

	private void onModeChanged(AutoObsidianFarmModes mode) {
		switch (mode) {
			case Portal_Homes, Portals_Vanila -> {
				currentMode = new Portals();
			}
			case Cauldrons ->  {
				currentMode = new Cauldrons();
			}
		}
	}

	@Override
	public String getInfoString() {
		return currentMode.getInfoString();
	}

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
	private void onCollisionShape(CollisionShapeEvent event) {
		currentMode.onCollisionShape(event);
	}

	@EventHandler
	private void onMovePacket(PacketEvent.Send event) {
		if (event.packet instanceof PlayerMoveC2SPacket playerMove) {
			currentMode.onMovePacket(playerMove);
		}
	}
}
