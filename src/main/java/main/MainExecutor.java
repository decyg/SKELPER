package main;

import command.CommandExecutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import plugin.PluginHarness;
import plugin.PluginLoadingException;
import sx.blah.discord.api.Event;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IListener;
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;

import java.util.*;

/**
 * Created by Declan on 09/04/2016.
 */
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"spring_controllers"})
@PropertySource("classpath:application.properties")
public class MainExecutor {

	@Value("${skelper.token}")
	private String botToken;

	public final static Logger log = Logger.getLogger("SKELPER");
	public static IDiscordClient cli;

	public static void main(String[] args) {

		SpringApplication.run(MainExecutor.class, args);

	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public MainExecutor(){
		initialise();
	}

	public void initialise(){
		System.out.println(botToken);

		try {
			cli = HelperFuncs.getClient(botToken);
		} catch (DiscordException e) {
			log.error("Could not get the bot client");
			return;
		}

		connectionHandle();
		
		readyHandle();

		tryLogin();

	}

	private static void tryLogin() {

		log.info("Attempting to log in to the discord server");

		Thread loginAtt = new Thread(() -> {
			try {
				cli.login();
			} catch (DiscordException e) {
				log.error("Could not log in to the discord server", e);
			}
		});

		loginAtt.start();


	}

	private static void readyHandle() {

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

	private static void connectionHandle() {

		cli.getDispatcher().registerListener((IListener<DiscordDisconnectedEvent>) discordDisconnectedEvent -> {

			tryLogin();
			log.info("Reconnected successfully");

		});

	}
}
