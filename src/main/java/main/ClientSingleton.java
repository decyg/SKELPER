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

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import static main.MainExecutor.log;

/**
 * Created by Declan on 29/04/2016.
 */
public final class ClientSingleton {

	public static IDiscordClient cli;

	public static void initClient(String token)  {

		ClientBuilder clientBuilder = new ClientBuilder()
				.setDaemon(true)
				.withToken(token);

		try {
			cli = clientBuilder.build();
		} catch (DiscordException e) {
			log.error("Fatal error, couldn't build the client object, retrying", e);
		}

	}

	public static void attemptLogin(int nr, int tw){

		log.warn("Attempting to log in to the discord server");

		Thread loginAtt = new Thread(() -> {
			try {

				cli.login();

				log.info("Connected successfully");

			} catch (DiscordException e) {
			} catch (RateLimitException e) {
				e.printStackTrace();
			}
		});

		loginAtt.start();

	}
}
