package main;

import command.ChatCommand;
import command.CommandException;
import command.CommandHelper;
import command.CommandList;
import plugin.PluginHarness;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;

import java.util.List;
import java.util.Map;

import static main.ClientSingleton.cli;
import static main.MainExecutor.log;

/**
 * Created by Declan on 29/04/2016.
 */
public class CoreEvents {

	@EventSubscriber
	public void HandleReadyEvent(ReadyEvent re){

		try {
			cli.changeUsername("SKELPER");
		} catch (DiscordException | HTTP429Exception ignored) {
			log.debug("Couldn't update the username");
		}

		try {
			PluginHarness.loadPlugins(cli);
		} catch (Exception e) {
			log.error("Couldn't load plugins", e);
		}

	}

	@EventSubscriber
	public void HandleDisconnectedEvent(DiscordDisconnectedEvent de){
		ClientSingleton.attemptLogin();
	}

	@EventSubscriber
	public void HandleCommands(MessageReceivedEvent msgE){
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

	}
}
