package main;

import org.springframework.core.env.Environment;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import static main.MainExecutor.log;

/**
 * Created by Declan on 29/04/2016.
 */
public final class ClientSingleton {

	public static IDiscordClient cli;
	private static int numRetries, timeWait;

	public static void initClient(String token)  { //Returns an instance of the discord client

		ClientBuilder clientBuilder = new ClientBuilder(); //Creates the ClientBuilder instance
		clientBuilder.setDaemon(true);
		clientBuilder.withToken(token);

		try {
			cli = clientBuilder.build();
		} catch (DiscordException e) {
			log.error("Fatal error, couldn't build the client object, retrying", e);
		}

	}

	public static void attemptLogin(){
		attemptLogin(numRetries, timeWait);
	}

	public static void attemptLogin(int nr, int tw){

		numRetries = nr;
		timeWait = tw;

		log.warn("Attempting to log in to the discord server");

		Thread loginAtt = new Thread(() -> {
			try {

				cli.login();

				log.info("Connected successfully");

			} catch (DiscordException e) {

				log.warn("Could not log in to the discord server, attempting " + numRetries + " retries.");

				for(int i = 0; i < numRetries; i++){

					log.warn("Attempt number " + (i + 1) + " to log into discord server.");

					try {

						cli.login();

						log.info("Reconnected successfully");

						return;
					} catch (DiscordException ignored) {
						try {
							Thread.sleep(1000 * 60);
						} catch (InterruptedException ignored1) {
						}
					}

				}

				log.error("Severe error, couldn't connect to discord server, going to sleep and trying again in " + timeWait + " minutes.");

				try {
					Thread.sleep(timeWait * 60 * 1000);
				} catch (InterruptedException ignored) {
				}

				attemptLogin(numRetries, timeWait);
			}
		});

		loginAtt.start();

	}
}
