package com.devtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.simple.parser.ParseException;
import com.devtest.business.*;

//Controller class to render html according to user actions and url
@Controller
public class ControllerClass {
	
	//static variable acting like global variable to use in many methods
	public static String employee;
	
	//Bussiness class like a service (business logic) using Autowired to inject like EJB
	@Autowired
	private BusinessClass businessClass;
	
	//HTTP Get to index HTML page
	@GetMapping("/index")
	public String greeting(Model model) {
		return "index";
	}
	
	//HTTP Get obtaining response from API 1 (all records) and sending data to result HTML page
	@GetMapping("/resultall")
	public String resultAll(Model model) throws ParseException {
		//String responses instantiating methods from BusinessClass / Consume API and get anual salary
		String response = businessClass.consumeApi1();
		String response2 = businessClass.getAnualAll();
		//Print in console acting like a basic Log
		System.out.println(response);
		System.out.println(response2);
		//Add responses to default model and ready to use in client side
		model.addAttribute("response", response);
		model.addAttribute("response2", response2);
		return "result";
	}
	//HTTP Get obtaining response from API 2 (requested ID) and sending data to result HTML page
	@GetMapping("/result")
	public String result(Model model) throws ParseException {
		//Analog method to resultAll() only one difference in argument in consumeApi2 business class method
		String response = businessClass.consumeApi2(employee);
		String response2 = businessClass.getAnual();
		System.out.println(response);
		System.out.println(response2);
		model.addAttribute("response", response);
		model.addAttribute("response2", response2);
		return "result2";
	}
	//HTTP Post getting value from html form and redirecting to the correct  result depending of value entered by user
	@PostMapping(value="/consult")
	public String consult(@RequestParam(name="employeeid", required=false, defaultValue="25") int employeeId) {
		employee= String.valueOf(employeeId);
		//Values in correct range return single result
		if (employeeId>0 && employeeId<=24) {
			return "redirect:/result";
		}
		//No value or values out of range returns all records
		else {
			System.out.println(employeeId);
			return "redirect:/resultall";
		}
		
	}
}
