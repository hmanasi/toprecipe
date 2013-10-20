package com.toprecipe.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.services.UserService;

import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;

@org.springframework.stereotype.Controller
public class Application extends Controller {

	@Autowired
	private UserService userService;

	public Result index() {
		return redirect(com.toprecipe.controllers.routes.Recipes.recipes());
	}

	public Result login() {
		return ok(views.html.login.render(Form.form(Login.class)));
	}

	public Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

		if (loginForm.hasErrors()) {
			return badRequest(views.html.login.render(loginForm));
		}

		boolean loggedIn = userService.login(loginForm.field("email").value(),
				loginForm.field("password").value());

		if (loggedIn) {
			return redirect(com.toprecipe.controllers.routes.Application
					.index());
		}

		loginForm.reject("Invalid username or password.");

		return badRequest(views.html.login.render(loginForm));
	}

	public static class Login {

		@Required
		@Email
		public String email;
		@Required
		public String password;

	}
}
