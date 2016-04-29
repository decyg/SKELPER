package command;

import sx.blah.discord.handle.obj.IMessage;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Declan on 02/04/2016.
 */
public class ChatCommand {

	private String channelUse, prettyName, commandString, description;
	private CommandFunc storedFunc;

	public ChatCommand(String channelUse, String pN, String cmdStr, String desc, CommandFunc storedF){
		this.channelUse = channelUse;
		this.prettyName = pN;
		this.commandString = cmdStr;
		this.description = desc;
		this.storedFunc = storedF;
	}

	public void execCommand(IMessage s, List<String> vargs) throws Throwable {
		this.storedFunc.doCommand(s, vargs);
	}

	public String getCommandString(){ return commandString; }

	public String getDescription() {
		return description;
	}

	public String getChannelUse() { return channelUse; }

	public interface CommandFunc{
		void doCommand(IMessage chatSource, List<String> vargs) throws Throwable;
	}
}
