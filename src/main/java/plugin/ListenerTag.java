package plugin;

import sx.blah.discord.api.Event;
import sx.blah.discord.api.IListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Declan on 15/04/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ListenerTag {
	Class<? extends Event> listenEvent();
}
