package com.blu.imdg.wsession.controller;

import com.blu.imdg.wsession.bean.Person;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class WebSessionController {

	@RequestMapping(value="/putperson", method = RequestMethod.GET)
	public String putSession(ModelMap model, HttpServletRequest request) {
		model.addAttribute("person", "Add Person to user session");
        String fName = request.getParameter("name");
        int age = Integer.valueOf(request.getParameter("age"));
        Person person = new Person();
        person.setFirstName(fName);
        person.setAge(age);
        // set user session data
		request.getSession().setAttribute(fName, person);
        // return to the welcome.jsp view
		return "welcome";
	}

	@RequestMapping(value="/getperson", method = RequestMethod.GET)
	public String getSession(ModelMap model, HttpServletRequest request) {
        String fName = request.getParameter("name");
        // get session data
		model.addAttribute("person", request.getSession().getAttribute(fName) != null ? "Age: " + ((Person) request.getSession().getAttribute(fName)).getAge() : " Unknown");
		// view welcome.jsp
        return "welcome";
	}

}
