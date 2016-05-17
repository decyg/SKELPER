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
