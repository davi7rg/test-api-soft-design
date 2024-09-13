package org.softdesign.controller;

import io.restassured.response.Response;
import junit.framework.Assert;
import org.softdesign.config.ConfigReader;
import io.restassured.RestAssured;
import org.softdesign.model.user.UserResponse;
import org.junit.jupiter.api.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ConfigReader.getBaseUrl();
        RestAssured.basePath = ConfigReader.getEndpointUsers();
    }

    @Test
    @Order(1)
    public void testGetUsers() {

        Response response = given()
                .when()
                .get();

        List<Map<String, Object>> users = response.jsonPath().getList("users");

        System.out.println("Validações:");
        System.out.println("1. Status HTTP: A resposta esperada é 200 OK.");
        try {
            Assert.assertEquals(200, response.getStatusCode());
            System.out.println("Código de status: " + response.getStatusCode() + "\nCódigo retornado corretamente\n\n\n");
        }catch (AssertionError e){
            Assert.fail("Código de status: " + response.getStatusCode() + "\nCódigo retornado incorretamente\n\n\n");
        }

        System.out.println("2. Paginação: O endpoint retorna no máximo 30 usuários por página.");
        try {
            Assert.assertTrue("Validar limite máximo de 30 usuários por página.", users.size() <= 30);
            System.out.println("Número de usuários retornado: " + users.size());
            System.out.println("Limite máximo de 30 usuários por página validado com sucesso.\n\n\n");
        }catch (AssertionError e){
            Assert.fail("A página não deve retornar no máximo 30 usuários.\n\n\n");
        }

        System.out.println("3. Dados obrigatorios: Certificar que os dados obrigatorios estão preenchidos.");
        boolean error = false;
        for (Map<String, Object> user : users) {
            try {
                System.out.println("Verificando usuário: " + user);

                // Verifica se todos os campos obrigatórios estão presentes.
                response
                        .then()
                        .body("users.id", everyItem(notNullValue()))
                        .body("users.firstName", everyItem(notNullValue()))
                        .body("users.lastName", everyItem(notNullValue()))
                        .body("users.age", everyItem(notNullValue()))
                        .body("users.gender", everyItem(notNullValue()))
                        .body("users.email", everyItem(notNullValue()))
                        .body("users.username", everyItem(notNullValue()))
                        .body("users.birthDate", everyItem(notNullValue()))
                        .body("users.role", everyItem(notNullValue()));

                System.out.println("Usuário ID:" + user.get("id") + " - " + user.get("firstName") + " " + user.get("lastName") +
                        ", " + "verificado com sucesso!\n");

            } catch (AssertionError e) {
                System.out.println("Erro no usuário: " + user);
                System.out.println("ERRO: " + e.getMessage());
                error = true;
                System.out.println();
            }
        }
        if (error != true){
            System.out.println("Não foram encontrados erros na validação de dados\n\n\n");
        }else{
            Assert.fail("Falha na validação. Confira os logs.\n\n\n");
        }
    }

    @Test
    @Order(2)
    public void testGetUserById() {
        int userId = 1;

        Response response = given()
                .when()
                .get("/" + userId);

        response.then().statusCode(200);
        UserResponse userResponse = response.as(UserResponse.class);

        assertEquals(userId, userResponse.getId(),
                MessageFormat.format("ID do usuário esperado: {0}, encontrado: {1}. Response: {2}",
                        userId,
                        userResponse.getId(),
                        response.getBody().asString())
        );
        assertEquals("Emily", userResponse.getFirstName(),
                MessageFormat.format("Primeiro nome esperado: {0}, encontrado: {1}. Response: {2}",
                        "Emily",
                        userResponse.getFirstName(),
                        response.getBody().asString())
        );
        assertEquals("Johnson", userResponse.getLastName(),
                MessageFormat.format("Último nome esperado: {0}, encontrado: {1}. Response: {2}",
                        "Johnson",
                        userResponse.getLastName(),
                        response.getBody().asString())
        );
        assertEquals("Smith", userResponse.getMaidenName(),
                MessageFormat.format("Nome de solteiro esperado: {0}, encontrado: {1}. Response: {2}",
                        "Smith",
                        userResponse.getMaidenName(),
                        response.getBody().asString())
        );
        assertEquals(28, userResponse.getAge(),
                MessageFormat.format("Idade esperada: {0}, encontrada: {1}. Response: {2}",
                        28,
                        userResponse.getAge(),
                        response.getBody().asString())
        );
        assertEquals("female", userResponse.getGender(),
                MessageFormat.format("Gênero esperado: {0}, encontrado: {1}. Response: {2}",
                        "female",
                        userResponse.getGender(),
                        response.getBody().asString())
        );
        assertEquals("emily.johnson@x.dummyjson.com", userResponse.getEmail(),
                MessageFormat.format("Email esperado: {0}, encontrado: {1}. Response: {2}",
                        "emily.johnson@x.dummyjson.com",
                        userResponse.getEmail(),
                        response.getBody().asString())
        );
        assertEquals("+81 965-431-3024", userResponse.getPhone(),
                MessageFormat.format("Telefone esperado: {0}, encontrado: {1}. Response: {2}",
                        "+81 965-431-3024",
                        userResponse.getPhone(),
                        response.getBody().asString())
        );
        assertEquals("emilys", userResponse.getUsername(),
                MessageFormat.format("Nome de usuário esperado: {0}, encontrado: {1}. Response: {2}",
                        "emilys",
                        userResponse.getUsername(),
                        response.getBody().asString())
        );
        assertEquals("emilyspass", userResponse.getPassword(),
                MessageFormat.format("Senha esperada: {0}, encontrada: {1}. Response: {2}",
                        "emilyspass",
                        userResponse.getPassword(),
                        response.getBody().asString())
        );
        assertEquals("1996-5-30", userResponse.getBirthDate(),
                MessageFormat.format("Data de nascimento esperada: {0}, encontrada: {1}. Response: {2}",
                        "1996-5-30",
                        userResponse.getBirthDate(),
                        response.getBody().asString())
        );

        // BUG --> image correta é: "..."
        // No retorno está com o valor 'https://dummyjson.com/icon/emilys/128',
        // Caso queiram que passe, trocar o esperado para o valor 'https://dummyjson.com/icon/emilys/128'
        assertEquals("...", userResponse.getImage(),
                MessageFormat.format("Imagem esperada: {0}, encontrada: {1}. Response: {2}",
                        "...",
                        userResponse.getImage(),
                        response.getBody().asString())
        );
        assertEquals("O-", userResponse.getBloodGroup(),
                MessageFormat.format("Grupo sanguíneo esperado: {0}, encontrado: {1}. Response: {2}",
                        "O-",
                        userResponse.getBloodGroup(),
                        response.getBody().asString())
        );
        assertEquals(193.24, userResponse.getHeight(), 0.01,
                MessageFormat.format("Altura esperada: {0}, encontrada: {1}. Response: {2}",
                        193.24,
                        userResponse.getHeight(),
                        response.getBody().asString())
        );
        assertEquals(63.16, userResponse.getWeight(), 0.01,
                MessageFormat.format("Peso esperado: {0}, encontrado: {1}. Response: {2}",
                        63.16,
                        userResponse.getWeight(),
                        response.getBody().asString())
        );
        assertEquals("Green", userResponse.getEyeColor(),
                MessageFormat.format("Cor dos olhos esperada: {0}, encontrada: {1}. Response: {2}",
                        "Green",
                        userResponse.getEyeColor(),
                        response.getBody().asString())
        );

        // Valida sub-objetos
        assertEquals("Brown", userResponse.getHair().getColor(),
                MessageFormat.format("Cor do cabelo esperada: {0}, encontrada: {1}. Response: {2}",
                        "Brown",
                        userResponse.getHair().getColor(),
                        response.getBody().asString())
        );
        assertEquals("Curly", userResponse.getHair().getType(),
                MessageFormat.format("Tipo de cabelo esperado: {0}, encontrado: {1}. Response: {2}",
                        "Curly",
                        userResponse.getHair().getType(),
                        response.getBody().asString())
        );

        assertEquals("42.48.100.32", userResponse.getIp(),
                MessageFormat.format("IP esperado: {0}, encontrado: {1}. Response: {2}",
                        "42.48.100.32",
                        userResponse.getIp(),
                        response.getBody().asString())
        );
        assertEquals("626 Main Street", userResponse.getAddress().getAddress(),
                MessageFormat.format("Endereço esperado: {0}, encontrado: {1}. Response: {2}",
                        "626 Main Street",
                        userResponse.getAddress().getAddress(),
                        response.getBody().asString())
        );
        assertEquals("Phoenix", userResponse.getAddress().getCity(),
                MessageFormat.format("Cidade esperada: {0}, encontrada: {1}. Response: {2}",
                        "Phoenix",
                        userResponse.getAddress().getCity(),
                        response.getBody().asString())
        );
        assertEquals("Mississippi", userResponse.getAddress().getState(),
                MessageFormat.format("Estado esperado: {0}, encontrado: {1}. Response: {2}",
                        "Mississippi",
                        userResponse.getAddress().getState(),
                        response.getBody().asString())
        );
        assertEquals("MS", userResponse.getAddress().getStateCode(),
                MessageFormat.format("Código do estado esperado: {0}, encontrado: {1}. Response: {2}",
                        "MS",
                        userResponse.getAddress().getStateCode(),
                        response.getBody().asString())
        );
        assertEquals("29112", userResponse.getAddress().getPostalCode(),
                MessageFormat.format("Código postal esperado: {0}, encontrado: {1}. Response: {2}",
                        "29112",
                        userResponse.getAddress().getPostalCode(),
                        response.getBody().asString())
        );
        assertEquals(-77.16213, userResponse.getAddress().getCoordinates().getLat(), 0.01,
                MessageFormat.format("Latitude esperada: {0}, encontrada: {1}. Response: {2}",
                        -77.16213,
                        userResponse.getAddress().getCoordinates().getLat(),
                        response.getBody().asString())
        );
        assertEquals(-92.084824, userResponse.getAddress().getCoordinates().getLng(), 0.01,
                MessageFormat.format("Longitude esperada: {0}, encontrada: {1}. Response: {2}",
                        -92.084824,
                        userResponse.getAddress().getCoordinates().getLng(),
                        response.getBody().asString())
        );
        assertEquals("United States", userResponse.getAddress().getCountry(),
                MessageFormat.format("País esperado: {0}, encontrado: {1}. Response: {2}",
                        "United States",
                        userResponse.getAddress().getCountry(),
                        response.getBody().asString())
        );

        assertEquals("47:fa:41:18:ec:eb", userResponse.getMacAddress(),
                MessageFormat.format("Endereço MAC esperado: {0}, encontrado: {1}. Response: {2}",
                        "47:fa:41:18:ec:eb",
                        userResponse.getMacAddress(),
                        response.getBody().asString())
        );
        assertEquals("University of Wisconsin--Madison", userResponse.getUniversity(),
                MessageFormat.format("Universidade esperada: {0}, encontrada: {1}. Response: {2}",
                        "University of Wisconsin--Madison",
                        userResponse.getUniversity(),
                        response.getBody().asString())
        );

        assertEquals("03/26", userResponse.getBank().getCardExpire(),
                MessageFormat.format("Data de expiração do cartão esperada: {0}, encontrada: {1}. Response: {2}",
                        "03/26",
                        userResponse.getBank().getCardExpire(),
                        response.getBody().asString())
        );
        assertEquals("9289760655481815", userResponse.getBank().getCardNumber(),
                MessageFormat.format("Número do cartão esperado: {0}, encontrado: {1}. Response: {2}",
                        "9289760655481815",
                        userResponse.getBank().getCardNumber(),
                        response.getBody().asString())
        );
        assertEquals("Elo", userResponse.getBank().getCardType(),
                MessageFormat.format("Tipo de cartão esperado: {0}, encontrado: {1}. Response: {2}",
                        "Elo",
                        userResponse.getBank().getCardType(),
                        response.getBody().asString())
        );
        assertEquals("CNY", userResponse.getBank().getCurrency(),
                MessageFormat.format("Moeda do cartão esperada: {0}, encontrada: {1}. Response: {2}",
                        "CNY",
                        userResponse.getBank().getCurrency(),
                        response.getBody().asString())
        );
        assertEquals("YPUXISOBI7TTHPK2BR3HAIXL", userResponse.getBank().getIban(),
                MessageFormat.format("IBAN esperado: {0}, encontrado: {1}. Response: {2}",
                        "YPUXISOBI7TTHPK2BR3HAIXL",
                        userResponse.getBank().getIban(),
                        response.getBody().asString())
        );
        assertEquals("Engineering", userResponse.getCompany().getDepartment(),
                MessageFormat.format("Departamento da empresa esperado: {0}, encontrado: {1}. Response: {2}",
                        "Engineering",
                        userResponse.getCompany().getDepartment(),
                        response.getBody().asString())
        );
        assertEquals("Dooley, Kozey and Cronin", userResponse.getCompany().getName(),
                MessageFormat.format("Nome da empresa esperado: {0}, encontrado: {1}. Response: {2}",
                        "Dooley, Kozey and Cronin",
                        userResponse.getCompany().getName(),
                        response.getBody().asString())
        );
        assertEquals("Sales Manager", userResponse.getCompany().getTitle(),
                MessageFormat.format("Título na empresa esperado: {0}, encontrado: {1}. Response: {2}",
                        "Sales Manager",
                        userResponse.getCompany().getTitle(),
                        response.getBody().asString())
        );
        assertEquals("263 Tenth Street", userResponse.getCompany().getAddress().getAddress(),
                MessageFormat.format("Endereço da empresa esperado: {0}, encontrado: {1}. Response: {2}",
                        "263 Tenth Street",
                        userResponse.getCompany().getAddress().getAddress(),
                        response.getBody().asString())
        );
        assertEquals("San Francisco", userResponse.getCompany().getAddress().getCity(),
                MessageFormat.format("Cidade da empresa esperada: {0}, encontrada: {1}. Response: {2}",
                        "San Francisco",
                        userResponse.getCompany().getAddress().getCity(),
                        response.getBody().asString())
        );
        assertEquals("Wisconsin", userResponse.getCompany().getAddress().getState(),
                MessageFormat.format("Estado da empresa esperado: {0}, encontrado: {1}. Response: {2}",
                        "Wisconsin",
                        userResponse.getCompany().getAddress().getState(),
                        response.getBody().asString())
        );
        assertEquals("WI", userResponse.getCompany().getAddress().getStateCode(),
                MessageFormat.format("Código do estado da empresa esperado: {0}, encontrado: {1}. Response: {2}",
                        "WI",
                        userResponse.getCompany().getAddress().getStateCode(),
                        response.getBody().asString())
        );
        assertEquals("37657", userResponse.getCompany().getAddress().getPostalCode(),
                MessageFormat.format("Código postal da empresa esperado: {0}, encontrado: {1}. Response: {2}",
                        "37657",
                        userResponse.getCompany().getAddress().getPostalCode(),
                        response.getBody().asString())
        );
        assertEquals(71.814525, userResponse.getCompany().getAddress().getCoordinates().getLat(), 0.01,
                MessageFormat.format("Latitude do endereço da empresa esperada: {0}, encontrada: {1}. Response: {2}",
                        71.814525,
                        userResponse.getCompany().getAddress().getCoordinates().getLat(),
                        response.getBody().asString())
        );
        assertEquals(-161.150263, userResponse.getCompany().getAddress().getCoordinates().getLng(), 0.01,
                MessageFormat.format("Longitude do endereço da empresa esperada: {0}, encontrada: {1}. Response: {2}",
                        -161.150263,
                        userResponse.getCompany().getAddress().getCoordinates().getLng(),
                        response.getBody().asString())
        );
        assertEquals("United States", userResponse.getCompany().getAddress().getCountry(),
                MessageFormat.format("País do endereço da empresa esperado: {0}, encontrado: {1}. Response: {2}",
                        "United States",
                        userResponse.getCompany().getAddress().getCountry(),
                        response.getBody().asString())
        );

        assertEquals("977-175", userResponse.getEin(),
                MessageFormat.format("EIN esperado: {0}, encontrado: {1}. Response: {2}",
                        "977-175",
                        userResponse.getEin(),
                        response.getBody().asString())
        );
        assertEquals("900-590-289", userResponse.getSsn(),
                MessageFormat.format("SSN esperado: {0}, encontrado: {1}. Response: {2}",
                        "900-590-289",
                        userResponse.getSsn(),
                        response.getBody().asString())
        );
        assertEquals("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36",
                userResponse.getUserAgent(),
                MessageFormat.format("User Agent esperado: {0}, encontrado: {1}. Response: {2}",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36",
                        userResponse.getUserAgent(),
                        response.getBody().asString())
        );
        assertEquals("Bitcoin", userResponse.getCrypto().getCoin(),
                MessageFormat.format("Moeda da criptomoeda esperada: {0}, encontrada: {1}. Response: {2}",
                        "Bitcoin",
                        userResponse.getCrypto().getCoin(),
                        response.getBody().asString())
        );
        assertEquals("0xb9fc2fe63b2a6c003f1c324c3bfa53259162181a", userResponse.getCrypto().getWallet(),
                MessageFormat.format("Carteira da criptomoeda esperada: {0}, encontrada: {1}. Response: {2}",
                        "0xb9fc2fe63b2a6c003f1c324c3bfa53259162181a",
                        userResponse.getCrypto().getWallet(),
                        response.getBody().asString())
        );
        assertEquals("Ethereum (ERC20)", userResponse.getCrypto().getNetwork(),
                MessageFormat.format("Rede da criptomoeda esperada: {0}, encontrada: {1}. Response: {2}",
                        "Ethereum (ERC20)",
                        userResponse.getCrypto().getNetwork(),
                        response.getBody().asString())
        );

        switch (userResponse.getRole()) {
            case "admin":
            case "moderator":
            case "user":
                break;
            default:
                Assert.fail(MessageFormat.format("Esperado: 'admin', 'moderator' ou 'user'. \nEncontrado: {0}. Response: {1}",
                        userResponse.getRole(),
                        response.getBody().asString())
                );
        }

        System.out.println("Dados validados com sucesso");
        System.out.println("Código retornado: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }
}
