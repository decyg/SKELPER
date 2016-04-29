import com.sun.prism.paint.Color
import command.CommandException
import command.CommandHelper
import command.CommandList
import main.HelperFuncs
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import plugin.CommandTag

import plugin.PluginException
import plugin.PluginInfo
import sx.blah.discord.api.IListener
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.HTTP429Exception
import sx.blah.discord.util.MissingPermissionsException
import java.awt.Color

@PluginInfo(
        name="Fun commands",
        description="Contains fun commands",
        author="Declan Neilson",
        version="1.0"
)
class Plugin {

    @CommandTag(
            prettyName="Add Emote",
            channelScope="all",
            commandPattern="addemote|ae [emote name]",
            description="Add an emote to the emote list (send a message with an image and !ae <emotename>)"
    )
    def AddEmote(IMessage chatSource, List<String> vargs){

        File emoteList = HelperFuncs.getDataFolder("emote")

        for (File f : emoteList.listFiles()) {
            if(vargs.get(0).equals(FilenameUtils.removeExtension(f.getName()))){
                throw new CommandException("An emote already exists with that name!");
            }
        }

        if(chatSource.getAttachments().size() != 1){
            throw new CommandException("You haven't attached one photo to the message")
        }

        IMessage.Attachment picToAdd = chatSource.getAttachments().get(0);

        URLConnection conn = new URL(picToAdd.getUrl()).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        conn.connect();

        String extension = FilenameUtils.getExtension(picToAdd.getFilename())

        File copyLoc = new File(emoteList.path + "/" + vargs.get(0) + "." + extension)

        FileUtils.copyInputStreamToFile(conn.getInputStream(), copyLoc);

        CommandHelper.sM(chatSource, "Successfully added emote: \"" + vargs.get(0) + "\"");

    }

    @CommandTag(
            prettyName="Emote",
            channelScope="all",
            commandPattern="emote|e <emote name>",
            description="Spruce up your chatting experience with an exciting image emoticon"
    )
    def Emote(IMessage chatSource, List<String> vargs){
        File emoteList = HelperFuncs.getDataFolder("emote")

        if (vargs.size() == 0) {

            String stringelist = "";

            for (File f : emoteList.listFiles()) {
                stringelist = stringelist + FilenameUtils.removeExtension(f.getName()) + ", ";
            }

            CommandHelper.sM(chatSource, "Available emotes: " + stringelist);

        } else {

            for (File f : emoteList.listFiles()) {
                if (FilenameUtils.removeExtension(f.getName()).equals(vargs[0].toLowerCase())) {
                    try {
                        chatSource.getChannel().sendFile(f);
                    } catch (IOException | MissingPermissionsException | DiscordException | HTTP429Exception e) {
                        throw new CommandException("Couldn't find that emote");
                    }
                    return;
                }
            }

            throw new CommandException("Couldn't find that emote");

        }
    }

    @CommandTag(
            prettyName="Colour me",
            channelScope="all",
            commandPattern="colourme|cm [colour]",
            description="[WIP]"
    )
    def ColourMe(IMessage chatSource, List<String> vargs){

    }

}