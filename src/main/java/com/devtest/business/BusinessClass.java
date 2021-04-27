package com.devtest.business;

import java.util.Arrays;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.client.RestTemplate;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//Bussiness class like a service (business logic) using Autowired to inject like EJB
@Service
public class BusinessClass {
	
	//static like global variable
	public static String employee2;
	
	//EJB related to RestTemplates
	@Autowired
	private RestTemplate restTemplate;
	
	//Method to consume API 1 (all data) 
	public String consumeApi1() {
		//Many times returns Error: Too Many Attempts
		try {
			//Headers necessary to consume web service through HTTP Get (Error 403 Forbidden if not)
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			//HTTP request using RetTemplate
			ResponseEntity<String> response = restTemplate.exchange("http://dummy.restapiexample.com/api/v1/employees", HttpMethod.GET, entity, String.class);
			//response is given with information not used so body is obtained
			String responseStr = response.getBody();
			int begin = responseStr.indexOf("{");
			int end = responseStr.lastIndexOf("}") + 1;
			responseStr = responseStr.substring(begin, end);
			return responseStr;
		}
		catch(Exception e){
			return "API Error";
		}
	}
	//Analog method but consuming API 2 (single data) completing URL from id given by the user
	public String consumeApi2(String employee) {
		
		try {
			employee2=employee;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			HttpEntity<String> entity2 = new HttpEntity<String>("parameters", headers);
			String Url="http://dummy.restapiexample.com/api/v1/employee/"+employee;
			ResponseEntity<String> response = restTemplate.exchange(Url, HttpMethod.GET, entity2, String.class);
			String responseStr = response.getBody();
			int begin = responseStr.indexOf("{");
			int end = responseStr.lastIndexOf("}") + 1;
			responseStr = responseStr.substring(begin, end);
			return responseStr;
			}
		catch(Exception e){
			return "API Error";
		}
	}
	//Method to get "anual salaries from all data and insert into an integer array and return like string
	public String getAnualAll() throws ParseException {
		try {
			String response = consumeApi1();
			//Actions to obtain only data JSON array
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			JSONArray value = (JSONArray) json.get("data");
			//Iterator based in array elements
			Iterator i = value.iterator();
			//Array to insert every computed salary
			int[] salary_array = new int[24];
			//Loop for extract every salary insert every computed (x12) anual salary in array
			while (i.hasNext()) {
				JSONObject values = (JSONObject) i.next();
				long salary = (Long)values.get("employee_salary");
				int anual_salary = (int) salary*12;
				long id = (Long)values.get("id");
				int id_i = (int)id;
				salary_array[id_i-1]=anual_salary;
			}
			return Arrays.toString(salary_array);
			}
		catch(Exception e) {
			return "API Error";
		}
	}
	//Analog method but getting the array with an only computed salary 
	public String getAnual() throws ParseException {
		//Differences: Omitted loop, JsonObject and not JsonArray after parse, global variable to consume Api2
		try {
		String response = consumeApi2(employee2);
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response);
		JSONObject value = (JSONObject) json.get("data");
		long salary = (Long)value.get("employee_salary");
		int anual_salary = (int) salary*12;
		int[] salary_array = new int[1];
		salary_array[0]=anual_salary;
		return Arrays.toString(salary_array);
		}
		catch(Exception e) {
			return "API Error";
		}
	}
	
	
	
}
