package com.sri.inventory.mgmt;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class InventoryMgmtFunctionalTest {
	
	@Test
	public void testInventoryMgmtReports() {
		
		createItem("Book01", 10.50, 13.79);
		createItem("Food01", 1.47, 3.98);
		createItem("Med01", 30.63, 34.29);
		createItem("Tab01", 57.00, 84.98);
		
		updateBuy("Tab01", 100);
		updateSell("Tab01", 2);
		updateBuy("Food01", 500);
		updateBuy("Book01", 100);
		updateBuy("Med01", 100);
		updateSell("Food01", 1);
		updateSell("Food01", 1);
		updateSell("Tab01", 2);

		
		Response response = given()
				.when()
				.get("http://localhost:8080/inventory-mgr/report");
		
		printResponse(response);
		
		
	}
	
	private Response updateBuy(String name, int quantity) {
		return given()
				.parameters("quantity", quantity)
				.when()
				.put(getURL() + "item/" + name +"/buy");
	}
	
	private Response updateSell(String name, int quantity) {
		return given()
				.parameters("quantity", quantity)
				.when()
				.put(getURL() + "item/" + name +"/sell");
	}

	private Response createItem(String name, double costPrice, double sellPrice) {
		return given()
		.parameters("costPrice", costPrice, "sellPrice", sellPrice)
		.when()
		.post(getURL() +"item/" + name );
		
	}

	public void printResponse(Response response){
		String responseJson = response.asString();
		JsonPath jsonPath = new JsonPath(responseJson);
		System.out.println(jsonPath.prettyPrint());
	}

	private String getURL() {
		return "http://localhost:8080/inventory-mgr/";
	}
}
