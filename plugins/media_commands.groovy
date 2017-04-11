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
import com.google.gson.reflect.TypeToken
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.FeedException
import command.CommandException
import command.CommandHelper
import main.ClientSingleton
import org.apache.commons.io.IOUtils
import plugin.CommandTag
import plugin.PluginInfo
import plugin.PluginUtil
import sx.blah.discord.api.events.IListener
import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.handle.impl.events.MessageReceivedEvent
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.util.EmbedBuilder

import java.util.concurrent.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@PluginInfo(
        name="Media commands",
        description="Contains commands related to scraping or viewing media",
        author="Declan Neilson",
        version="1.0"
)
class media_commands {

    // Constants

    private static final String BASE_STRING = "https://torrentapi.org/pubapi_v2.php?"
    private static final Gson gObj = new Gson()


    // Endpoints

    private static final String ENDP_TOKEN = "get_token=get_token"
    private static final String ENDP_SEARCH = "mode=search&search_string=%s&token=%s&format=json_extended&sort=seeders&category=14;48;17;44;45;47;42;46;18;41;23;25"
    private static final String ENDP_TOP = "mode=list&token=%s&format=json_extended&sort=seeders&category=14;48;17;44;45;47;42;46;18;41;23;25"

    // RateLimiting
    // The rate is 1 request every 2 seconds

    private static long LAST_TIME = System.currentTimeMillis();
    private static String TOKEN


    // POGOS

    private class POGO_Response {
        List<POGO_Torrent> torrent_results

        String error
        int error_code
    }

    private class POGO_Token {
        String token
    }

    private class POGO_Torrent {
        String title
        String category
        String download
        int seeders
        int leechers
        String info_page

        String toString() {
            // seeders, leechers, title, info, magnet wrapped in url
            return String.format(
                    "[S:%d|L:%d] %s",
                    seeders,
                    leechers,
                    title
            )
        }
    /* String toString() {
            return title + "|" + category + "|" + download + "|" + seeders + "|" + leechers + "|" + info_page
        }*/
    }



    // Helper functions

    private def updateToken(){
        POGO_Token oTok = gObj.fromJson(IOUtils.toString(new URL(BASE_STRING + ENDP_TOKEN), "UTF-8"), POGO_Token.class)
        TOKEN = oTok.getToken()
    }

    private POGO_Response makeRateLimitedCall(String sURL){

        System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")

        if(TOKEN == null) // first time
            updateToken()

        String sFinal = BASE_STRING + String.format(sURL, TOKEN)

        POGO_Response oRes = gObj.fromJson(IOUtils.toString(new URL(sFinal), "UTF-8"), POGO_Response.class)

        println(oRes)

        if(oRes.error != null){ // uh oh

            switch (oRes.error_code){
                case 4: // invalid token, update it and go again
                    println("updating token")
                    updateToken()
                    Thread.sleep(2000)
                    return makeRateLimitedCall(sURL)
                    break;
                case 5: // requesting too fast, try again in two seconds
                    println("going 2fast")
                    Thread.sleep(2000)
                    return makeRateLimitedCall(sURL)
                    break;
            }

        }

        return oRes;
    }

    private POGO_Response getTopTorrents(){

        POGO_Response oRes = makeRateLimitedCall(ENDP_TOP)

        return oRes

    }

    private POGO_Response getTopSearchedTorrents(String term){

        POGO_Response oRes = makeRateLimitedCall(String.format(ENDP_SEARCH, URLEncoder.encode(term, "UTF-8"), "%s"))

        return oRes

    }

    private EmbedObject getEmbedForTorrents(List<POGO_Torrent> lstTorrents){

        lstTorrents = lstTorrents.reverse()

        while(lstTorrents.size() > 7){
            lstTorrents.remove(0)
        }


        EmbedBuilder torOut = new EmbedBuilder()
        torOut.withTitle("Searched torrent results")


        for (POGO_Torrent oTor : lstTorrents){

            torOut.appendField(
                    oTor.toString(),
                    ":inbox_tray: [MAGNET](" + oTor.download + ")",
                    false
            )

        }

        return torOut.build()

    }

    // Command registrations

    @CommandTag(
            prettyName="Torrent Search",
            channelScope="torrents",
            commandPattern="torrents|ts [search term]",
            description="Searches for linux distros"
    )
    def TorSearch(IMessage chatSource, List<String> vargs){

        List<POGO_Torrent> lstTorrents = getTopSearchedTorrents(vargs.get(0)).torrent_results

        if(lstTorrents == null || lstTorrents.size() == 0.intValue()){
            CommandHelper.sM(chatSource, "No torrents found, would you like to set a watch for this? (yes/y/etc to add to your watchlist)")

            ClientSingleton.cli.dispatcher.registerListener(new IListener<MessageReceivedEvent>() {
                @Override
                void handle(MessageReceivedEvent event) {

                    if(event.message.author != chatSource.author || event.message.channel.name != "torrents")
                        return

                    List<String> triggerList = ["yes", "y", "ye", "yeah", "yep", "aye"] as String[]

                    for (String trigger : triggerList){
                        if(event.message.content.contains(trigger)){

                            File torrentFolder = PluginUtil.getDataFolder("torrents")
                            File watchlistFile = new File(torrentFolder, "watchlist.json")
                            watchlistFile.createNewFile()
                            FileReader fr = new FileReader(watchlistFile)

                            Map<String, List<String>> uRes = gObj.fromJson(fr, new TypeToken<Map<String, List<String>>>(){}.getType())

                            if(uRes == null)
                                uRes = new HashMap<>()

                            fr.close()

                            String authorID = event.message.author.mention()

                            if(uRes.containsKey(authorID)){
                                uRes.get(authorID).add(vargs.get(0))
                            } else {
                                List<String> temp = new ArrayList<>()
                                temp.add(vargs.get(0))
                                uRes.put(authorID, temp)
                            }

                            Writer writer = new FileWriter(watchlistFile)
                            gObj.toJson(uRes, writer)
                            writer.close()

                            CommandHelper.sM(chatSource, "Your watchlist has been updated, use !tw to view your watch list")

                            ClientSingleton.cli.dispatcher.unregisterListener(this)
                            return
                        }
                    }

                }
            })

            return
        }

        CommandHelper.sM(chatSource, getEmbedForTorrents(lstTorrents))

    }

    Runnable oWatch

    media_commands(){

        ScheduledExecutorService exServiceTimed = Executors.newScheduledThreadPool(1)

        ExecutorService exService = Executors.newCachedThreadPool()

        oWatch = new Runnable() {
            @Override
            void run() {

                File torrentFolder = PluginUtil.getDataFolder("torrents")
                File watchlistFile = new File(torrentFolder, "watchlist.json")
                FileReader fr = new FileReader(watchlistFile)
                Map<String, List<String>> uRes = gObj.fromJson(fr, new TypeToken<Map<String, List<String>>>(){}.getType())
                fr.close()

                uRes.keySet().each {
                    for(String s : uRes[it]){
                        Thread.sleep(10000)
                        List<POGO_Torrent> lstTorrents = getTopSearchedTorrents(s).torrent_results

                        if(lstTorrents == null || lstTorrents.size() < 2)
                            return

                        def torChan = ClientSingleton.cli.getChannels(false).find {chan -> chan.name == "torrents"}

                        CommandHelper.sM(
                                torChan,
                                "$it, some torrents have been found for your watch item $s",
                                getEmbedForTorrents(lstTorrents)
                        )

                        CommandHelper.sM(torChan, "Is this what you wanted, $it? (replying no/n/nope/nah/nay will keep the term on your watchlist, will time out after 30s)")


                        boolean done = false
                        IListener oLis

                        Runnable oRun = new Runnable() {
                            @Override
                            void run() {

                                Thread.sleep(30000)

                                ClientSingleton.cli.dispatcher.unregisterListener(oLis)

                                done = true

                                CommandHelper.sM(torChan, "Assuming this was correct, removing watch term")

                                uRes.get(it).remove(s)

                                Writer writer = new FileWriter(watchlistFile)
                                gObj.toJson(uRes, writer)
                                writer.close()

                            }
                        }


                        Future<?> oFut = exService.submit(oRun)

                        oLis = new IListener<MessageReceivedEvent>() {
                            @Override
                            void handle(MessageReceivedEvent event) {

                                if (event.message.author.mention() != it || event.message.channel.name != "torrents")
                                    return

                                List<String> triggerList = ["no", "n", "nope", "nah", "nay"] as String[]

                                for (String trigger : triggerList) {
                                    if (event.message.content.contains(trigger)) {
                                        oFut.cancel(true)

                                        CommandHelper.sM(torChan, "The term will be kept on your watch list.")

                                        done = true

                                    }
                                }

                            }
                        }

                        ClientSingleton.cli.dispatcher.registerListener(oLis)

                        while (!done){
                            Thread.sleep(10000)
                        }

                    }
                }


            }
        }

        exServiceTimed.scheduleAtFixedRate(oWatch, 0, 12, TimeUnit.HOURS)

    }

    @CommandTag(
            prettyName="Torrent Watchlist",
            channelScope="torrents",
            commandPattern="torrentwatchlist|tw <number to delete or term to watch>",
            description="Lists or manages your watchlist"
    )
    def TorWatch(IMessage chatSource, List<String> vargs){

        File torrentFolder = PluginUtil.getDataFolder("torrents")
        File watchlistFile = new File(torrentFolder, "watchlist.json")
        watchlistFile.createNewFile()
        FileReader fr = new FileReader(watchlistFile)

        Map<String, List<String>> uRes = gObj.fromJson(fr, new TypeToken<Map<String, List<String>>>(){}.getType())

        fr.close()

        if(uRes != null && uRes.containsKey(chatSource.author.mention()) && vargs.size() > 0) {

            Integer toDel

            try {
                toDel = Integer.parseInt(vargs.get(0))
            } catch (NumberFormatException e){

                uRes.get(chatSource.author.mention()).add(vargs.get(0))

                Writer writer = new FileWriter(watchlistFile)
                gObj.toJson(uRes, writer)
                writer.close()

                CommandHelper.sM(chatSource, "Your term has been added to your watchlist.")

                return
            }

            if((toDel.intValue() < 0 || toDel.intValue() > uRes.get(chatSource.author.mention()).size()))
                return

            uRes.get(chatSource.author.mention()).remove(toDel)

            Writer writer = new FileWriter(watchlistFile)
            gObj.toJson(uRes, writer)
            writer.close()

            CommandHelper.sM(chatSource, "That entry has been removed.")

            return

        }

        if(uRes != null && uRes.containsKey(chatSource.author.mention())){

            if(uRes.get(chatSource.author.mention()).size() == 0){
                CommandHelper.sM(chatSource, "Your watch list is empty!")
                return
            }

            String temp = "Your watch list is as follows (!tw number will remove the numbered item from being watched): \n"
            int i = 0

            for(String s : uRes.get(chatSource.author.mention())){
                temp += "$i: $s\n"
                i++
            }

            CommandHelper.sM(chatSource, temp)
        }

    }

    @CommandTag(
            prettyName="Torrent Check",
            channelScope="torrents",
            commandPattern="torcheck|tc",
            description="Checks your watchlist"
    )
    def TorCheck(IMessage chatSource, List<String> vargs) {

        CommandHelper.sM(chatSource, "Running a manual check on your watchlist, if there's no results, its found nothing.")

        oWatch.run()

    }

    @CommandTag(
            prettyName="Torrents Top List",
            channelScope="torrents",
            commandPattern="tortop|tt",
            description="Lists top 10 linux distros"
    )
    def TorList(IMessage chatSource, List<String> vargs) {

        List<POGO_Torrent> lstTorrents = getTopTorrents().torrent_results

        if(lstTorrents == null || lstTorrents.size() == 0.intValue()){
            CommandHelper.sM(chatSource, "No torrents found")
            return
        }

        CommandHelper.sM(chatSource, getEmbedForTorrents(lstTorrents))

    }

    @CommandTag(
            prettyName="Football Streams",
            channelScope="futbol",
            commandPattern="futbolstreams|fs <stream code>",
            description="Gets a nice list of upcoming football streams from r/soccerstreams"
    )
    def FootballStreams(IMessage chatSource, List<String> vargs) {
        SyndFeed feed = null;
        try {
            feed = CommandHelper.rssFromURL("https://www.reddit.com/r/soccerstreams/.rss");
        } catch (IOException | URISyntaxException | FeedException e) {
            throw new CommandException("Couldn't parse the rss feed");
        }

        Map<String, SyndEntry> searchMapper = new HashMap<>();

        for (SyndEntry e : feed.getEntries()) {

            searchMapper.put(CommandHelper.hashMe(e.getTitle()), e);

        }

        if (vargs.size() == 0) {
            String output = "";

            CommandHelper.sM(chatSource, "Listing all streams...");

            for (Map.Entry<String, SyndEntry> e : searchMapper.entrySet()) {
                String ke = e.getKey();
                SyndEntry obj = e.getValue();

                if (obj.getTitle().contains("Match"))
                    output = "\n" + "[" + ke + "] " + obj.getTitle() + "\n" + CommandHelper.getTinyURL(obj.getLink()) + "\n" + output;

            }

            CommandHelper.sM(chatSource, output);

        } else if (vargs.size() == 1) {
            String s = String.valueOf(vargs[0]).toUpperCase();

            if (searchMapper.containsKey(s)) {
                SyndEntry ent = searchMapper.get(s);

                String rssURI = ent.getLinks().get(0).getHref() + ".rss";

                SyndFeed subfeed = null;
                try {
                    subfeed = CommandHelper.rssFromURL(rssURI);
                } catch (IOException | URISyntaxException | FeedException e) {
                    throw new CommandException("Couldn't parse the rss feed");
                }


                List<String> linkList = new ArrayList<>();
                String urlregex = "<a.+>(.+)<\\/a>";

                for (SyndEntry entry : subfeed.getEntries()) {
                    String internal = entry.getContents().get(0).getValue();

                    Pattern tokenMatcher = Pattern.compile(urlregex);

                    Matcher m = tokenMatcher.matcher(internal);


                    while (m.find()) {
                        String link = m.group(1);

                        if (!linkList.contains(link) && link.contains("http://")) {
                            linkList.add(link);
                        }
                    }


                }

                String output = "";

                for (String lin : linkList) {
                    output = "\n" + lin + "\n" + output;
                }

                CommandHelper.sM(chatSource, output);

            }
        }
    }

}