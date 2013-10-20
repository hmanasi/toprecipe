package com.toprecipe.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.repository.UserRepository;
import com.toprecipe.services.UserService;

import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;

@org.springframework.stereotype.Controller
public class Users extends Controller {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserService userService;

	public Result signup() {
		return ok(views.html.signup.render(Form.form(SignupInfo.class)));
	}

	public Result createUser() {
		Form<SignupInfo> signupForm = Form.form(SignupInfo.class)
				.bindFromRequest();

		String email = signupForm.field("email").value();

		if (!signupForm.hasErrors() && userRepo.findByEmail(email) != null) {
			signupForm.reject("email", "This email address is already in use.");
		}

		if (signupForm.hasErrors()) {
			return badRequest(views.html.signup.render(signupForm));
		}

		userService.createUser(signupForm.field("email").value(), signupForm
				.field("password").value());

		return redirect(com.toprecipe.controllers.routes.Application.login());
	}

	public static class SignupInfo {
		@Required
		@Email
		private String email;
		@Required
		@Email
		private String repeatEmail;
		@Required
		private String password;
		@Required
		private String repeatPassword;

		public List<ValidationError> validate() {
			List<ValidationError> errors = new ArrayList<ValidationError>();
			if (!email.equalsIgnoreCase(repeatEmail)) {
				errors.add(new ValidationError("repeatEmail",
						"Does not match email."));
			}

			if (!password.equalsIgnoreCase(repeatPassword)) {
				errors.add(new ValidationError("repeatPassword",
						"Does not match password."));
			}

			return errors.isEmpty() ? null : errors;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getRepeatEmail() {
			return repeatEmail;
		}

		public void setRepeatEmail(String repeatEmail) {
			this.repeatEmail = repeatEmail;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRepeatPassword() {
			return repeatPassword;
		}

		public void setRepeatPassword(String repeatPassword) {
			this.repeatPassword = repeatPassword;
		}

	}
}
