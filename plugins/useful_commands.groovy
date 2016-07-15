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
import command.CommandHelper
import plugin.CommandTag
import plugin.PluginInfo
import sx.blah.discord.handle.obj.IMessage

@PluginInfo(
        name="Useful commands",
        description="Contains useful commands",
        author="Declan Neilson",
        version="1.0"
)
class useful_commands {

    @CommandTag(
            prettyName="Admin Commands",
            channelScope="all",
            commandPattern="admin|ac [command] <command args>...",
            description="Executes admin commands"
    )
    def AdminCom(IMessage chatSource, List<String> vargs){

        for(int i = 0; i < 6; i++){
            CommandHelper.sM(chatSource, "test")
        }
    }

    @CommandTag(
            prettyName="Show Pinned",
            channelScope="all",
            commandPattern="pinned|p",
            description="Lists pinned messages"
    )
    def PinnedCom(IMessage chatSource, List<String> vargs){

        String outMessage = "";

        if(chatSource.channel.pinnedMessages.empty) {
            CommandHelper.sM(chatSource, "There's no pinned messages to show")
            return
        }

        for(IMessage msg : chatSource.channel.pinnedMessages){
            outMessage += msg.author.getName() + ": " + msg.content + "\n"
        }

        CommandHelper.sM(chatSource, outMessage)

    }

    /*
    @CommandTag(
            prettyName="Train Times",
            channelScope="all",
            commandPattern="traintimes|tt [from station] <to station>",
            description="Gets upcoming train times at specified station"
    )
    def TrainTimes(IMessage chatSource, List<String> vargs){
        String fromS, destS;
        StationJSON stat = null;
        Gson gson = new Gson();


        if (CommandHelper.isValidStation(vargs[0])) {
            fromS = vargs[0];
        } else {
            fromS = CommandHelper.getStationCode(vargs[0]);
        }

        if (fromS == null || fromS.equals(""))
            throw new CommandException(fromS + " is not a valid station.");

        if (vargs.size() == 1) {


            CommandHelper.sM(chatSource, "Assuming station code: " + fromS + " from search " + vargs[0]);

            try {

                stat = gson.fromJson(IOUtils.toString(new URL("https://huxley.apphb.com/all/" + fromS + "?accessToken=05ef98c7-3308-433d-977f-6eafcf0b0ef0"), "UTF-8"), StationJSON.class);

            } catch (IOException e) {
                throw new CommandException("Couldn't read the train times");
            }

        } else if (vargs.size() == 2) {
            if (CommandHelper.isValidStation(vargs[1])) {
                destS = vargs[1];
            } else {
                destS = CommandHelper.getStationCode(vargs[1]);
            }


            if (destS == null || destS.equals(""))
                throw new CommandException(destS + " is not a valid station sproglet.");

            CommandHelper.sM(chatSource, "Assuming station code from: " + fromS + " and station code to: " + destS + " from search " + vargs[0] + "->" + vargs[1]);

            try {

                stat = gson.fromJson(IOUtils.toString(new URL("http://huxley.apphb.com/departures/" + fromS + "/to/" + destS + "?accessToken=05ef98c7-3308-433d-977f-6eafcf0b0ef0"), "UTF-8"), StationJSON.class);

            } catch (IOException e) {
                throw new CommandException("Couldn't read the train times");
            }

        }


        String entries = "";

        if (stat.getTrainServices() != null) {

            for (TrainService ts : stat.getTrainServices()) {

                String arrives = (ts.getSta() != null ? "Arr " + (ts.getEta().equals("On time") ? "on time " + ts.getSta() : "delayed " + ts.getEta()) + " " : "");

                String departs = (ts.getStd() != null ? "Dep " + (ts.getEtd().equals("On time") ? "on time " + ts.getStd() : "delayed " + ts.getEtd()) + " " : "");

                String fromStat = (ts.getOrigin().get(0) != null ? ts.getOrigin().get(0).getLocationName() : "");

                String toStat = (ts.getDestination().get(0) != null ? ts.getDestination().get(0).getLocationName() : "");

                String platform = (ts.getPlatform() != null ? "Platform " + ts.getPlatform() : "");


                String listStr = "";


                TrainRoute route = null;
                try {
                    route = gson.fromJson(IOUtils.toString(new URL("https://huxley.apphb.com/service/" + ts.getServiceID() + "?accessToken=05ef98c7-3308-433d-977f-6eafcf0b0ef0"), "UTF-8"), TrainRoute.class);
                } catch (IOException e) {
                }

                try {

                    for (CallingPoint_ cp : route.getSubsequentCallingPoints().get(0).getCallingPoint()) {
                        listStr = listStr + cp.getLocationName() + "->";
                    }

                } catch (NullPointerException ignored) {
                }

                String calling = "<tr colspan=\"5\">\n" +
                        "<td colspan=\"5\">" + listStr + "</td\n" +
                        "</tr>\n";
                ;

                entries = entries +
                        "  <tr>\n" +
                        "    <td>" + arrives + "</td>\n" +
                        "    <td>" + departs + "</td>\n" +
                        "    <td>" + fromStat + "</td>\n" +
                        "    <td>" + toStat + "</td>\n" +
                        "    <td>" + platform + "</td>\n" +
                        "  </tr>\n" +
                        calling;

            }
        }


        String style = "<style>\n" +
                "#customers {\n" +
                "    font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\n" +
                "    border-collapse: collapse;\n" +
                "    width: 100%;\n" +
                "}\n" +
                "\n" +
                "#customers td, #customers th {\n" +
                "font-family: Tahoma;\n" +
                "font-size:20px;\n" +
                "    border: 1px solid #ddd;\n" +
                "    text-align: left;\n" +
                "    padding: 8px;\n" +
                "}\n" +
                "\n" +
                "#customers tr:nth-child(even){background-color: #f2f2f2}\n" +
                "\n" +
                "#customers tr:hover {background-color: #ddd;}\n" +
                "\n" +
                "#customers th {\n" +
                "    padding-top: 12px;\n" +
                "    padding-bottom: 12px;\n" +
                "    background-color: #4CAF50;\n" +
                "    color: white;\n" +
                "}\n" +
                "</style>";


        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        imageGenerator.loadHtml("<head>\n" +
                style +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<table id=\"customers\">\n" +
                "  <tr>\n" +
                "    <th>Arrives</th>\n" +
                "    <th>Departs</th>\n" +
                "    <th>From</th>\n" +
                "    <th>To</th>\n" +
                "    <th>Platform</th>\n" +
                "  </tr>\n" +
                entries +
                "</table>\n" +
                "\n" +
                "</body>");

        File emoteList = PluginUtil.getDataFolder("pics")

        File tempP = new File(emoteList.getPath() + "/temp.png");

        if(tempP.exists())
            tempP.delete()

        imageGenerator.saveAsImage(tempP);

        try {
            chatSource.getChannel().sendFile(new File(emoteList.getPath() + "/temp.png"));
        } catch (IOException | MissingPermissionsException | DiscordException | HTTP429Exception e) {
            throw new CommandException("Couldn't find the temp picture");
        }
    }
    */

}