package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/*
 * Teste SoftDesign
 * Davi Rodrigues Gonçalves
 * API - Java + RestAssured + Junit4
 */

//OBS: Decidi criar uma classe específica para a validar o cenário de produto e não deixar
// o mesmo junto com os cenários de usuários para melhor organização
public class TesteDeProduto {

    /*
    3. Criação de Produto
     */
    @Test
    public void teste6CriacaoDeProduto() {

        RestAssured.baseURI = "https://dummyjson.com";

        String requestBody = "{\n" +
                "  \"title\": \"Perfume Oil\",\n" +
                "  \"description\": \"Mega Discount, Impression of A...\",\n" +
                "  \"price\": 13,\n" +
                "  \"discountPercentage\": 8.4,\n" +
                "  \"rating\": 4.26,\n" +
                "  \"stock\": 65,\n" +
                "  \"brand\": \"Impression of Acqua Di Gio\",\n" +
                "  \"category\": \"fragrances\",\n" +
                "  \"thumbnail\": \"https://i.dummyjson.com/data/products/11/thumnail.jpg\"\n" +
                "}";

        TestesDeUsuarios user = new TestesDeUsuarios();
        user.teste4Login();

        // Validando adição de produto
        try {
            Response response =
                    given()
                            .header("Authorization", "Bearer " + user.token)
                            .contentType(ContentType.JSON)
                            .body(requestBody)
                            .when()
                            .post("/auth/products/add")
                            .then()
                            .statusCode(201)
                            .body("title", equalTo("Perfume Oil"))
                            .body("description", equalTo("Mega Discount, Impression of A..."))
                            .body("price", equalTo(13))
                            .body("discountPercentage", equalTo(8.4f))
                            .body("rating", equalTo(4.26f))
                            .body("stock", equalTo(65))
                            .body("brand", equalTo("Impression of Acqua Di Gio"))
                            .body("category", equalTo("fragrances"))
                            .body("thumbnail", equalTo("https://i.dummyjson.com/data/products/11/thumnail.jpg"))
                            .extract()
                            .response();

            Assert.assertNotNull(response);
            System.out.println("Código de status: " + response.statusCode() + "\nProduto adicionado com sucesso!");
        }catch(AssertionError e){
            Assert.fail("Falha ao adicionar produto");
        }

    }
}
