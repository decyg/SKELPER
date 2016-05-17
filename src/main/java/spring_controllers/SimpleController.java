package spring_controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Declan on 15/04/2016.
 */
@Controller
public class SimpleController {

	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView simpleController() {


		ModelAndView modelAndView = new ModelAndView("skelper_status");
		modelAndView.addObject("shortenedUrl", "yeet");

		return modelAndView;
	}

	@RequestMapping(value="/shutdown", method = RequestMethod.GET)
	public void shutdownNow(){
		System.exit(0);
	}

}
