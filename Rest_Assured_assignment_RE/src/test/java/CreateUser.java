import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CreateUser extends javaUtility {

    public static Logger log = LogManager.getLogger(CreateUser.class);

    @BeforeSuite
    public void nothing() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    Response response;

    //Path and sheet name of the database(Excel File)
    String Path_Of_Excel_File = "C:\\Users\\adityakumar72\\IdeaProjects\\Rest_Assured_assignment_RE\\RequestPayload.xlsx";
    String SHEET_NAME_INSIDE_THE_EXCEL = "Sheet1";


    //Registering new user with the required details
    @Test(priority = 1)
    public void create_user() throws IOException {

        log.info("Fetching the user database from excel file");

        //Fetching number of rows we have in excl file
        int rowCount = javaUtility.getRowCount(Path_Of_Excel_File, SHEET_NAME_INSIDE_THE_EXCEL);
        System.out.println(rowCount);

        //Iterating through the rows
        for (int i = 1; i <=rowCount; i++) {
            String name = javaUtility.getCellvalue(Path_Of_Excel_File, SHEET_NAME_INSIDE_THE_EXCEL, i, 0);
            String gender = javaUtility.getCellvalue(Path_Of_Excel_File, SHEET_NAME_INSIDE_THE_EXCEL, i, 1);
            String email = javaUtility.getCellvalue(Path_Of_Excel_File, SHEET_NAME_INSIDE_THE_EXCEL, i, 2);
            String status = javaUtility.getCellvalue(Path_Of_Excel_File, SHEET_NAME_INSIDE_THE_EXCEL, i, 3);

            Map bodyParameters = new LinkedHashMap();

            bodyParameters.put("name", name);
            bodyParameters.put("gender", gender);
            bodyParameters.put("email", email);
            bodyParameters.put("status", status);

            //Google Gson is a simple Java-based library to serialize Java objects to JSON and vice versa

            //Converting hashmap to json object
            Gson gson = new Gson();
            String json = gson.toJson(bodyParameters, LinkedHashMap.class);

            log.info("name, gender, email and status added");

            response = (Response) given().baseUri("https://gorest.co.in").
                    header("Content-Type","application/json").
                    header("Authorization","Bearer "+ Token).
                    body(json).
                    when().
                    post("/public/v1/users").
                    then().extract();


            System.out.println("Account registered");
            System.out.println(response.asString());
            System.out.println(response.statusCode());
        }

    }

    @Test(priority = 2)
    public void ValidateUser(){
        Assert.assertEquals(response.statusCode(),422);
    }
}