package br.com.inmetrics.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.regex.Matcher;

import static io.restassured.RestAssured.*;

public class EnvioDadosTest {
    @Test
    public void deveEnviarValorViaQuery(){
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/v2/users?format=json")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void deveEnviarValorViaParam(){
        given()
            .log().all()
            .queryParam("format","xml")
//            .queryParam("format","Json")
        .when()
            .get("https://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.XML)
//            .contentType(ContentType.JSON)
            .contentType(Matchers.containsString("utf-8"))
        ;
    }

    @Test
    public void deveEnviarValorViaHeader(){
        given()
            .log().all()
            .accept(ContentType.XML)
        .when()
        .get("https://restapi.wcaquino.me/v2/users")
        .then()
        .log().all()
        .statusCode(200)
        .contentType(ContentType.XML)
        ;
    }
}
