package ga.alexhalogen.villageinfo;
import com.mumfrey.liteloader.modconfig.AbstractConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;


public class VillageInfoConfigPanel extends AbstractConfigPanel {

	public static final int SPACING = 24;

	
	@Override
	public String getPanelTitle() {
		return I18n.format("villageinfo.configpanel.title");
	}

	@Override
	public void onPanelHidden() {}

	@Override
	protected void addOptions(ConfigPanelHost host) {
		LiteModVillageInfo mod = host.<LiteModVillageInfo>getMod();
		
		int id = 0;
        
		this.addControl(new GuiButton(id++, 20, (id - 1) * SPACING, 200, 20, I18n.format("villageinfo.configpanel.button.toggle") + mod.getStatus()), new ConfigOptionListener<GuiButton>() {
        	@Override
        	public void actionPerformed(GuiButton control) {
    			mod.setStatus(!mod.getStatus().equals("On"));
    			control.displayString = I18n.format("villageinfo.configpanel.button.toggle") + mod.getStatus();
        	}
        });
	}

}