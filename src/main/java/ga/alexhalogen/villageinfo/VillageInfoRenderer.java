package ga.alexhalogen.villageinfo;
import net.minecraft.client.Minecraft;
import net.minecraft.world.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import java.util.List;
import java.util.ArrayList;

import java.util.Collections;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.village.*;


// import net.minecraft.client.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
public class VillageInfoRenderer {
	public boolean enabledF3 = false;
	public boolean enabled = true;

	public void render(int screenwidth, int screenheight) {
		if (!enabledF3 || !enabled) return;

		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		
		if (player == null) return;
	
		Village villageIn = mc.getIntegratedServer()
				.getWorld(mc.player.dimension)
				.getVillageCollection().getNearestVillage(
					new BlockPos(player), 80);

		if (villageIn == null) return;

		// Getting Information of village

		NBTTagCompound nbt = new NBTTagCompound();
		villageIn.writeVillageDataToNBT(nbt);
		BlockPos vCenter = villageIn.getCenter();
		BlockPos playerPos = player.getPosition();
		int distance = (int) Math.sqrt(Math.pow((playerPos.getX()
				- vCenter.getX()), 2) + Math.pow((playerPos.getY()
				- vCenter.getY()), 2) + Math.pow((playerPos.getZ()
				- vCenter.getZ()), 2));

		String centerPos = "";
		int population,populationCap, numDoors;
		int numGolems,repu,maxGolems,radius;
		boolean isSpring,fuhyohigai,insideGolemArea,enoughDoors;
		numDoors = villageIn.getNumVillageDoors();
		radius = villageIn.getVillageRadius();
		centerPos = vCenter.getX() + "/"
					+ vCenter.getY() + "/"
					+ vCenter.getZ();

		population = villageIn.getNumVillagers();
		populationCap = (int) ((numDoors * 0.35D));
		numGolems = nbt.getInteger("Golems");
		repu = villageIn.getPlayerReputation(player.getName());

		maxGolems = (int) Math.floor((population / 10));
		isSpring = (population < populationCap) && villageIn.isMatingSeason();

		if (repu <= -15) {
			fuhyohigai = true;
		} else {
			fuhyohigai = false;
		}

		float xDiff = player.getPosition().getX() - vCenter.getX();
		float yDiff = player.getPosition().getY() - vCenter.getY();
		float zDiff = player.getPosition().getZ() - vCenter.getZ();

		if ( ( (yDiff < 3) && (yDiff >= -3) )
				&& ((zDiff < 8) && (zDiff >= -8))
				&& ((xDiff < 8) && (xDiff >= -8)) ) {
			insideGolemArea = true;
		} else
			insideGolemArea = false;

		if (villageIn.getNumVillageDoors() < 21)
			enoughDoors = false;
		else
			enoughDoors = true;


		List<String> outputStr = new ArrayList();
		GlStateManager.enableBlend();
		FontRenderer var13 = mc.fontRenderer;
		int height = var13.FONT_HEIGHT;

		int posY = (int) (0.99 * screenheight);

		outputStr.add("Center: " + centerPos + ", " + "Radius: " + radius);
		outputStr.add("Distance to Center: " + distance) ;
		outputStr.add("Villagers: " + population + " / Max: " + populationCap);
		outputStr.add("Golems: " + numGolems + " / " + maxGolems);
		outputStr.add("Houses: " + numDoors );
		outputStr.add("Reputation: " + repu);
		boolean popCapped = population >= populationCap;
		// boolean inCondole = !isSpring && !popCapped;
		boolean inCondole = !villageIn.isMatingSeason();
		String breeding = "";
		
		if (isSpring) {
			breeding = "OK";
		}
		else {
			if (popCapped) breeding += "Capped";
			if (inCondole && popCapped) breeding += " Condoling";
			if (inCondole && !popCapped) breeding += "Condoling";
		}
		outputStr.add("Breeding: " +  breeding);

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();

		for(int i=0; i<outputStr.size(); i++){
			// Only Drawing white text now
			var13.drawStringWithShadow(outputStr.get(i),2, posY-height
			*(outputStr.size()-i),16777215);

		}
		
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

}


