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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.districtmeps.dbot.commands.HelloWorld;
import com.districtmeps.dbot.objects.Command;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandManager {

    // Command list mapped with their invoke
    private final Map<String, Command> commands = new HashMap<>();

    CommandManager(EventWaiter waiter){
        addCommand(new HelloWorld());
    }

    // Checks if the command is in the list
    private void addCommand(Command command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }

    /**
     * 
     * @return Collection of Commands
     */
    public Collection<Command> getCommands() {
        return commands.values();
    }

    /**
     * 
     * @param type
     * @return List of Commands of a specific int type
     */
    public List<Command> getCommands(int type){
        List<Command> newCommands = new ArrayList<>();

        commands.forEach((k, v)->{
            if(v.getType() == type){
                newCommands.add(v);
            }
        });

        return newCommands;
    }

    /**
     * Used to get a command of a specific invoke/name
     * @param name
     * @return Command
     */
    public Command getCommand(@NotNull String name) {
        return commands.get(name);
    }

    /**
     * 
     * Callable from inside the package
     * 
     * <p>Uses the message event to read what the command is then check if the command exists. If so, it runs the command
     * 
     * @param event
     */
    void handleCommand(GuildMessageReceivedEvent event) {
        
        // Cuts out the prefix. We know it's there since it was a check in the listener
        // Then it splits it at every space (" ")
        final String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Constants.PREFIX), "").split("\\s+");
        
        // Seperate out the invoke
        final String invoke = split[0].toLowerCase();

        // If the invoke is a command
        // creates a list from the first split now without the 0th index which was the invoke
        // Then "handles" the command, or runs it.
        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            event.getChannel().sendTyping().queue();
            commands.get(invoke).handle(args, event);

        }
    }
    
}
