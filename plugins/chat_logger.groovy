/**
 * MIT License
 *
 * Copyright (c) 2016 Declan Neilson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import command.ChatCommand
import command.CommandException
import command.CommandHelper
import command.CommandList
import groovy.transform.stc.ClosureParams

import plugin.CommandTag

import plugin.ListenerTag
import plugin.PluginException
import plugin.PluginInfo
import sx.blah.discord.api.Event
import sx.blah.discord.api.EventDispatcher
import sx.blah.discord.api.EventSubscriber
import sx.blah.discord.api.IListener
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent
import sx.blah.discord.handle.impl.events.MessageReceivedEvent
import sx.blah.discord.handle.impl.events.TypingEvent
import sx.blah.discord.handle.obj.IMessage

import java.util.function.Consumer
import java.util.stream.StreamSupport

@PluginInfo(
        name="Chat logger",
        description="Logs chat commands and adds a !history command",
        author="Declan Neilson",
        version="1.0"
)
class Plugin {

    @CommandTag(
        prettyName="Search History",
        channelScope="all",
        commandPattern="history|h [search term] <other terms>...",
        description="Use \"searchterm\" to search for a term"
    )
    def SearchHistory(IMessage chatSource, List<String> vargs){

        List<String> searchterms = vargs;

        String init = "Searching for lines in " + chatSource.getChannel().getName() + " containing: ";

        int i = 0;
        for (String s : searchterms) {
            init = init + "\"" + s + "\"" + (i == searchterms.size() - 1 ? "" : " and ");
            i++;
        }

        CommandHelper.sM(chatSource, init);

        chatSource.getChannel().getMessages().setCacheCapacity(-1)

        while(chatSource.getChannel().getMessages().load(100)){
            println(chatSource.getChannel().getMessages().size())
        }

        int n = 0
        String out = "\n";

        StreamSupport.stream(chatSource.getChannel().getMessages().spliterator(), false).forEach({ msg ->

            boolean hasall = true;
            for (String s : searchterms) {
                if (!(msg.content.toLowerCase()).contains(s.toLowerCase())) {
                    hasall = false
                    break
                }
            }

            if (hasall && n < 10) {
                out = out + ("```" + "\\[" + msg.timestamp + "] " + msg.author.getName() + ": " + msg.content + "```") + "\n";
                n++;
            }
        })

        if (n == 0) {
            CommandHelper.sM(chatSource, "No results found");
        } else {
            CommandHelper.sM(chatSource, out);
        }

        if(n==10)
            CommandHelper.sM(chatSource, "More than 10 results found")

    }

}