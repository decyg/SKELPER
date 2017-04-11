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



import com.google.gson.Gson
import command.CommandException
import command.CommandHelper
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import plugin.CommandTag
import plugin.PluginInfo
import plugin.PluginUtil
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.EmbedBuilder
import sx.blah.discord.util.MissingPermissionsException

import java.awt.*
import java.lang.reflect.Field
import java.util.List

@PluginInfo(
        name="Fun commands",
        description="Contains fun commands",
        author="Declan Neilson",
        version="1.0"
)
class fun_commands {

    @CommandTag(
            prettyName="Spoiler",
            channelScope="all",
            commandPattern="spoiler|s [spoiler to hide]",
            description="Hide a spoiler in an embed"
    )
    def AddSpoiler(IMessage chatSource, List<String> vargs){

        chatSource.delete()

        EmbedBuilder spoilerEmbed = new EmbedBuilder()

        spoilerEmbed.withTitle("Spoiler")

        spoilerEmbed.appendField("Click below to see spoiler", "[SPOILER](http://" + URLEncoder.encode(vargs.get(0), "UTF-8") + ")", true)

        CommandHelper.sM(chatSource, spoilerEmbed.build())

    }

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
                if (FilenameUtils.removeExtension(f.getName()) == vargs[0].toLowerCase()) {
                    try {
                        chatSource.getChannel().sendFile(f);
                    } catch (IOException | MissingPermissionsException | DiscordException e) {
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

                roleArray = new IRole[roleList.size()]
                roleArray = roleList.toArray(roleArray)

                chatSource.guild.editUserRoles(incomingUser, roleArray)

               // Thread.sleep(1000)
                newRole.changeName(incomingUser.ID)

               // ColourMe(chatSource, vargs)
                newRole.changeColor(actColour)

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

    private static final String BASE_URL = "https://owapi.net"
    private static final Gson gObj = new Gson()

    // Overwatch Icon URLs

    private static final String COMP_BRONZE = "https://hydra-media.cursecdn.com/overwatch.gamepedia.com/8/89/Badge_1_Bronze.png"
    private static final String COMP_SILVER = "https://hydra-media.cursecdn.com/overwatch.gamepedia.com/b/bb/Badge_2_Silver.png"
    private static final String COMP_GOLD = "https://hydra-media.cursecdn.com/overwatch.gamepedia.com/b/b8/Badge_3_Gold.png"
    private static final String COMP_PLATINUM = "https://hydra-media.cursecdn.com/overwatch.gamepedia.com/f/f8/Badge_4_Platinum.png"
    private static final String COMP_DIAMOND = "https://hydra-media.cursecdn.com/overwatch.gamepedia.com/2/2f/Badge_5_Diamond.png"
    private static final String COMP_MASTER = "https://hydra-media.cursecdn.com/overwatch.gamepedia.com/f/f0/Badge_6_Master.png"
    private static final String COMP_GRANDMASTER = "https://hydra-media.cursecdn.com/overwatch.gamepedia.com/8/87/Badge_7_Grandmaster.png"
    private static final String COMP_TOP500 = "https://hydra-media.cursecdn.com/overwatch.gamepedia.com/7/73/Badge_8_Top_500.png"


    // Overwatch POGOs

    private class POGO_STATS {
        POGO_STATS_REGION eu
        POGO_STATS_REGION us
        POGO_STATS_REGION kr
        POGO_STATS_REGION any
    }

    private class POGO_STATS_DSTATS_INNER {

        class OVERALL_STATS {
            int win_rate
            int level
            int prestige
            String avatar
            int wins
            int games
            int comprank
            int losses
        }

        class GAME_STATS {
            float time_played
            float healing_done_most_in_game
            float eliminations_most_in_game
            float damage_done_most_in_game

            // jesus christ fuck this there's so god damn many
        }

        class AVERAGE_STATS {
            float healing_done_avg
            float eliminations_avg
            float damage_done_avg
            float deaths_avg

        }

        OVERALL_STATS overall_stats
        GAME_STATS game_stats
        boolean competitive
        AVERAGE_STATS average_stats

    }

    private class POGO_STATS_DSTATS {
        POGO_STATS_DSTATS_INNER competitive
        POGO_STATS_DSTATS_INNER quickplay
    }

    private class POGO_STATS_DHEROES {

    }

    private class POGO_STATS_DACHIEV {

    }

    private class POGO_STATS_REGION {
        POGO_STATS_DSTATS stats
        POGO_STATS_DHEROES heroes
        POGO_STATS_DACHIEV achievements
    }


    @CommandTag(
            prettyName="Overwatch stats",
            channelScope="all",
            commandPattern="overwatch|owa [battletag#discrim] <region>",
            description="Fetches a players stats and presents it in rich text "
    )
    def Overwatch(IMessage chatSource, List<String> vargs) {

        String raw_username = vargs.get(0)
        String region = "eu"

        if(vargs.size() == 2)
            region = vargs.get(1)

        String username = raw_username.replace("#", "-")

        String STATS_ROUTE = "/api/v3/u/$username/stats"

        String sFinal = BASE_URL + STATS_ROUTE

        try{


            URL url = new URL(sFinal)

            String pageText = IOUtils.toString(url, "UTF-8")

            POGO_STATS oRes = gObj.fromJson(pageText, POGO_STATS.class)

            POGO_STATS_DSTATS_INNER.OVERALL_STATS oStats_c = oRes.eu.stats.competitive.overall_stats
            POGO_STATS_DSTATS_INNER.OVERALL_STATS oStats_qp = oRes.eu.stats.quickplay.overall_stats
            POGO_STATS_DSTATS_INNER.GAME_STATS gStats_c = oRes.eu.stats.competitive.game_stats
            POGO_STATS_DSTATS_INNER.GAME_STATS gStats_qp = oRes.eu.stats.quickplay.game_stats
            POGO_STATS_DSTATS_INNER.AVERAGE_STATS aStats_c = oRes.eu.stats.competitive.average_stats
            POGO_STATS_DSTATS_INNER.AVERAGE_STATS aStats_qp = oRes.eu.stats.quickplay.average_stats

            EmbedBuilder returnEmbed = new EmbedBuilder()

            //returnEmbed.withTitle("$raw_username")
            returnEmbed.withAuthorName(raw_username)
            returnEmbed.withColor(255, 102, 0)
            returnEmbed.withAuthorIcon(oStats_qp.avatar)

            returnEmbed.appendDesc("[Overbuff](https://www.overbuff.com/players/pc/$username)")

            returnEmbed.appendField("Level", "$oStats_qp.level", true)
            returnEmbed.appendField("Skill Rating", "$oStats_c.comprank", true)

            returnEmbed.appendField(":trophy: COMPETITIVE", "$oStats_c.games games played ($oStats_c.win_rate% won) over $gStats_c.time_played hours.", false)

            returnEmbed.appendField("Average Eliminations", "$aStats_c.eliminations_avg", true)
            returnEmbed.appendField("Most Eliminations", "$gStats_c.eliminations_most_in_game", true)

            returnEmbed.appendField("Average Damage", "$aStats_c.damage_done_avg", true)
            returnEmbed.appendField("Most Damage", "$gStats_c.damage_done_most_in_game", true)

            returnEmbed.appendField("Average Healing", "$aStats_c.healing_done_avg", true)
            returnEmbed.appendField("Most Healing", "$gStats_c.healing_done_most_in_game", true)

            returnEmbed.appendField(":video_game: QUICK PLAY", "$oStats_qp.wins games won over $gStats_qp.time_played hours.", false)

            returnEmbed.appendField("Average Eliminations", "$aStats_qp.eliminations_avg", true)
            returnEmbed.appendField("Most Eliminations", "$gStats_qp.eliminations_most_in_game", true)

            returnEmbed.appendField("Average Damage", "$aStats_qp.damage_done_avg", true)
            returnEmbed.appendField("Most Damage", "$gStats_qp.damage_done_most_in_game", true)

            returnEmbed.appendField("Average Healing", "$aStats_qp.healing_done_avg", true)
            returnEmbed.appendField("Most Healing", "$gStats_qp.healing_done_most_in_game", true)

            returnEmbed.withFooterText("EU")


            // gonna do some real nasty switching/if stack for the icon


            String chosenIcon = ""

            switch (oStats_c.comprank) {
                case 1..1499:
                    chosenIcon = COMP_BRONZE
                    break
                case 1500..1999:
                    chosenIcon = COMP_SILVER
                    break
                case 2000..2499:
                    chosenIcon = COMP_GOLD
                    break
                case 2500..2999:
                    chosenIcon = COMP_PLATINUM
                    break
                case 3000..3499:
                    chosenIcon = COMP_DIAMOND
                    break
                case 3500..3999:
                    chosenIcon = COMP_MASTER
                    break
                case 4000..5000:
                    chosenIcon = COMP_GRANDMASTER
                    break
            }

            returnEmbed.withThumbnail(chosenIcon) // this is the comp ico

            //:trophy:
            //:video_game:

            CommandHelper.sM(chatSource, returnEmbed.build())

        } catch (Exception e){
            chatSource.reply("Something bad happened :/// try again, make sure your username is correct and is in the format Username#0001")
            e.printStackTrace()
        }




    }

}