/**
 * Copyright (C) 2016 Chikachi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package chikachi.democracy;

import chikachi.democracy.event.DiscordEventListener;
import chikachi.democracy.event.MinecraftEventListener;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Constants.MODID,
        name = Constants.MODNAME,
        version = Constants.VERSION,
        acceptableRemoteVersions = "*",
        dependencies = "after:DiscordIntegration",
        serverSideOnly = true
)
public class RestartDemocracy {
    @Mod.Instance
    public static RestartDemocracy instance;

    private static final Logger logger = LogManager.getLogger(Constants.MODID);
    private static final MinecraftEventListener minecraftEventListener = new MinecraftEventListener();
    static final DiscordEventListener discordEventListener = new DiscordEventListener();
    public static MinecraftServer minecraftServer;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        Configuration.onPreInit(event);

        MinecraftForge.EVENT_BUS.register(minecraftEventListener);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        minecraftServer = event.getServer();

        if (Loader.isModLoaded("DiscordIntegration")) {
            Log("Hooking into DiscordIntegration", false);
            FMLInterModComms.sendRuntimeMessage(this, "DiscordIntegration", "registerListener", "");
        }
    }

    @Mod.EventHandler
    public void imcReceived(FMLInterModComms.IMCEvent event) {
        event.getMessages().forEach(IMCHandler::onMessageReceived);
    }

    static void Log(String message, boolean warning) {
        logger.log(warning ? Level.WARN : Level.INFO, "[" + Constants.VERSION + "] " + message);
    }
}
