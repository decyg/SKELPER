import com.sun.prism.paint.Color
import command.CommandException
import command.CommandHelper
import command.CommandList
import main.ClientSingleton
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import plugin.CommandTag

import plugin.PluginException
import plugin.PluginInfo
import plugin.PluginUtil
import sx.blah.discord.api.IListener
import sx.blah.discord.handle.impl.obj.Role
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.HTTP429Exception
import sx.blah.discord.util.MissingPermissionsException
import java.awt.Color
import java.lang.reflect.Field

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

        File emoteList = PluginUtil.getDataFolder("emote")

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
        File emoteList = PluginUtil.getDataFolder("emote")

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

        String colour = vargs.get(0);

        Color actColour;

        try {
            // get color by hex or octal value
            actColour = Color.decode("#" + colour);
        } catch (NumberFormatException nfe) {
            try {
                // try to get a color by name using reflection
                Field f = Color.class.getField(colour.toLowerCase());

                actColour = (Color) f.get(null);
            } catch (Exception ce) {
                // if we can't get any color return black
                actColour = Color.black;
            }
        }

        System.out.println(actColour)

        IUser incomingUser = chatSource.author

        boolean hasRole = false
        for(IRole r : incomingUser.getRolesForGuild(chatSource.guild)){
            System.out.println(r.name)
            if(r.name.equals(incomingUser.ID)){
                hasRole = true;
                break;
            }
        }

        if(hasRole){

            for(IRole r : incomingUser.getRolesForGuild(chatSource.guild)){

                if(r.name.equals(incomingUser.ID)){
                   r.changeColor(actColour)
                    break;
                }
            }

        } else {
            IRole newRole
            IRole[] roleArray
            try {
                List<IRole> roleList = incomingUser.getRolesForGuild(chatSource.guild)

                newRole = chatSource.guild.createRole()

                roleList.add(newRole)

                roleArray = new IRole[roleList.size()];
                roleArray = roleList.toArray(roleArray)

                chatSource.guild.editUserRoles(incomingUser, roleArray)

               // Thread.sleep(1000)
                newRole.changeName(incomingUser.ID)

               // ColourMe(chatSource, vargs)
                //newRole.changeColor(actColour)

                //println(chatSource.guild.getRoleByID(incomingUser.discriminator))
                /*
                for(IRole r : incomingUser.getRolesForGuild(chatSource.guild)){
                    println(r.name)
                    if(r.name.equals(incomingUser.discriminator)){
                        r.changeColor(actColour)
                        break;
                    }
                }*/
            } catch (Exception e){
                if(newRole) {
                    newRole.delete()
                }
                throw new CommandException("Cloudflare has gone weird again")
            }



        }

    }

}