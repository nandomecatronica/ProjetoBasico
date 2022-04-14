package br.com.inmetrics.rest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class XMLTest {
    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;

    @BeforeClass
    public static void setup(){
        baseURI = "https://restapi.wcaquino.me";
//        port = 443;
//        basePath = "";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        reqSpec = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectStatusCode(200);
        resSpec = resBuilder.build();

        requestSpecification = reqSpec;
        responseSpecification = resSpec;
    }


    @Test
    public void devoTrabalharComXML(){

        given()
//                .log().all()
//                .spec(reqSpec)
            .when()
                .get("/usersXML/3")
            .then()
//                .statusCode(200)
//                .spec(resSpec)

                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id",is("3"))
                .rootPath("user.filhos")
                .body("name.size()",is(2))
                .body("name[0]",is("Zezinho"))
                .detachRootPath("filhos")
                .body("filhos.name[1]",is("Luizinho"))
                .body("filhos.name",hasItem("Zezinho"))
                .detachRootPath("user")
                .body("filhos.filhos.name",hasItems("Zezinho"))//Pq funciona se o caminho não está correto?
                .body("user.filhos.name",hasItems("Zezinho","Luizinho"))//Esse seria o correto quando retiramos o user
        ;
    }

    @Test
    public void devoPesquisasAvancadasComXML(){
        given()
//            .spec(reqSpec)
        .when()
            .get("/usersXML")
        .then()
//            .spec(resSpec)
            .statusCode(200)
            .body("users.user.size()",is(3))
            .body("users.user.findAll{it.age.toInteger() <25}.size()",is(1))
            .body("users.user.@id", hasItems("1","2","3"))
            .body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
            .rootPath("users.user")
            .body(".find{it.age == 30}.name",is("João da Silva"))
            .body(".age.collect{it.toInteger()*2}",hasItems(40,50,60))
            ;
    }

    @Test
    public void devoPesquisasAvancadasComXMLEJava(){
        String nome = given()
                .when()
                .get("/usersXML")
                .then()
                .statusCode(200)
                .extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}");

        System.out.println(nome.toString());
        Assert.assertEquals("Maria Joaquina".toString(),nome.toString());
        Assert.assertEquals("Maria Joaquina".toUpperCase(),nome.toUpperCase());

    }

    @Test
    public void devoPesquisasAvancadasComXPath(){
        given()
        .when()
        .get("/usersXML")
        .then()
        .statusCode(200)
        .body(hasXPath("count(/users/user)",is("3")))
        .body(hasXPath("/users/user[@id='1']"))
        .body(hasXPath("//user[@id='3']"))//pega o primeiro usuário da árvore na condição @id='3'
        .body(hasXPath("//name[text()='Luizinho']"))
        .body(hasXPath("//name[text()='Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"),allOf(containsString("Luizinho")))))
        .body(hasXPath("/users/user/name", is("João da Silva")))
        .body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
        .body(hasXPath("/users/user[3]/name", is("Ana Julia")))
        .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
        .body(hasXPath("count(/users/user/name[contains(.,'n')])", is("2")))
        .body(hasXPath("//user[age<24]/name", is("Ana Julia")))
        .body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
        .body(hasXPath("//user[age > 20 ][age < 30]/name", is("Maria Joaquina")))



        ;

    }
}
