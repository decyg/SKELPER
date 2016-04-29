package main;

import command.CommandExecutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import plugin.PluginHarness;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IListener;
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;

import javax.annotation.PostConstruct;

/**
 * Created by Declan on 09/04/2016.
 */
@SpringBootApplication
@ComponentScan("spring_controllers")
public class MainExecutor {

	@Autowired
	private Environment propertiesEnv;

	public final static Logger log = Logger.getLogger("SKELPER");
	private static IDiscordClient cli;

	public static void main(String[] args) {

		SpringApplication.run(MainExecutor.class, args);

	}

	@PostConstruct
	public void initialise(){

		try {
			cli = HelperFuncs.getClient(propertiesEnv.getProperty("skelper.token"));
		} catch (DiscordException e) {
			log.error("Could not get the bot client");
			return;
		}

		connectionHandle();
		
		readyHandle();

		tryLogin();

	}

	private void tryLogin() {

		log.warn("Attempting to log in to the discord server");

		Thread loginAtt = new Thread(() -> {
			try {

				cli.login();

				log.info("Connected successfully");

			} catch (DiscordException e) {

				int numRetries = Integer.valueOf(propertiesEnv.getProperty("skelper.login.retries"));

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

				int timeWait = Integer.valueOf(propertiesEnv.getProperty("skelper.login.timewait"));

				log.error("Severe error, couldn't connect to discord server, going to sleep and trying again in " + timeWait + " minutes.");

				try {
					Thread.sleep(timeWait * 60 * 1000);
				} catch (InterruptedException ignored) {
				}

				tryLogin();
			}
		});

		loginAtt.start();


	}

	private void readyHandle() {

		cli.getDispatcher().registerListener((IListener<ReadyEvent>) discordReady -> {

			try {
				cli.changeUsername("SKELPER");
			} catch (DiscordException | HTTP429Exception ignored) {
				log.debug("Couldn't update the username");
			}

			CommandExecutor.startListener(cli);

			try {
				PluginHarness.loadPlugins(cli);
			} catch (Exception e) {
				log.error("Couldn't load plugins", e);
			}

		});

	}

	private void connectionHandle() {

		cli.getDispatcher().registerListener((IListener<DiscordDisconnectedEvent>) discordDisconnectedEvent -> {

			tryLogin();

		});

	}
}
