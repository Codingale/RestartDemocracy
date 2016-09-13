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

package chikachi.democracy.event;

import chikachi.democracy.Configuration;
import chikachi.democracy.IMCHandler;
import chikachi.democracy.RestartDemocracy;
import chikachi.democracy.Vote;
import chikachi.democracy.enumerator.UserType;
import chikachi.democracy.manager.VoteManager;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class MinecraftEventListener {
    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        if (event.getPlayer() == null) return;

        String cmd = event.getMessage().trim();

        if (cmd.equalsIgnoreCase("!" + Configuration.getCommand())) {
            VoteManager.getInstance().onVote(new Vote(
                    UserType.MINECRAFT,
                    event.getPlayer().getGameProfile().getId().toString()
            ));
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        List<FMLInterModComms.IMCMessage> imcMessages = FMLInterModComms.fetchRuntimeMessages(RestartDemocracy.instance);
        imcMessages.forEach(IMCHandler::onMessageReceived);
    }
}
