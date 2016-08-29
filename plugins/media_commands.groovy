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
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.FeedException
import command.CommandException
import command.CommandHelper
import org.apache.commons.io.IOUtils
import plugin.CommandTag
import plugin.PluginInfo
import sx.blah.discord.handle.obj.IMessage

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
            CommandHelper.sM(chatSource, "No torrents found")
            return
        }

        lstTorrents = lstTorrents.reverse()

        while(lstTorrents.size() > 10){
            lstTorrents.remove(0)
        }

        String sOut = ""

        for (POGO_Torrent oTor : lstTorrents){
            sOut += "```" + oTor.toString() + "```" +
                    "^ <" + oTor.info_page + "> ^\n" +
                    "^ <" + CommandHelper.shortenMagnet(oTor.download) + "> ^\n\n"

        }

        CommandHelper.sM(chatSource, sOut)

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

        lstTorrents = lstTorrents.reverse()

        while(lstTorrents.size() > 10){
            lstTorrents.remove(0)
        }

        String sOut = ""

        for (POGO_Torrent oTor : lstTorrents){
            sOut += "```" + oTor.toString() + "```" +
                    "^ <" + oTor.info_page + "> ^\n" +
                    "^ <" + CommandHelper.shortenMagnet(oTor.download) + "> ^\n\n"

        }

        CommandHelper.sM(chatSource, sOut)

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