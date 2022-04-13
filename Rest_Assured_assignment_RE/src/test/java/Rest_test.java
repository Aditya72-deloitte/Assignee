import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AnyOf.anyOf;

public class Rest_test {
    @BeforeSuite
    public void nothing() {
        RestAssured.useRelaxedHTTPSValidation();
    }
    @Test
    public void get_call(){
        given().
                baseUri("https://gorest.co.in/public/v1").
                header("Content-Type","application/json").
        when().
                get("/users").
        then().
                statusCode(200);
    }
    @Test
    public void VerifyGender(){
        Response response = given().
        when().
                get("https://gorest.co.in/public/v1/users").
                then().extract().response();
        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");
        for(int i = 0 ; i < arr.length();i++){
            assertThat(arr.getJSONObject(i).get("gender"), anyOf(is("male"), is("female")));
        }
    }
    @Test
    public void VerifyEmail() {
        Response response = given().
                get("https://gorest.co.in/public/v1/users").
                then().extract().response();
        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");
        int flag = 0;
        for (int i = 0; i < arr.length(); i++) {
            String email = (String) arr.getJSONObject(i).get("email");
            if (email.endsWith(".biz")) {
                flag = flag + 1;
            }
            if (flag < 2) {
                Assert.fail(".biz extension should be more than 1");
            }
        }
    }
    @Test
    public void Unique_id_value(){
        Response response = given().
                when().
                get("https://gorest.co.in/public/v1/users").
                then().extract().response();
        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");
        int[] id = new int[arr.length()];
        for(int i = 0 ; i < arr.length(); i++) {
           id[i] = (int)arr.getJSONObject(i).get("id");
        }
        Arrays.sort(id);
        for (int i = 0 ; i < (id.length)-1 ; i++) {
            if(id[i] == id[i+1]){
                Assert.fail();
            }
        }
    }
    @Test
    public void ValidateJsonSchema(){
        given().
                baseUri("https://gorest.co.in/public/v1").
                when().
                get("/users").
                then().
                body(matchesJsonSchemaInClasspath("JSON_schema.json"));
    }
}
