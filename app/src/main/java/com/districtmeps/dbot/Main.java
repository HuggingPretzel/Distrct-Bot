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


import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

import javax.security.auth.login.LoginException;

import com.districtmeps.dbot.config.Config;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class Main {

    // Used to hold application and wait for user imput
    // Creating multiple instances of EventWaiter can cause memory issues so it is passed around to where it's needed
    private static final EventWaiter waiter = new EventWaiter();

    // Logging
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    

    // Init of CommandManager
    private static final CommandManager commandManager = new CommandManager(waiter);

    // Init of Listener
    private static final Listener listener = new Listener(commandManager);

    // Main JDA Object that I like to use across the application if I need something
    private static JDA jda = null;
    
    public static void main(String[] args) {
        

        // Uses config class to safely load information from json file and store it for later use
        Config config = null;
        try {
            config = new Config(new File("botconfig.json"));
        } catch (IOException e1) {
            logger.error("Could not load config file", e1);
        }

        logger.info("Bot is Starting");

        logger.info("User Agent Set");
        // Sets UserAgent when using WebUtils POST/GET
        WebUtils.setUserAgent("Mozilla/5.0 District JDA Bot/Danboi#1962");

        // Sets default Discord Embed settings. (I like using Embeds)
        EmbedUtils.setEmbedBuilder(() -> new EmbedBuilder()
            .setColor(Color.red)
            .setFooter(Constants.NAME, null)
            .setTimestamp(Instant.now()));
        
        try {
            jda = JDABuilder.createDefault(config.getString("token"))
                .setActivity(Activity.listening("smooth beats"))
                .addEventListeners(waiter, listener)
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS)
                .build();
            
        } catch (LoginException e) {
            logger.error("Could not log in", e);
        }

        logger.info("Ready");

    }

    public static JDA getJda() {
        return jda;
    }
}
