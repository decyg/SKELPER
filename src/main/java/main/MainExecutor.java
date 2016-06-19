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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Declan on 09/04/2016.
 */
@SpringBootApplication
@ComponentScan("spring_controllers")
public class MainExecutor {

	@Autowired
	public Environment propertiesEnv;

	public final static Logger log = LoggerFactory.getLogger("SKELPER");

	public final static CoreEvents mainEvents = new CoreEvents();

	public static void main(String[] args) {

		// main spring
		SpringApplication.run(MainExecutor.class, args);

	}

	@PostConstruct
	public void initialise(){

		ClientSingleton.initClient(getToken());

		ClientSingleton.cli.getDispatcher().registerListener(mainEvents);

		ClientSingleton.attemptLogin(
				Integer.valueOf(propertiesEnv.getProperty("skelper.login.retries")),
				Integer.valueOf(propertiesEnv.getProperty("skelper.login.timewait"))
		);

	}


	private String getToken(){

		File tokenFile = new File("config/token");

		if(!tokenFile.exists())
			System.exit(-1);

		String tok = "";

		try {
			tok = new String(Files.readAllBytes(tokenFile.toPath()));
		} catch (IOException e) {
		}

		if(tok.length() != 59)
			System.exit(-1);

		return tok;

	}

}
