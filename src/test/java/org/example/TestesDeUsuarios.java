package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/*
 * Teste SoftDesign
 * Davi Rodrigues Gonçalves
 * API - Java + RestAssured + Junit4
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestesDeUsuarios extends TesteDeProduto {

    private static final String BASE_URL = "https://dummyjson.com/users";
    private static final String LOGIN_URL = "https://dummyjson.com/auth/login";

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    /*
    1. Verificação de Usuários
     */

    //Status HTTP: A resposta esperada é 200 OK.
    @Test
    public void teste1CodigoDeStatus() {

        Response response = RestAssured.get();

        // Verifica se o status code da resposta é 200.
        try {
            Assert.assertEquals(200, response.getStatusCode());
            System.out.println("Código de status: " + response.getStatusCode() + "\nCódigo retornado corretamente\n\n\n");
        }catch (AssertionError e){
            Assert.fail("Código de status: " + response.getStatusCode() + "\nCódigo retornado incorretamente\n\n\n");
        }
    }

    //Dados Obrigatórios
    @Test
    public void teste2DadosObrigatorios() {

        Response response = RestAssured.get();

        List<Map<String, Object>> usuarios = response.jsonPath().getList("users");

        boolean erro = false;
        for (Map<String, Object> usuario : usuarios) {
            try {

                System.out.println("Verificando usuário: " + usuario);

                // Verifica se todos os campos obrigatórios estão presentes.
                Assert.assertTrue("Campo 'id' não encontrado", usuario.containsKey("id"));
                Assert.assertTrue("Campo 'firstName' não encontrado", usuario.containsKey("firstName"));
                Assert.assertTrue("Campo 'lastName' não encontrado", usuario.containsKey("lastName"));
                Assert.assertTrue("Campo 'age' não encontrado", usuario.containsKey("age"));
                Assert.assertTrue("Campo 'gender' não encontrado", usuario.containsKey("gender"));
                Assert.assertTrue("Campo 'email' não encontrado", usuario.containsKey("email"));
                Assert.assertTrue("Campo 'username' não encontrado", usuario.containsKey("username"));
                Assert.assertTrue("Campo 'birthDate' não encontrado", usuario.containsKey("birthDate"));
                Assert.assertTrue("Campo 'role' não encontrado", usuario.containsKey("role"));

                System.out.println("Usuário ID:" + usuario.get("id") + " - " + usuario.get("firstName") + " " + usuario.get("lastName") +
                        ", " + "verificado com sucesso!\n");

            } catch (AssertionError e) {
                System.out.println("Erro no usuário: " + usuario);
                System.out.println("ERRO: " + e.getMessage());
                erro = true;
                System.out.println();
            }
        }
        if (erro == true){
            Assert.fail("Falha na validação. Confira os logs.\n\n\n");
        }else{
            System.out.println("Não foram encontrados erros na validação de dados\n\n\n");
        }
    }

    //Paginação: O endpoint retorna no máximo 30 usuários por página.
    @Test
    public void teste3Paginacao() {

        Response response = RestAssured.get();

        int totalDeUsuarios = response.jsonPath().getInt("total");
        List<Map<String, Object>> usuarios = response.jsonPath().getList("users");
        System.out.println("O total de usuários é: " + totalDeUsuarios);

        // Verifica se a lista de usuários retornada contém no máximo 30 usuários por página.
        try {
            Assert.assertTrue("Validar limite máximo de 30 usuários por página.", usuarios.size() <= 30);
            System.out.println("Limite máximo de 30 usuários por página validado com sucesso.\n\n\n");
        }catch (AssertionError e){
            Assert.fail("A página não deve retornar no máximo 30 usuários.\n\n\n");
        }

    }

    /*
    2. Autenticação de Login
     */
    String token;
    @Test
    public void teste4Login() {

        String loginPayload = "{\n" +
                "    \"username\": \"emilys\",\n" +
                "    \"password\": \"emilyspass\"\n" +
                "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginPayload)
                .post(LOGIN_URL);

        // Status HTTP: A resposta esperada para um login bem-sucedido é 201
        // Foi encontrado um erro de documentação, pois na DOC informa que o retorno esperado na resposta seria 201 em caso de sucesso,
        // porém, ao efetuar o login, está sendo retornado 200 e não 201. Logo com o teste configurado para receber 201 conforme a documentação
        // o mesmo vai falhar.
        // OBS: Para resultar em um teste de sucesso, na linha de assertEquals, o expected deve ser configurado como 200
        try {
            Assert.assertEquals(201, response.getStatusCode());
            System.out.println("Código de status: " + response.getStatusCode());
        }catch (AssertionError e){
            Assert.fail("Código de status: " + response.getStatusCode() + "\nFalha ao efetuar login\n\n\n");
        }

        Map<String, Object> loginResponse = response.jsonPath().getMap("$");

        // Validação de Dados: Verifique se todos os campos esperados
        // (id,username, email, firstName, lastName, gender, image, token,refreshToken)
        // estão presentes na resposta.
        Assert.assertTrue("Campo 'id' não encontrado", loginResponse.containsKey("id"));
        Assert.assertTrue("Campo 'username' não encontrado", loginResponse.containsKey("username"));
        Assert.assertTrue("Campo 'email' não encontrado", loginResponse.containsKey("email"));
        Assert.assertTrue("Campo 'firstName' não encontrado", loginResponse.containsKey("firstName"));
        Assert.assertTrue("Campo 'lastName' não encontrado", loginResponse.containsKey("lastName"));
        Assert.assertTrue("Campo 'gender' não encontrado", loginResponse.containsKey("gender"));
        Assert.assertTrue("Campo 'image' não encontrado", loginResponse.containsKey("image"));
        Assert.assertTrue("Campo 'token' não encontrado", loginResponse.containsKey("token"));

        // Valide o token JWT
        //OBS: Não adicionei verificação de codificação pois não tem informação na DOC referente ao tipo de codificação
        token = (String) loginResponse.get("token");
        Assert.assertNotNull("Token JWT está ausente", token);

        System.out.println("Login bem-sucedido!\nDados do usuário:\n"
                + "Token: " + loginResponse.get("token") + "\n" + "RefreshToken: " + loginResponse.get("refreshToken") + "\n"
                + "ID: " +loginResponse.get("id") + "\n" + "Username: " + loginResponse.get("username") + "\n"
                + "Email: " +loginResponse.get("email") + "\n" + "FirstName: " +loginResponse.get("firstName") + "\n"
                + "LastName: " +loginResponse.get("lastName") + "\n" + "Gender: " +loginResponse.get("gender") + "\n"
                + "Image: " +loginResponse.get("image") + "\n\n\n");

        //OBS: Na documentação também estava endo solicitado que fosse validado a paginação novamente, após login porém
        // entendo que este passo foi adicionado erronearmente como critério de validação de login com sucesso visto que
        // a mesma validação foi verificada no passo de retorno de dados na api de /users
    }

    /*
    4.  Consulta de Usuário por ID
     */
    @Test
    public void teste5ConsultarUsuarioPorID() {

        RestAssured.baseURI = "https://dummyjson.com";

        // Validando response para validar todos os dados retornados com sucesso
        try {
            given()
                    .when()
                    .get("/users/1")
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("firstName", equalTo("Emily"))
                    .body("lastName", equalTo("Johnson"))
                    .body("maidenName", equalTo("Smith"))
                    .body("age", equalTo(28))
                    .body("gender", equalTo("female"))
                    .body("email", equalTo("emily.johnson@x.dummyjson.com"))
                    .body("phone", equalTo("+81 965-431-3024"))
                    .body("username", equalTo("emilys"))
                    .body("password", equalTo("emilyspass"))
                    .body("birthDate", equalTo("1996-5-30"))
                    .body("bloodGroup", equalTo("O-"))
                    .body("height", equalTo(193.24f))
                    .body("weight", equalTo(63.16f))
                    .body("eyeColor", equalTo("Green"))
                    .body("hair.color", equalTo("Brown"))
                    .body("hair.type", equalTo("Curly"))
                    .body("ip", equalTo("42.48.100.32"))
                    .body("address.address", equalTo("626 Main Street"))
                    .body("address.city", equalTo("Phoenix"))
                    .body("address.state", equalTo("Mississippi"))
                    .body("address.stateCode", equalTo("MS"))
                    .body("address.postalCode", equalTo("29112"))
                    .body("address.coordinates.lat", equalTo(-77.16213f))
                    .body("address.coordinates.lng", equalTo(-92.084824f))
                    .body("address.country", equalTo("United States"))
                    .body("macAddress", equalTo("47:fa:41:18:ec:eb"))
                    .body("university", equalTo("University of Wisconsin--Madison"))
                    .body("bank.cardExpire", equalTo("03/26"))
                    .body("bank.cardNumber", equalTo("9289760655481815"))
                    .body("bank.cardType", equalTo("Elo"))
                    .body("bank.currency", equalTo("CNY"))
                    .body("bank.iban", equalTo("YPUXISOBI7TTHPK2BR3HAIXL"))
                    .body("company.department", equalTo("Engineering"))
                    .body("company.name", equalTo("Dooley, Kozey and Cronin"))
                    .body("company.title", equalTo("Sales Manager"))
                    .body("company.address.address", equalTo("263 Tenth Street"))
                    .body("company.address.city", equalTo("San Francisco"))
                    .body("company.address.state", equalTo("Wisconsin"))
                    .body("company.address.stateCode", equalTo("WI"))
                    .body("company.address.postalCode", equalTo("37657"))
                    .body("company.address.coordinates.lat", equalTo(71.814525f))
                    .body("company.address.coordinates.lng", equalTo(-161.150263f))
                    .body("company.address.country", equalTo("United States"))
                    .body("ein", equalTo("977-175"))
                    .body("ssn", equalTo("900-590-289"))
                    .body("userAgent", equalTo("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36"))
                    .body("crypto.coin", equalTo("Bitcoin"))
                    .body("crypto.wallet", equalTo("0xb9fc2fe63b2a6c003f1c324c3bfa53259162181a"))
                    .body("crypto.network", equalTo("Ethereum (ERC20)"))
                    .body("role", equalTo("admin"));

            System.out.println("\nValidação dos dados com sucesso!\n\n\n");

        }catch (AssertionError e){
            Assert.fail("Falha na validação dos dados\n\n\n");
        }

    }
}