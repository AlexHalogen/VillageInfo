package ga.alexhalogen.villageinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.HUDRenderListener;
import com.mumfrey.liteloader.ServerCommandProvider;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.ServerPlayerListener;
import com.google.gson.annotations.Expose;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.modconfig.Exposable;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;


import java.io.File;


@ExposableOptions(
        strategy = ConfigStrategy.Unversioned,
        filename = "villageinfo.json",
        aggressive = true
)
public class LiteModVillageInfo implements ServerCommandProvider, HUDRenderListener, Tickable, Configurable,
        ServerPlayerListener, Exposable {


    @Expose
    @SerializedName("mod_enabled")
    private boolean isEnabled = true;

    private boolean isSinglePlayer = false;
    private boolean isF3Enabled = false;
    private boolean isBlocking = false;

    public static KeyBinding toggleVillageinfo;
    private VillageInfoRenderer renderer;

    public LiteModVillageInfo() {
    }

    @Override
    public String getName() {
        return "VillageInfo-Liteloader";
    }


    @Override
    public String getVersion() {
        return "1.0.2";
    }


    @Override
    public void init(File configPath) {
        toggleVillageinfo = new KeyBinding(
                I18n.format("villageinfo.controls.toggle"), Keyboard.KEY_F3
                , "VillageInfo HUD");

        LiteLoader.getInput().registerKeyBinding(toggleVillageinfo);
    }

    @Override
    public void onTick(Minecraft minecraft, float partialTick, boolean inGame, boolean clock) {
        if (toggleVillageinfo.isPressed()) {
            this.isF3Enabled = !this.isF3Enabled;
        }
         isBlocking = !(inGame && minecraft.currentScreen == null && Minecraft.isGuiEnabled());
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {
    }


    @Override
    public void provideCommands(ServerCommandManager commandManager) {
        commandManager.registerCommand(new VillageInfoCommand());
    }

    @Override
    public void onPostRenderHUD(int screenWidth, int screenHeight) {
        if (isEnabled && isSinglePlayer && isF3Enabled && (renderer != null) && !isBlocking) {
            renderer.render(screenWidth, screenHeight);
        }
    }

    @Override
    public void onPreRenderHUD(int screenWidth, int screenHeight) {
    }

    @Override
    public Class<? extends ConfigPanel> getConfigPanelClass() {
        return VillageInfoConfigPanel.class;
    }

    @Override
    public void onPlayerLoggedIn(EntityPlayerMP player) {

        this.isF3Enabled = false;
        if (player != null) {
            Minecraft mc = Minecraft.getMinecraft();
            MinecraftServer integratedServer = mc.getIntegratedServer();

            if (integratedServer == null) { // not singleplayer or lan host
                this.isSinglePlayer = false;
            } else {
                this.isSinglePlayer = true;
                this.renderer = new VillageInfoRenderer(mc, integratedServer, player);
            }
        }
    }

    @Override
    public void onPlayerRespawn(EntityPlayerMP player, EntityPlayerMP oldPlayer, int newDimension,
                                boolean playerWonTheGame) {
    }

    @Override
    public void onPlayerConnect(EntityPlayerMP player, GameProfile profile) {
    }

    @Override
    public void onPlayerLogout(EntityPlayerMP player) {
        this.isF3Enabled = false;
        this.isSinglePlayer = false;
        this.renderer = null;
    }


    public String getStatus() {
        return this.isEnabled ? "On" : "Off";
    }

    public void setStatus(boolean status) {
        this.isEnabled = status;
        LiteLoader.getInstance().writeConfig(this);
    }

}
