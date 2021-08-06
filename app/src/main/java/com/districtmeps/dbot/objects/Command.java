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

package com.districtmeps.dbot.objects;

import java.util.List;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/** Used to amalgamate and order the tons of commands
 * 
 */
public interface Command {
    void handle(List<String> args, GuildMessageReceivedEvent event); 
    String getHelp();
    String getInvoke();
    int getType();
}