import main.HelperFuncs
import org.apache.commons.io.FileUtils

import plugin.PluginException
import plugin.PluginInfo
import sx.blah.discord.api.EventDispatcher
import sx.blah.discord.api.EventSubscriber
import sx.blah.discord.api.IListener
import sx.blah.discord.handle.impl.events.MessageReceivedEvent
import sx.blah.discord.handle.obj.IMessage

@PluginInfo(
        name="File saver",
        description="Saves all received files in the chat into their respective channel folders",
        author="Declan Neilson",
        version="1.0"
)
class Plugin {

    @EventSubscriber
    def OnMessageEvent(MessageReceivedEvent e){
        for (IMessage.Attachment a : e.message.attachments){
            File picFolder = HelperFuncs.getDataFolder("pics")

            URLConnection conn = new URL(a.getUrl()).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
            conn.connect();

            File copyLoc = new File(picFolder.path + "/" + a.getFilename())

            FileUtils.copyInputStreamToFile(conn.getInputStream(), copyLoc);
        }
    }

}