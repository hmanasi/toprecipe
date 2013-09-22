package com.toprecipe.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.services.HelloWorldService;

import play.*;
import play.mvc.*;
import views.html.*;

@org.springframework.stereotype.Controller
public class Application extends Controller {
	
	public Application()
	{
		System.out.println("controller initialization");
	}
	
	@Autowired
	HelloWorldService helloService;
	
    public Result index() {
        return ok(index.render(helloService.hellowWorld()));
    }

}
