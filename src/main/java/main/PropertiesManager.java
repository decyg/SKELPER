package main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Declan on 29/04/2016.
 */
@Component
@PropertySource("classpath:application.properties")
public class PropertiesManager {

	@Value("${skelper.token}")
	public String token;
}
