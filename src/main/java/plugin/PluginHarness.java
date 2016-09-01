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
package plugin;

import command.CommandList;
import groovy.lang.GroovyClassLoader;
import main.MainExecutor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IMessage;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static main.ClientSingleton.cli;

/**
 * Created by Declan on 09/04/2016.
 */
public final class PluginHarness {

	static Logger log = MainExecutor.log;
	public static List<Object> registeredListeners = new ArrayList<>();

	public static void loadPlugins() throws PluginLoadingException {

		unloadAllPlugins();

		CommandList.dumpCommands();

		File allPlugins = new File("plugins");

		if (!allPlugins.exists())
			allPlugins.mkdirs();

		for(File cPlugin : allPlugins.listFiles()){

			String ezName = FilenameUtils.removeExtension(cPlugin.getName());

			String pluginLogTag = "[" + ezName + "] ";

			log.info(pluginLogTag + "Attempting to load plugin");

			GroovyClassLoader gcl = new GroovyClassLoader();
			try {

				Class c = gcl.parseClass(cPlugin);

				Object listenerInst = c.newInstance();

				if(!listenerInst.getClass().isAnnotationPresent(PluginInfo.class)) {
					log.error(pluginLogTag + "Could not load plugin, PluginInfo annotation not present");
					continue;
				}

				PluginInfo loadPlug = listenerInst.getClass().getAnnotation(PluginInfo.class);

				boolean plugValid = true;

				for (Method method : c.getMethods())
				{

					if (method.isAnnotationPresent(CommandTag.class))
					{

						if(method.getParameters().length != 2) {
							log.error(pluginLogTag + "Method " + method.toGenericString() + " could not be loaded, expected 2 parameters.");
							plugValid = false;
							break;
						}
						Class<?> eventClazz = method.getParameters()[0].getType();

						if(!eventClazz.equals(IMessage.class)) {
							log.error(pluginLogTag + "Method " + method.toGenericString() + " could not be loaded, parameter 1 should be of type IMessage");
							plugValid = false;
							break;
						}
						eventClazz = method.getParameters()[1].getType();

						if(!eventClazz.equals(List.class)) {
							log.error(pluginLogTag + "Method " + method.toGenericString() + " could not be loaded, parameter 2 should be of type List<String>");
							plugValid = false;
							break;
						}


						CommandTag cmd = method.getAnnotation(CommandTag.class);
						// pretty name, scope, command string, description, method
						CommandList.putC(cmd.prettyName(), cmd.channelScope(), cmd.commandPattern(), cmd.description(), (chatSource, vargs) -> {

							try {
								method.invoke(listenerInst, chatSource, vargs);
							} catch (InvocationTargetException | IllegalAccessException e) {
								throw e.getCause();
							}

						});

						log.info(pluginLogTag + "Registered chat command \"" + cmd.prettyName());

					} else if(method.isAnnotationPresent(EventSubscriber.class)){ // validate method signature as being instance of Event

						if(method.getParameters().length != 1) {
							log.error(pluginLogTag + "Method " + method.toGenericString() + " could not be loaded, expected only 1 parameter.");
							plugValid = false;
							break;
						}

						Class<?> eventClazz = method.getParameters()[0].getType();

						if(eventClazz.equals(Event.class) || !Event.class.isAssignableFrom(eventClazz)) {
							log.error(pluginLogTag + "Method " + method.toGenericString() + " could not be loaded, parameter should extend Event");
							plugValid = false;
							break;
						}
					}
				}

				if(!plugValid){
					log.error(pluginLogTag + "One or more errors occurred, loading attempted");
					continue;
				}

				cli.getDispatcher().registerListener(listenerInst);
				registeredListeners.add(listenerInst);

				log.info(pluginLogTag + "Registered listener: " + listenerInst.toString());

				log.info(pluginLogTag + "Successfully loaded" +
						" plugin: \"" + loadPlug.name() +
						"\" version: \"" + loadPlug.version() +
						"\" by: \"" + loadPlug.author() +
						"\"");


			} catch (Exception e) {
				log.error(pluginLogTag + "Unknown error occurred", e);
			}
		}



	}

	public static void unloadAllPlugins(){

		EventDispatcher ed = cli.getDispatcher();

		if(registeredListeners.size() > 0) {
			registeredListeners.forEach(ed::unregisterListener);
			registeredListeners.clear();

			log.info("Unloaded all listeners");
		}
	}
}
