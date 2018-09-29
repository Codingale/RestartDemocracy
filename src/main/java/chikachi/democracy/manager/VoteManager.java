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

package chikachi.democracy.manager;

import chikachi.democracy.Configuration;
import chikachi.democracy.DemocracyCommandSender;
import chikachi.democracy.RestartDemocracy;
import chikachi.democracy.Vote;
import chikachi.democracy.timer.CooldownTimerTask;
import chikachi.democracy.timer.VoteTimerTask;
import net.minecraft.util.text.TextComponentString;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class VoteManager {
    private static VoteManager instance;

    public boolean postedCooldown = false;
    private boolean ignoreCooldown = true;

    private int voteRequired = 0;
    private List<String> votes = new ArrayList<>();

    private Timer timer = new Timer();

    private CooldownTimerTask cooldownTask;
    private VoteTimerTask voteTask;

    private VoteManager() {
    }

    public static VoteManager getInstance() {
        if (instance == null) {
            instance = new VoteManager();
        }

        return instance;
    }

    public void onVote(Vote vote) {
        if (this.isCoolingDown()) {
            int cooldownEnd = Configuration.getCooldownEnd();
            if (cooldownEnd > 0) {
                long now = System.currentTimeMillis();
                if (cooldownEnd > now) {
                    if (!this.postedCooldown) {
                        this.broadcastMessage(
                                String.format(
                                        "Currently in cooldown - Try again in %s",
                                        timeBeautifier(cooldownEnd - now)
                                )
                        );
                        this.postedCooldown = true;

                        if (this.cooldownTask != null) {
                            return;
                        }

                        this.cooldownTask = new CooldownTimerTask();
                        this.timer.schedule(this.cooldownTask, 60000);
                    }
                    return;
                }

                this.ignoreCooldown = true;
            }
        }

        if (this.votes.contains(vote.toString())) {
            return;
        }

        boolean voteRunning = this.isRunning();

        if (!voteRunning) {
            this.voteRequired = (int) (RestartDemocracy.minecraftServer.getCurrentPlayerCount() * (Configuration.getVotesRequired() / 100f));
            if (this.voteRequired < 2) {
                this.onVoteSuccess();
                return;
            }
            startVote();
        }

        this.votes.add(vote.toString());

        if (this.votes.size() >= this.voteRequired) {
            this.onVoteSuccess();
            return;
        }

        if (voteRunning) {
            this.broadcastMessage(
                    "Vote " + this.votes.size() + "/" + this.voteRequired
            );
        } else {
            this.broadcastMessage(
                    String.format(
                            "Vote on server restart have begun!\nVote running for %d seconds\nVote by using !%s or !vr",
                            Configuration.getVoteTime(),
                            Configuration.getCommand()
                    )
            );
        }
    }

    private void broadcastMessage(String message) {
        if (message == null || message.trim().length() == 0) {
            return;
        }

        // Minecraft
        RestartDemocracy.minecraftServer.sendMessage(new TextComponentString(message));
    }

    private String timeBeautifier(long milliseconds) {
        String response = "";

        int seconds = (int) Math.floor(milliseconds / 1000);
        int minutes = (int) Math.floor(seconds / 60);

        if (minutes > 0) {
            seconds -= minutes * 60;

            int hours = (int) Math.floor(minutes / 60);
            if (hours > 0) {
                minutes -= hours * 60;

                response = (hours < 10 ? "0" : "") + hours + ":";
            }

            response += (minutes < 10 ? "0" : "") + minutes + ":";
        } else {
            return seconds + " seconds";
        }

        return response + (seconds < 10 ? "0" : "") + seconds;
    }

    private void startVote() {
        if (this.isRunning()) {
            return;
        }

        this.voteTask = new VoteTimerTask();
        this.timer.schedule(this.voteTask, Configuration.getVoteTime() * 1000);
    }

    private void onVoteSuccess() {
        this.broadcastMessage("Server is restarting!");
        RestartDemocracy.minecraftServer.getCommandManager().executeCommand(new DemocracyCommandSender(), "stop");
    }

    public void onVoteEnd() {
        this.voteTask = null;
        this.votes.clear();

        this.broadcastMessage(
                "Vote on server restart have ended!\nNot enough votes to restart."
        );
    }

    private boolean isCoolingDown() {
        if (!this.ignoreCooldown) {
            int cooldownEnd = Configuration.getCooldownEnd();
            if (cooldownEnd > 0) {
                long now = System.currentTimeMillis();
                if (cooldownEnd > now) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isRunning() {
        return this.voteTask != null;
    }
}
