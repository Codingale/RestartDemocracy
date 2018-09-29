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


import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

@Config(modid = Constants.MODID, name = Constants.MODNAME)
public class Configuration {

    @Config.Comment("How long after a server restart should we wait before letting another one happen? (in seconds) [default: 10 minutes]")
    @Config.Name("Cooldown")
    public static int cooldownEnd = 60 * 10;

    @Config.Comment("Restart command other than (!vr) [default:!restart]")
    @Config.Name("Command")
    public static String command = "!restart";


    @Config.Name("VoteTime")
    @Config.Comment("Total time to vote")
    public static int voteTime = 30;

    @Config.Name("Percent to pass")
    @Config.Comment("How much of the server needs to vote to restart?")
    public static int votesRequired = 50;

    public static int getVoteTime() {
        return voteTime;
    }

    public static int getCooldownEnd() {
        return cooldownEnd;
    }

    public static int getVotesRequired() {
        return votesRequired;
    }

    public static String getCommand() { return command;}
}
