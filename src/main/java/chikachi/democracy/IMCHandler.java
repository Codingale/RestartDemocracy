package chikachi.democracy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class IMCHandler {
    public static void onMessageReceived(FMLInterModComms.IMCMessage imcMessage) {
        if (imcMessage.getSender().equalsIgnoreCase("DiscordIntegration")) {
            if (imcMessage.key.equalsIgnoreCase("event")) {
                if (imcMessage.isNBTMessage()) {
                    NBTTagCompound tagCompound = imcMessage.getNBTValue();

                    if (tagCompound.hasKey("type") && tagCompound.getString("type").equalsIgnoreCase("chat")) {
                        NBTTagCompound user = tagCompound.getCompoundTag("user");
                        String message = tagCompound.getString("message");

                        RestartDemocracy.discordEventListener.onMessageReceived(user, message);
                    }
                }
            }
        }
    }
}