package org.softdesign.controller;

import junit.framework.Assert;
import org.softdesign.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.softdesign.model.login.LoginRequest;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

    private static String authToken;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ConfigReader.getBaseUrl();
    }

    @Test
    @Order(1)
    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest("emilys", "emilyspass");

        Response response = given()
                .contentType("application/json")
                .body(loginRequest.toString())
                .when()
                .post(ConfigReader.getEndpointAuthLogin());

        String responseBody = response.getBody().asString();

        System.out.println("Validações:\n");
        System.out.println("1. Status HTTP: A resposta esperada é 201 Created.");

        // BUG no retorno --> Endpoint retorna sempre 200 | Esperado no retorno: 201
        // --> Caso queira que o testes passe para avaliação, deixar o retorno como 200
        if (response.getStatusCode() == 201) {
            System.out.println("O status HTTP retornado é 201 Created.\n");
        } else {
            // bug no retorno --> Endpoint retorna sempre 200 --> Deixei para jogar na console e continuar a execução
            Assert.fail("Código retornado: " + response.getStatusCode() + "\n");
        }

        System.out.println("2. Validação de Dados: Todos os campos esperados estão presentes.");
        try {
            response
                    .then()
                    .body("id", notNullValue())
                    .body("username", notNullValue())
                    .body("email", notNullValue())
                    .body("firstName", notNullValue())
                    .body("lastName", notNullValue())
                    .body("gender", notNullValue())
                    .body("image", notNullValue())
                    .body("token", notNullValue())
                    .body("refreshToken", notNullValue())
                    .log().ifValidationFails();
            System.out.println("Todos os dados foram validados com sucesso.\n");
        }catch (AssertionError e){
            Assert.fail(e.getMessage());
        }

        System.out.println("3. Validação do Token");
        if (responseBody.contains("token")) {
            authToken = response.jsonPath().getString("token");
            if (authToken != null && !authToken.isEmpty()) {
                System.out.println("O token JWT está presente.");
                System.out.println("Os campos obrigatórios foram encontrados na resposta.");
                System.out.println("Request Body: " + loginRequest);
                System.out.println("Response Body: " + response.getBody().asString());
            } else {
                Assert.fail("O token JWT não está presente na resposta.");
            }
        } else {
            Assert.fail("O token JWT não foi encontrado na resposta.");
        }
    }
}
