package ga.alexhalogen.villageinfo;

import java.util.List;
import java.util.Collections;
import javax.annotation.Nullable;


import net.minecraft.command.ServerCommandManager;
import net.minecraft.command.*;
import net.minecraft.nbt.NBTTagCompound;
// import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class VillageInfoCommand extends net.minecraft.command.CommandBase { 

	
    public String getName()
    {
        return "vi";
    }


    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getUsage(ICommandSender sender)
    {
        return "commands.difficulty.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender
                ,String[] args) throws CommandException {
        
        if (sender instanceof EntityPlayer) {
        
            BlockPos loc = sender.getPosition();
            BlockPos cen;
            Village villageIn = sender.getEntityWorld().getVillageCollection()
                    .getNearestVillage(new BlockPos((EntityPlayer) sender), 1);
            if (villageIn != null) {
                NBTTagCompound nbt = new NBTTagCompound();
                villageIn.writeVillageDataToNBT(nbt);
                BlockPos vCenter = villageIn.getCenter();
                BlockPos playerPos = sender.getPosition();
                int distance = (int) Math.sqrt(Math.pow((playerPos.getX()
                        - vCenter.getX()), 2)
                        + Math.pow((playerPos.getY() - vCenter.getY()), 2)
                        + Math.pow((playerPos.getZ() - vCenter.getZ()), 2));


                if (distance < villageIn.getVillageRadius()) {
                    String centerPos = vCenter.getX() + "/" + vCenter.getY()
                        + "/" + vCenter.getZ();
                    int population = villageIn.getNumVillagers();
                    int populationCap = (int) 
                        ((villageIn.getNumVillageDoors() * 0.35D));
                    int numGolems = nbt.getInteger("Golems");
                    int repu = villageIn.getPlayerReputation(sender.getName());

                    int maxGolems = (int) Math.floor((population / 10));
                    boolean isSpring = (population < populationCap) 
                        && villageIn.isMatingSeason();
                    boolean fuhyohigai;
                    boolean insideGolemArea;
                    boolean enoughDoors;

                    if (repu <= -15) {
                        fuhyohigai = true;
                    } else {
                        fuhyohigai = false;
                    }

                    float xDiff = sender.getPosition().getX() - vCenter.getX();
                    float yDiff = sender.getPosition().getY() - vCenter.getY();
                    float zDiff = sender.getPosition().getZ() - vCenter.getZ();

                    if ( ( (yDiff < 3) && (yDiff >= -3) ) && ((zDiff < 8)
                        && (zDiff >= -8)) && ((xDiff < 8) && (xDiff >= -8)) ) {

                        insideGolemArea = true;

                    } else
                        insideGolemArea = false;

                    if (villageIn.getNumVillageDoors() < 21)
                        enoughDoors = false;
                    else
                        enoughDoors = true;

                    sender.sendMessage(new TextComponentString(
                            "§eCenter: " + centerPos + ", Radius: "
                                + villageIn.getVillageRadius() + "§r"));
                    sender.sendMessage(new TextComponentString(
                            (enoughDoors ? "§b" : "§d") + "Houses: "
                                + villageIn.getNumVillageDoors() + "§r"));

                    sender.sendMessage(new TextComponentString(
                            (isSpring ? "§b" : "§d") + "Villagers: "
                            + population + ", (Max " + populationCap + ")§r"));

                    sender.sendMessage(new TextComponentString(
                            (numGolems < maxGolems ? "§b" : "§d" ) + "Golems: "
                            + numGolems + " (Max " + maxGolems +
                            ")" + "§r" 
                            + (insideGolemArea ? 
                                " §e(Inside Golem Spawning Area)§r" : "")));

                    sender.sendMessage(
                            new TextComponentString((fuhyohigai ? "§d" : "§b")
                                + "Reputation: " + repu + "§r"));
                } else {
                    sender.sendMessage(new TextComponentString(
                            "§dYou are not inside a Village Radius§r"));
                }

            } else {
                sender.sendMessage(new TextComponentString(
                        "§dYou are not inside a Village Radius§r"));
            }

        } else {
            sender.sendMessage(new TextComponentString(
                    "Only players can use this command"));
        }
    }
}