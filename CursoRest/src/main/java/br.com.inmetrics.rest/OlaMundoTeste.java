package br.com.inmetrics.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OlaMundoTeste {
    @Test
    public void testeOlaMundo(){
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola?222");
        assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        assertTrue("O StatusCode deveria ser 200",response.statusCode()==200);
        assertEquals(200,response.statusCode());

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test
    public void devoConhecerOutrasFormasRestAssured(){
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
        assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("https://restapi.wcaquino.me/ola").then().statusCode(200);

        given()
                .when()
                .get("https://restapi.wcaquino.me/ola")
                .then()
                .statusCode(200);
    }

    @Test
    public void devoConhecerMathersHamscret(){
        assertThat("Maria", is("Maria"));
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class));
        assertThat(128d, isA(Double.class));
        assertThat(128d, greaterThan(110d));
        assertThat(128d, lessThan(140d));

        List<Integer>impares = Arrays.asList(1,5,9,3,71);

        for (int i = 0; i<impares.size(); i++){
            Integer valor = (Integer)(impares.get(i));
            if (valor % 2 ==0){
                fail();//número par na lista
            }
        };
        assertThat(impares, hasSize(5));
        assertThat(impares, contains(1,5,9,3,71));
        assertThat(impares, hasItems(1,71));

    }

    @Test
    public void devoValidarBody(){
        given()
                .when()
                .get("https://restapi.wcaquino.me/ola")
                .then()
                .statusCode(200)
                .body(is("Ola Mundo!"))
                .body(containsString("n"))
                .body(is(notNullValue()));
    }
}
