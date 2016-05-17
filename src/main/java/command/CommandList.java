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
package command;

import main.MainExecutor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Declan on 02/04/2016.
 */
public final class CommandList {

	private static List<ChatCommand> commandList = new ArrayList<>();

	public static void putC(String prettyName, String channelUse, String commandName, String comDes, ChatCommand.CommandFunc func) {

		String incomingCmdName = "[CMD: " + prettyName + "] ";

		for(ChatCommand c : commandList){
			List<String> storedCmd;
			List<String> incomingCmd;

			try{
				storedCmd = Arrays.asList(c.getCommandString().split(" ")[0].split("\\|"));
				incomingCmd = Arrays.asList(commandName.split(" ")[0].split("\\|"));
			} catch (NullPointerException e){
				MainExecutor.log.error(incomingCmdName + "Incoming command or stored command is malformed, ensure it is in the format (cmd|alias|alias2 [arg]) ");
				return;
			}

			for(String iCmd : incomingCmd){
				if(storedCmd.contains(iCmd)){
					MainExecutor.log.error(incomingCmdName + "Couldn't store command, a command is already using the text trigger \"" + iCmd + "\"");
					return;
				}
			}

		}

		commandList.add(new ChatCommand(channelUse, prettyName, commandName, comDes, func));
	}



	private static boolean stringMatchesCmd(ChatCommand com, String inputCmd){
		// first build the regex from the coms stuff then compare the inputcommand against it
		// regex for tokenising cmd string


		String argumentRegex = "( (?:(?:\\\"[\\w ]+\\\"|\\w+)(?: )??))"; // this matches "arg here" or arg

		String cmdToken = com.getCommandString().split(" ")[0];

		String argToken = com.getCommandString().replace(cmdToken + " ", "");

		String comparisonRegex = "(" + cmdToken + ")";

		Pattern p = Pattern.compile("(\\<.+\\>\\.{3})|(\\<.+\\>)|(\\[.+\\])"); // group 1 is <>... group 2 is <> and group 3 is []

		Matcher m = p.matcher(argToken);

		while(m.find()) { // something is fucked, space should be included in the arg token because without it

			if (m.group(1) != null) { // <>...
				comparisonRegex = comparisonRegex + argumentRegex + "*";
			} else if (m.group(2) != null) { // <>
				comparisonRegex = comparisonRegex + argumentRegex + "?";
			} else if (m.group(3) != null) { // []
				comparisonRegex = comparisonRegex + argumentRegex;
			}

		}

		comparisonRegex = comparisonRegex.trim();

		return Pattern.matches(comparisonRegex, inputCmd);
	}

	public static Map.Entry<ChatCommand, List<String>> getCmd(String cmd) throws CommandException {

		String argMatcher = "((?:(?:\\\"[\\w ]+\\\"|\\w+)(?: )??))";

		List<String> argList = new ArrayList<>();

		for(ChatCommand c : commandList){
			if(stringMatchesCmd(c, cmd)){

				String cmdToken = cmd.split(" ")[0];

				//String argToken = cmd.replace(cmdToken, "");
				String argToken = cmd.substring(cmdToken.length());

				Pattern p = Pattern.compile(argMatcher); // group 1 is <>... group 2 is <> and group 3 is []

				Matcher m = p.matcher(argToken);

				while(m.find()) { // something is fucked, space should be included in the arg token because without it

					String foundarg = m.group(1);

					if(foundarg.startsWith("\"") && foundarg.endsWith("\"")){
						foundarg = foundarg.substring(1, foundarg.length()-1);
					}

					argList.add(foundarg);

				}

				return new AbstractMap.SimpleEntry<>(c, argList);
			}
		}

		throw new CommandException("Invalid command, check !help");

	}

	static {

		putC(
				"Help Command",
				"all",
				"help|he",
				"Displays the help screen",
				(chatSource, vargs) -> {

					String response = "Listing all commands ([req] denotes required, <opt> denotes optional, <opt>... denotes multiple optional)\n";
					response += "Arguments can either be passed in as !command arg1 or !command \"multiple arg\"\n";

					for (ChatCommand e : commandList) {
						response += "```" + "!" + e.getCommandString() + " - " + e.getDescription() + "```" + "\n";
					}

					CommandHelper.sM(chatSource, response);

				}
		);

	}

}
