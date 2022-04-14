package br.com.inmetrics.rest;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {
    @Test
    public void deveVerificarPrimeiroNivel(){
        //dado
        given()
        //Quando
        .when()
               .get("https://restapi.wcaquino.me/users/1")
        //Entao
        .then()
        .statusCode(200)
        .body("id",is(1))
        .body("name", containsString("da Silva"))
        .body("age",greaterThan(29))
        ;
    }

    @Test
    public void  deveVerificarPrimeiroNivelOutrasFormas(){
        Response response = RestAssured.request(Method.GET,"https://restapi.wcaquino.me/users/1");

        //path
        Assert.assertEquals(new Integer(1),response.path("id"));

        //JsonPath
        JsonPath jpath  = new JsonPath(response.asString());
        Assert.assertEquals(1,jpath.getInt("id"));
        Assert.assertEquals("João da Silva",jpath.getString("name"));
        Assert.assertEquals("30",jpath.getString("age"));
        Assert.assertEquals("1234.5677",jpath.getString("salary"));

        //From
        int id= JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1,id);

        }

    @Test
    public void deveVerificarSegundoNivel(){
        //dado
        given()
        //Quando
        .when()
        .get("https://restapi.wcaquino.me/users/2")
        //Entao
        .then()
        .statusCode(200)
        .body("name", containsString("Joaquina"))
        .body("endereco.rua",is("Rua dos bobos"))
        .body("endereco.numero",is(0))
        .body("age", is(25))
        .body("salary",is(2500))
        ;
    }

    @Test
    public void deveVerificarLista(){

        //dado
        given()
        //Quando
        .when()
        .get("https://restapi.wcaquino.me/users/3")
        //Entao
        .then()
        .statusCode(200)
        .body("name", containsString("Ana "))
        .body("filhos",hasSize(2))
        .body("filhos[0].name",is("Zezinho"))
        .body("filhos[1].name",is("Luizinho"))
        .body("filhos.name",hasItem("Zezinho"))//verifica se possui Zezinho na lista
        ;

    }

    @Test
    public void deveRetornarErroUsuarioInexistente(){

        //dado
        given()
                //Quando
                .when()
                .get("https://restapi.wcaquino.me/users/4")
                //Entao
                .then()
                .statusCode(404)
        .body("error",is("Usuário inexistente"))


        ;

    }

    @Test
    public void devevVerificarListaRaiz(){

        //dado
        given()
                //Quando
                .when()
                .get("https://restapi.wcaquino.me/users")
                //Entao
                .then()
                .statusCode(200)
                .body("$",hasSize(3))
                .body("name",hasItems("João da Silva","Ana Júlia","Maria Joaquina"))
                .body("age[2]",is(20))
                .body("filhos.name", hasItem(Arrays.asList("Zezinho","Luizinho")))
                .body("salary",contains(1234.5677f, 2500, null))

        ;

    }

    @Test
    public void deveFazerVerificaçõesAvancadas(){

        //dado
        given()
                //Quando
                .when()
                .get("https://restapi.wcaquino.me/users")
                //Entao
                .then()
                .statusCode(200)
                .body("age.findAll{it<=25}.size()", is(2))
                .body("findAll{it.age <= 25 && it.age > 20}.name",hasItem("Maria Joaquina"))
                .body("findAll{it.age <= 25}[0].name",is("Maria Joaquina"))
                .body("findAll{it.age <= 25}[1].name",is("Ana Júlia"))
                .body("findAll{it.age <= 25}[-1].name",is("Ana Júlia"))//-1 para o último registro
                .body("find{it.age <= 25}.name",is("Maria Joaquina"))
                .body("findAll{it.name.contains('n')}.name",hasItems("Maria Joaquina","Ana Júlia"))
                .body("id.max()",is(3))
                .body("salary.min()", is(1234.5677f))
                .body("salary.findAll{it != null}.sum()",allOf(greaterThan(3000d),lessThan(3734.57d)))
        ;

    }

    @Test
    public void deveDevoUnirJsonPathComJava(){


        ArrayList<String> names =
        given()
                .when()
                .get("https://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .extract().path("name.findAll{it.startsWhith('Maria')}")
        ;

        Assert.assertEquals(1,names.size());
        Assert.assertTrue(names.get(0).equalsIgnoreCase("mAria jOaquina"));
        Assert.assertEquals(names.get(0).toUpperCase(),"maria joaquina".toUpperCase());

    }
}
