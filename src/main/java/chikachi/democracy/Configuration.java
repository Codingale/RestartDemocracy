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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.*;

public class Configuration {
    private static File configFile;
    private static File timestampFile;

    private static long timestamp = 0;
    private static long cooldownEnd = 0;
    private static int voteTime = 30;
    private static boolean enableDiscord = true;
    private static int votesRequired = 50;
    private static int cooldown = 20;
    private static String command = "restart";

    static void onPreInit(FMLPreInitializationEvent event) {
        File directory = new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "Chikachi");
        //noinspection ResultOfMethodCallIgnored
        directory.mkdirs();

        configFile = new File(directory, Constants.MODID + ".json");
        timestampFile = new File(directory, Constants.MODID + "_timestamp");

        load();
    }

    private static void load() {
        if (timestampFile != null && timestampFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(timestampFile));
                String line = reader.readLine();
                timestamp = Long.parseLong(line);
                cooldownEnd = timestamp + (cooldown * 60 * 1000);
                reader.close();
            } catch (IOException | NumberFormatException e) {
                RestartDemocracy.Log("Error reading timestamp file", true);
                e.printStackTrace();
            }
        }

        if (configFile == null) {
            return;
        }

        if (!configFile.exists()) {
            try {
                JsonWriter writer = new JsonWriter(new FileWriter(configFile));
                writer.setIndent("  ");

                writer.beginObject();

                writer.name("command");
                writer.value(command);

                writer.name("voteTime");
                writer.value(voteTime);

                writer.name("enableDiscord");
                writer.value(enableDiscord);

                writer.name("votesRequired");
                writer.value(votesRequired);

                writer.name("cooldown");
                writer.value(cooldown);

                writer.endObject();

                writer.close();
            } catch (IOException e) {
                RestartDemocracy.Log("Error generating default config file", true);
                e.printStackTrace();
            }
        } else {
            try {
                JsonReader reader = new JsonReader(new FileReader(configFile));
                String name;

                reader.beginObject();
                while (reader.hasNext()) {
                    name = reader.nextName();
                    if (name.equalsIgnoreCase("command") && reader.peek() == JsonToken.STRING) {
                        command = reader.nextString();
                    } else if (name.equalsIgnoreCase("voteTime") && reader.peek() == JsonToken.NUMBER) {
                        voteTime = reader.nextInt();
                    } else if (name.equalsIgnoreCase("enableDiscord") && reader.peek() == JsonToken.BOOLEAN) {
                        enableDiscord = reader.nextBoolean();
                    } else if (name.equalsIgnoreCase("votesRequired") && reader.peek() == JsonToken.NUMBER) {
                        votesRequired = reader.nextInt();
                    } else if (name.equalsIgnoreCase("cooldown") && reader.peek() == JsonToken.NUMBER) {
                        cooldown = reader.nextInt();
                        cooldownEnd = timestamp + (cooldown * 60 * 1000);
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } catch (IOException e) {
                RestartDemocracy.Log("Error reading config file", true);
                e.printStackTrace();
            }
        }
    }

    public static void writeTimestamp() {
        if (timestampFile == null) {
            return;
        }

        try {
            FileWriter writer = new FileWriter(timestampFile);
            writer.write(Long.toString(System.currentTimeMillis()));
            writer.close();
        } catch (IOException e) {
            RestartDemocracy.Log("Error writing timestamp file", true);
            e.printStackTrace();
        }
    }

    public static boolean isDiscordEnabled() {
        return enableDiscord;
    }

    public static int getVoteTime() {
        return voteTime;
    }

    public static long getCooldownEnd() {
        return cooldownEnd;
    }

    public static int getVotesRequired() {
        return votesRequired;
    }

    public static String getCommand() {
        return command;
    }
}
