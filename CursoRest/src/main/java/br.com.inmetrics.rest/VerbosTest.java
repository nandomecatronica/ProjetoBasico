    package br.com.inmetrics.rest;
    import io.restassured.RestAssured;
    import io.restassured.http.ContentType;
    import org.junit.Assert;
    import org.junit.Test;

    import java.util.HashMap;
    import java.util.Map;

    import static io.restassured.RestAssured.*;
    import static org.hamcrest.Matchers.*;

    public class VerbosTest {
        @Test
        public void deveSalvarUsuario(){
            given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\":\"Jose\",\"age\": 50 }")
            .when()
                .post("https://restapi.wcaquino.me/users")
            .then()
                .log().all()
                .statusCode(201)//status inseriu
                .body("id", is(notNullValue()))
                .body("name",is("Jose"))
                .body("age", is(50))
            ;
        }

        @Test
        public void deveSalvarUsuarioUsandoMap(){
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("name", "Usuario via map");
            params.put("age",25);

            given()
                .log().all()
                .contentType("application/json")
                .body(params)
            .when()
                .post("https://restapi.wcaquino.me/users")
            .then()
                .log().all()
                .statusCode(201)//status inseriu
                .body("id", is(notNullValue()))
                .body("name",is("Usuario via map"))
                .body("age", is(25))
                .body("salary", is("2.500"))
            ;
        }

        @Test
        public void deveSalvarUsuarioUsandoObjeto(){
            User user = new User("Usuario via objeto",35,2.500);

            given()
                .log().all()
                .contentType("application/json")
                .body(user)
            .when()
                .post("https://restapi.wcaquino.me/users")
            .then()
                .log().all()
                .statusCode(201)//status inseriu
                .body("id", is(notNullValue()))
                .body("name",is("Usuario via objeto"))
                .body("age", is(35))
                .body("salary",is("2.500"))
            ;
        }

        @Test
        public void deveDeserializarObjetoAoSalvarUsuario(){
            User user = new User("Usuario deserializado",35,2.500);

            User usuarioInserido = given()
                    .log().all()
                    .contentType("application/json")
                    .body(user)
                    .when()
                    .post("https://restapi.wcaquino.me/users")
                    .then()
                    .log().all()
                    .statusCode(201)//status inseriu
                    .extract().body().as(User.class);
            ;
            System.out.println(usuarioInserido);
            Assert.assertThat(usuarioInserido.getId(),notNullValue());
            Assert.assertEquals("Usuario deserializado",user.getName());
            Assert.assertThat(usuarioInserido.getAge(),is(35));
            Assert.assertEquals(Double.valueOf(2.500) ,user.getSalary());
        }


        @Test
        public void naoDeveSalvarUsuarioSemNome(){
            given()
                .log().all()
                .contentType("application/json")
                .body("{\"age\": 50 }")
            .when()
                .post("https://restapi.wcaquino.me/users")
            .then()
                .log().all()
                .statusCode(400)//bad request
                .body("id",is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"));
                ;
        }

        @Test
        public void deveSalvarUsuarioViaXML(){
            given()
                .log().all()
//                .contentType("application/xml")
                .contentType(ContentType.XML)//mais indicado
                .body("<user><name>Jose</name><age>50</age></user>")
            .when()
                .post("https://restapi.wcaquino.me/usersXML")
            .then()
                .log().all()
                .statusCode(201)//status inseriu
                .body("user.@id", is(notNullValue()))
                .body("user.name",is("Jose"))
                .body("user.age", is("50"))
            ;
        }

        @Test
        public void deveSalvarUsuarioViaXMLUsandoObjeto(){
            User user = new User("Usuario XML",40,2.000);

            given()
                .log().all()
                .contentType(ContentType.XML)//mais indicado
                .body(user)
            .when()
                .post("https://restapi.wcaquino.me/usersXML")
            .then()
                .log().all()
                .statusCode(201)//status inseriu
                .body("user.@id", is(notNullValue()))
                .body("user.name",is("Usuario XML"))
                .body("user.age", is("40"))
            ;
        }

        @Test
        public void deveDeserializarXMLAoSalvarUsuario(){
            User user = new User("Usuario XML",40,2.000);

            User usuarioInserido = given()
                .log().all()
                .contentType(ContentType.XML)
                .body(user)
            .when()
                .post("https://restapi.wcaquino.me/usersXML")
            .then()
                .log().all()
                .statusCode(201)//status inseriu
                .extract().body().as(User.class)
            ;
            Assert.assertThat(usuarioInserido.getId(),notNullValue());
            Assert.assertThat(usuarioInserido.getName(), is("Usuario XML"));
            Assert.assertThat(usuarioInserido.getAge(),is(40));
            Assert.assertThat(usuarioInserido.getSalary(),is(2.0));
        }


        @Test
        public void deveAlterarUsuario(){
            given()
                .log().all()
                .contentType("application/json")
                .contentType(ContentType.XML)//mais indicado
                .body("{ \"name\":\"Usuario alterado\",\"age\":80}")
            .when()
                .put("https://restapi.wcaquino.me/users/1")
            .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name",is("Usuario alterado"))
                .body("age", is(80))
                .body("salary",is(1234.5678f))
            ;
        }

            @Test
            public void devoCustomizarURLParte1(){
                given()
                    .log().all()
                    .contentType("application/json")
                    .body("{ \"name\":\"Usuario alterado\",\"age\":80}")
                .when()
                        .put("https://restapi.wcaquino.me/{entidade}/{userId}","users","1")
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("id", is(1))
                        .body("name",is("Usuario alterado"))
                        .body("age", is(80))
                        .body("salary",is(1234.5678f))
                ;
            }

            @Test
            public void devoCustomizarURLParte2(){
                given()
                    .log().all()
                    .contentType("application/json")
                    .body("{ \"name\":\"Usuario alterado\",\"age\":80}")
                    .pathParam("entidade","users")
                    .pathParam("userId",1)
                .when()
                    .put("https://restapi.wcaquino.me/{entidade}/{userId}")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("id", is(1))
                    .body("name",is("Usuario alterado"))
                    .body("age", is(80))
                    .body("salary",is(1234.5678f))
                ;
            }

        @Test
        public void deveRemoverUsuario(){
            given()
                .log().all()
            .when()
                .delete("https://restapi.wcaquino.me/users/1")
            .then()
                .log().all()
                .statusCode(204)
            ;
        }

        @Test
        public void naodeveRemoverUsuarioInexistente(){
            given()
                    .log().all()
                    .when()
                    .delete("https://restapi.wcaquino.me/users/1000")
                    .then()
                    .log().all()
                    .statusCode(400)
                    .body("error",is("Registro inexistente"))
            ;
        }
    }


