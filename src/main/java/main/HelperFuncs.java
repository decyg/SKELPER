package main;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.io.File;

/**
 * Created by Declan on 09/04/2016.
 */
public final class HelperFuncs {

	public static IDiscordClient getClient(String token) throws DiscordException { //Returns an instance of the discord client
		ClientBuilder clientBuilder = new ClientBuilder(); //Creates the ClientBuilder instance
		//clientBuilder.withLogin(email, password); //Adds the login info to the builder
		clientBuilder.setDaemon(true);
		clientBuilder.withToken(token);

		return clientBuilder.build(); //Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself

	}

	public static File getDataFolder(String folder){
		File dataFolder = new File("plugindata/" + folder);
		dataFolder.mkdirs();

		return dataFolder;
	}

}
