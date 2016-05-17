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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	//public final static Logger log = Logger.getLogger("SKELPER");
	public final static Logger log = LoggerFactory.getLogger("SKELPER");

	public static void main(String[] args) {

		// main spring
		SpringApplication.run(MainExecutor.class, args);

		//LOG.info("test");


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
