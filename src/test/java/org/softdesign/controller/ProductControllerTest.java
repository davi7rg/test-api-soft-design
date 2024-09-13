package org.softdesign.controller;

import junit.framework.Assert;
import org.softdesign.config.ConfigReader;
import org.softdesign.model.login.LoginRequest;
import org.softdesign.model.product.ProductRequest;
import org.softdesign.model.product.ProductResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.text.MessageFormat;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {

    private static String authToken;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ConfigReader.getBaseUrl();
        authToken = getAuthToken();
    }

    private static String getAuthToken() {
        LoginRequest loginRequest = new LoginRequest("emilys", "emilyspass");

        Response response = given()
                .contentType("application/json")
                .body(loginRequest.toString())
                .when()
                .post(ConfigReader.getEndpointAuthLogin());

        if (response.getStatusCode() == 200) {
            String token = response.jsonPath().getString("token");
            if (token != null && !token.isEmpty()) {
                System.out.println("O token JWT gerado com sucesso.");
                System.out.println("Bearer " + token);
                return token;
            } else {
                throw new IllegalStateException("Token JWT não encontrado na resposta.");
            }
        } else {
            throw new IllegalStateException("Falha ao obter token de autenticação. Status HTTP: " + response.getStatusCode());
        }
    }

    @Test
    @Order(1)
    public void testCreateProduct() {
        ProductRequest productRequest = new ProductRequest(
                "Perfume Oil",
                "Mega Discount, Impression of A...",
                13,
                8.4,
                4.26,
                65,
                "Impression of Acqua Di Gio",
                "fragrances",
                "https://i.dummyjson.com/data/products/11/thumnail.jpg"
        );

        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(productRequest.toString())
                .when()
                .post(ConfigReader.getEndpointAuthProductsAdd());

        int statusCode = response.getStatusCode();
        ProductResponse productResponse = response.as(ProductResponse.class);

        System.out.println("1. Status HTTP: A resposta esperada é 201 Created.");
        if (statusCode == 201) {
            System.out.println("Status Code: " + statusCode);
            System.out.println("O status HTTP retornado é 201 Created.");
        } else {
            System.out.println("O status HTTP retornado é " + statusCode + ", o que não é esperado.");
        }

        System.out.println("2. Validação de Dados: Todos os campos esperados estão presentes.");
        assertNotNull(productResponse);

        try{

            // BUG --> O response retorna o ID 195
            // Vou deixar conforme está no PDF. Caso queiram que passe, troque para 195
            assertEquals(101, productResponse.getId(),
                    MessageFormat.format("Id esperado: 101 - Id encontrado na response: {0} - Response: {1}",
                            productResponse.getId(), response.getBody().asString()));

            assertEquals(productRequest.getTitle(), productResponse.getTitle(),
                    MessageFormat.format("title esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getTitle(),
                            productResponse.getTitle(),
                            response.getBody().asString())
            );

            assertEquals(productRequest.getDescription(), productResponse.getDescription(),
                    MessageFormat.format("description esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getDescription(),
                            productResponse.getDescription(),
                            response.getBody().asString())
            );

            assertEquals(productRequest.getPrice(), productResponse.getPrice(),
                    MessageFormat.format("price esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getPrice(),
                            productResponse.getPrice(),
                            response.getBody().asString())
            );

            assertEquals(productRequest.getDiscountPercentage(), productResponse.getDiscountPercentage(),
                    MessageFormat.format("discountPercentage esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getPrice(),
                            productResponse.getPrice(),
                            response.getBody().asString())
            );

            assertEquals(productRequest.getRating(), productResponse.getRating(),
                    MessageFormat.format("rating esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getRating(),
                            productResponse.getRating(),
                            response.getBody().asString())
            );

            assertEquals(productRequest.getStock(), productResponse.getStock(),
                    MessageFormat.format("stock esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getStock(),
                            productResponse.getStock(),
                            response.getBody().asString())
            );

            assertEquals(productRequest.getBrand(), productResponse.getBrand(),
                    MessageFormat.format("brand esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getBrand(),
                            productResponse.getBrand(),
                            response.getBody().asString())
            );

            assertEquals(productRequest.getCategory(), productResponse.getCategory(),
                    MessageFormat.format("category esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getCategory(),
                            productResponse.getCategory(),
                            response.getBody().asString())
            );

            assertEquals(productRequest.getThumbnail(), productResponse.getThumbnail(),
                    MessageFormat.format("thumbnail esperado: {0}, encontrado: {1}. Response: {2}",
                            productRequest.getThumbnail(),
                            productResponse.getThumbnail(),
                            response.getBody().asString())
            );
            System.out.println("Todos os campos foram encontrados e validados com sucesso!");

            System.out.println("Request Body: " + productRequest);
            System.out.println("Response Body: " + response.getBody().asString());

        }catch (AssertionError e){
            Assert.fail("Campo esperado com valor ausente\n" + e.getMessage());
        }


    }
}
