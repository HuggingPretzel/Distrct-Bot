/**
 *      Copyright 2021 Daniel Sanchez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.districtmeps.dbot;

import com.districtmeps.dbot.config.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter{

    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(Listener.class);

    Listener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info(String.format("Logged in as %#s\n", event.getJDA().getSelfUser()));
    }
    

    // Console display of messages seen by bot
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().isWebhookMessage()) {
            return;
        }

        User author = event.getAuthor();
        String message = event.getMessage().getContentRaw();

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();

            logger.info(String.format("(%s) [%s] <%#s>: %s", guild.getName(), textChannel.getName(), author, message));

        } else if (event.isFromType(ChannelType.PRIVATE)) {
            logger.info(String.format("[PRIV]<%#s>: %s", author, message));
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        // Checks for "fake message"/webhooks or a bot (dont care bout em)
        if (event.getMessage().isWebhookMessage() || event.getAuthor().isBot()) return;

        // Listen for owner shutdown call
        if (event.getMessage().getContentRaw().equalsIgnoreCase(Constants.PREFIX + "shutdown")
                && event.getAuthor().getId().equals(Config.getInstance().getString("owner"))) {
            shutdown(event.getJDA());
            return;
        }

        // Finally if starts with prefix send to CommandManager
        if (event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
            manager.handleCommand(event);
        }

    }


    // To shutdown bot from discord
    private void shutdown(JDA jda) {
        jda.shutdown();
        System.exit(0);
    }
}
