package command;

import org.apache.commons.io.FileUtils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Declan on 09/04/2016.
 */
public class CommandExecutor {
	public static void startListener(IDiscordClient cli) {

		cli.getDispatcher().registerListener((IListener<MessageReceivedEvent>) msgE -> {

			IMessage msg = msgE.getMessage();

			String commandLine = msg.getContent();

			if(commandLine.startsWith("!")) {

				commandLine = commandLine.substring(1);

				try {

					Map.Entry<ChatCommand, List<String>> cmdTup = CommandList.getCmd(commandLine);

					ChatCommand cmdO = cmdTup.getKey();
					List<String> cmdArgsList = cmdTup.getValue();

					if (cmdO.getChannelUse().equals("all") || msg.getChannel().getName().equals(cmdO.getChannelUse())) {
						cmdO.execCommand(msg, cmdArgsList);
					} else {
						CommandHelper.sM(msg, "Use the #" + cmdO.getChannelUse() + " channel.");
					}

				} catch (Throwable e) {

					try {
						CommandHelper.sM(msg, "Something happened: " + e.getMessage());
					} catch (CommandException ignored) {
					}
				}
			}


		});
	}
}
