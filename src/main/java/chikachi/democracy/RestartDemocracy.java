/**
 * Copyright (C) 2016 Chikachi
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package chikachi.democracy;

import chikachi.democracy.event.RestartEventHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Constants.MODID,
        name = Constants.MODNAME,
        version = Constants.VERSION,
        acceptableRemoteVersions = "*",
        serverSideOnly = true
)
public class RestartDemocracy
{
    @Mod.Instance
    public static RestartDemocracy instance;

    public static Logger logger;
    private static final RestartEventHandler RestartHandler = new RestartEventHandler();
    public static MinecraftServer minecraftServer;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("Loading mod in pre-int");
        MinecraftForge.EVENT_BUS.register(RestartHandler);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        minecraftServer = event.getServer();
    }
}
