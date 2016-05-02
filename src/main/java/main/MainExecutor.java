package main;

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
	public Environment propertiesEnv;

	public final static Logger log = Logger.getLogger("SKELPER");

	public static void main(String[] args) {

		// main spring
		SpringApplication.run(MainExecutor.class, args);


	}

	@PostConstruct
	public void initialise(){

		ClientSingleton.initClient(propertiesEnv.getProperty("skelper.token"));

		ClientSingleton.cli.getDispatcher().registerListener(new CoreEvents());

		ClientSingleton.attemptLogin(
				Integer.valueOf(propertiesEnv.getProperty("skelper.login.retries")),
				Integer.valueOf(propertiesEnv.getProperty("skelper.login.timewait"))
		);

	}

}
