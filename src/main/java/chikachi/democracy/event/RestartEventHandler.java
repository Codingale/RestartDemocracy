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

package chikachi.democracy.event;

import chikachi.democracy.Configuration;
import chikachi.democracy.RestartDemocracy;
import chikachi.democracy.Vote;
import chikachi.democracy.enumerator.UserType;
import chikachi.democracy.manager.VoteManager;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RestartEventHandler
{
    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event)
    {
        if (event.getPlayer() == null)
            return;

        String cmd = event.getMessage().trim();
        RestartDemocracy.logger.info(cmd);
        RestartDemocracy.logger.info(cmd);
        if (cmd.equalsIgnoreCase("!vr") | cmd.equalsIgnoreCase(Configuration.getCommand()))
        {
            VoteManager.getInstance().onVote(new Vote(
                    UserType.MINECRAFT,
                    event.getPlayer().getName()
            ));
        }
    }
}
