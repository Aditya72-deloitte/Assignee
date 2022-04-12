import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
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
//            System.out.println(arr.getJSONObject(i).get("gender"));
//            String gender = (String) arr.getJSONObject(i).get("gender");
//            assertThat(gender,hasItems("male","female"));

            //assertThat(response.path((String) arr.getJSONObject(i).get("gender")), is(equalTo("male")));
        }
    }
    @Test
    public void VerifyEmail() {
        Response response = given().
                when().
                get("https://gorest.co.in/public/v1/users").
                then().extract().response();
        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");
        int flag = 0;
        for (int i = 0; i < arr.length(); i++) {
            String email = (String) arr.getJSONObject(i).get("email");
            System.out.println(email);
            if(email.endsWith(".biz")){
                flag = flag+1;
            }
          if(flag<2) {
              Assert.fail();
          }
    }
}
