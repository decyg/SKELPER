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

import org.springframework.core.env.Environment;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.util.concurrent.TimeUnit;

import static main.MainExecutor.log;

/**
 * Created by Declan on 29/04/2016.
 */
public final class ClientSingleton {

	public static IDiscordClient cli;
	private static int numRetries, timeWait;

	public static void initClient(String token)  {

		ClientBuilder clientBuilder = new ClientBuilder();
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
							Thread.sleep(TimeUnit.MINUTES.toMillis(1));
						} catch (InterruptedException ignored1) {
						}
					}

				}

				log.error("Severe error, couldn't connect to discord server, going to sleep and trying again in " + timeWait + " minutes.");

				try {
					Thread.sleep(TimeUnit.MINUTES.toMillis(timeWait));

				} catch (InterruptedException ignored) {
				}

				attemptLogin(numRetries, timeWait);
			}
		});

		loginAtt.start();

	}
}
