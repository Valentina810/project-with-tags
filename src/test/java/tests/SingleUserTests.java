package tests;

import io.qameta.allure.AllureId;
import model.user.CreatedUser;
import model.user.UpdatedUser;
import model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static properties.TestTags.ENTITIES;
import static properties.TestTags.ENTITY;
import static properties.TestTags.MANY_ENTITIES;
import static properties.TestTags.SMOKE;

@Execution(value = CONCURRENT)
@Tag(ENTITIES)
@Tag(ENTITY)
public class SingleUserTests extends TestBase {

    @Test
    @Tag(SMOKE)
    @AllureId("74")
    @DisplayName("Создание пользователя")
    public void testCreateUser() {
        CreatedUser createdUser = CreatedUser.builder().name("morpheus").job("zion resident").build();
        User user = given().contentType("application/json")
                .body(createdUser)
                .when().post("/api/users").then().statusCode(SC_CREATED).log().body()
                .extract().body().as(User.class);
        assertAll(
                () -> assertNotNull(user.getId()),
                () -> assertEquals(createdUser.getJob(), createdUser.getJob()),
                () -> assertEquals(createdUser.getName(), createdUser.getName())
        );
    }

    @Test
    @Tag(SMOKE)
    @AllureId("75")
    @DisplayName("Обновление пользователя")
    public void testUpdateUser() {
        CreatedUser user = CreatedUser.builder().name("olga").job("leader").build();
        UpdatedUser updatedUser = given().contentType("application/json")
                .body(user)
                .when().put("/api/users/4").then().statusCode(SC_OK).log().body()
                .extract().body().as(UpdatedUser.class);
        assertAll(
                () -> assertEquals(user.getJob(), updatedUser.getJob()),
                () -> assertEquals(user.getName(), updatedUser.getName())
        );
    }

    @Test
    @AllureId("76")
    @DisplayName("Удаление пользователя")
    public void testDeleteUser() {
        given().when().delete("/api/users/3").then().statusCode(SC_NO_CONTENT);
    }
}
