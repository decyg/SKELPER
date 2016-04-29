import command.ChatCommand
import command.CommandException
import command.CommandHelper
import command.CommandList
import groovy.transform.stc.ClosureParams
import main.HelperFuncs
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