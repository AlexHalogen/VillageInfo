package ga.alexhalogen.villageinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.HUDRenderListener;
import com.mumfrey.liteloader.ServerCommandProvider;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.Tickable;
import com.google.gson.annotations.Expose;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.modconfig.Exposable;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.command.*;


import java.io.File;


@ExposableOptions(
		strategy = ConfigStrategy.Unversioned,
		filename = "villageinfo.json",
		aggressive = true
)
public class LiteModVillageInfo implements ServerCommandProvider,
			HUDRenderListener, Tickable, Configurable, Exposable {    

	@Expose
	@SerializedName("master_switch")
	private boolean on = false;

	private boolean f3on = false;
	public static KeyBinding togglevillageinfo;
	public VillageInfoRenderer renderer = new VillageInfoRenderer();

	public LiteModVillageInfo() {}
	
	@Override
	public String getName() {
		return "VillageInfo-Liteloader";
	}
	

	@Override
	public String getVersion() {
		return "1.0.0";
	}
		

	@Override
	public void init(File configPath) {
		togglevillageinfo = new KeyBinding(
			I18n.format("villageinfo.controls.toggle"), Keyboard.KEY_F3
			, "VillageInfo HUD");

		LiteLoader.getInput().registerKeyBinding(togglevillageinfo);
	}
	
	@Override
	public void onTick(Minecraft minecraft, float partialTicks
					,boolean inGame, boolean clock) {
		if(togglevillageinfo.isPressed()) {
			renderer.enabledF3 = !renderer.enabledF3;
		}
	}
	@Override
	public void upgradeSettings(String version, File configPath
			, File oldConfigPath) {}
	
	
	@Override
	public void provideCommands(ServerCommandManager commandManager) {
		commandManager.registerCommand(new VillageInfoCommand());
	}

	@Override
	public void onPostRenderHUD(int screenWidth, int screenHeight) {
		renderer.render(screenWidth, screenHeight);
	}

	@Override
	public void onPreRenderHUD(int screenWidth, int screenHeight) {}

	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass() {
		return VillageInfoConfigPanel.class;
	}

	public String getStatus() {
		return renderer.enabled ? "On" : "Off";
	}

	public void setStatus(boolean status) {
		renderer.enabled = status;
	}

	public boolean getF3Toggle() {
		return renderer.enabledF3;
	}

	public void setF3Toggle(boolean status) {
		renderer.enabledF3 = status;
	}
}
