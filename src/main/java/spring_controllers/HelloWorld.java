package spring_controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Declan on 15/04/2016.
 */
@Controller
public class HelloWorld {

	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView shortenUrl() {


		ModelAndView modelAndView = new ModelAndView("shortener");
		modelAndView.addObject("shortenedUrl", "yeet");

		return modelAndView;
	}

}
