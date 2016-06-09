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

import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.FeedException
import command.CommandException
import command.CommandHelper
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
class Plugin {

    @CommandTag(
            prettyName="KAT Link",
            channelScope="all",
            commandPattern="kat|k",
            description="Gets a link to kat"
    )
    def KatLink(IMessage chatSource, List<String> vargs){
        CommandHelper.sM(chatSource, "https://kickass.unblocked.red/full/");
    }

    @CommandTag(
            prettyName="KAT Search",
            channelScope="torrents",
            commandPattern="katsearch|ks [search term]",
            description="Searches kat"
    )
    def KatSearch(IMessage chatSource, List<String> vargs){
        String searchTerm = "";

        for (String s : vargs) {
            searchTerm = searchTerm + s + " ";
        }

        searchTerm += "is_safe:1";

        searchTerm = searchTerm.trim();

        SyndFeed feed = null;
        try {
            feed = CommandHelper.rssFromURL("https://kickass.unblocked.red/usearch/" + searchTerm + "/?rss=1&field=seeders&sorder=desc");
        } catch (IOException | URISyntaxException | FeedException e) {
            throw new CommandException("Couldn't parse the rss feed");
        }

        CommandHelper.sM(chatSource, "Listing search results for: " + searchTerm);

        String response = "";
        int i = 0;
        for (SyndEntry e : feed.getEntries()) {
            if (i > 5)
                break;

            String title = e.getTitle();
            String seeds = e.getForeignMarkup().get(3).getValue();
            String peers = e.getForeignMarkup().get(4).getValue();
            String magnet = e.getForeignMarkup().get(2).getValue();
            String link = e.getLink();

            link = link.replace("http://kat.cr/", "https://kickass.unblocked.red/");

            response = "\n" + "[S:" + seeds + " P: " + peers + "] " + title + "\n    Desc: " + CommandHelper.getTinyURL(link) + "    Magnet: " + CommandHelper.shortenMagnet(magnet) + "\n" + response;

            i++;
        }

        CommandHelper.sM(chatSource, response);
    }

    @CommandTag(
            prettyName="KAT Top List",
            channelScope="torrents",
            commandPattern="kattop|kt",
            description="Lists top 10 kat torrents"
    )
    def KatList(IMessage chatSource, List<String> vargs) {
        SyndFeed feed = null;
        try {
            feed = CommandHelper.rssFromURL("https://kickass.unblocked.red/movies/?rss=1&field=seeders&sorder=desc");
        } catch (IOException | URISyntaxException | FeedException e) {
            throw new CommandException("Couldn't parse the rss feed");
        }

        CommandHelper.sM(chatSource, "Listing top seeded results");

        String response = "";
        int i = 0;
        for (SyndEntry e : feed.getEntries()) {
            if (i > 10)
                break;

            String title = e.getTitle();
            String seeds = e.getForeignMarkup().get(3).getValue();
            String peers = e.getForeignMarkup().get(4).getValue();
            String magnet = e.getForeignMarkup().get(2).getValue();
            String link = e.getLink();

            link = link.replace("http://kat.cr/", "https://kickass.unblocked.red/");

            response = "\n" + "[S:" + seeds + " P: " + peers + "] " + title + "\n    Desc: " + CommandHelper.getTinyURL(link) + "    Magnet: " + CommandHelper.shortenMagnet(magnet) + "\n" + response;

            i++;
        }
        CommandHelper.sM(chatSource, response);
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