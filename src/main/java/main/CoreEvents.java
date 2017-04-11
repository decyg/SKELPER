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
package main;

import command.ChatCommand;
import command.CommandException;
import command.CommandHelper;
import command.CommandList;
import plugin.PluginHarness;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;
import java.util.Map;

import static main.MainExecutor.log;

/**
 * Created by Declan on 29/04/2016.
 */
public class CoreEvents {

	@EventSubscriber
	public void HandleReadyEvent(ReadyEvent re) {

		log.info("ReadyEvent fired!");

		try {
			PluginHarness.loadPlugins();
		} catch (Exception e) {
			log.error("Couldn't load plugins", e);
		}



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

				if (cmdO.getChannelUse().equals("all") ||
						msg.getChannel().getName().equals(cmdO.getChannelUse()) ||
						msg.getChannel().getName().equals("dev")) {

					msg.getChannel().setTypingStatus(true);

					cmdO.execCommand(msg, cmdArgsList);

				} else {
					CommandHelper.sM(msg, "Use the #" + cmdO.getChannelUse() + " channel.");
				}

			} catch (Throwable e) {

				try {
					CommandHelper.sM(msg, "Something happened: " + e.getMessage());
					e.printStackTrace();
				} catch (CommandException ignored) {
				}
			}

			msg.getChannel().setTypingStatus(false);
		}

	}

}
