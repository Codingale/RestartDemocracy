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

import chikachi.democracy.*;
import chikachi.democracy.enumerator.UserType;
import chikachi.democracy.manager.VoteManager;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscordEventListener {
    public void onMessageReceived(NBTTagCompound user, String message) {
        if (!Configuration.isDiscordEnabled()) {
            return;
        }

        if (message.startsWith("!")) {
            List<String> args = new ArrayList<>(Arrays.asList(message.substring(1).split(" ")));
            String cmd = args.remove(0);

            if (cmd.equals(Configuration.getCommand())) {
                VoteManager.getInstance().onVote(new Vote(
                        UserType.DISCORD,
                        user.getString("id")
                ));
            }
        }
    }
}
