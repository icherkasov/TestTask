package org.testTask.controllerTests;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.testTask.BaseTest;
import org.testTask.ENUM.ROLE;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testData.CreatePlayerData;
import testData.DataProviders;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DeletePlayerTests extends BaseTest {

    @Description("Verify that player with correct role can be correctly deleted by supervisor")
    @Test(dataProvider = "provideRolesForCRUD", dataProviderClass = DataProviders.class)
    public void testDeleteCreatedPlayerBySupervisor(ROLE role) {

        var playerParams = CreatePlayerData.getDefaultCreateParams("ForDelete", role);
        var player = creationSteps.createPlayer(playerParams, supervisorLogin);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(supervisorLogin, player.getId());

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_NO_CONTENT, "Expecting status code for delete to be 204");
        soft.assertAll();
    }

    @Description("Verify that player with correct role can be correctly deleted by admin")
    @Test(dataProvider = "provideRolesForCRUD", dataProviderClass = DataProviders.class)
    public void testDeleteCreatedPlayerByAdmin(ROLE role) {

        var playerParams = CreatePlayerData.getDefaultCreateParams("ForDelete", role);
        var player = creationSteps.createPlayer(playerParams, supervisorLogin);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(adminLogin, player.getId());

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_NO_CONTENT, "Expecting status code for delete to be 204");
        soft.assertAll();
    }

    @Description("Verify that supervisor can't be deleted")
    @Test(dataProvider = "provideRoles", dataProviderClass = DataProviders.class)
    public void testDeleteSupervisorPlayer(ROLE role) {

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(supervisorLogin, 1);
        assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting status code 403 for request to delete supervisor");
    }

    @Description("Verify that admin can't delete other admin")
    @Test
    public void testDeleteAdminByAdmin() {
        var adminParams = CreatePlayerData.getDefaultCreateParams("ForDelete", ROLE.ADMIN);
        var admin = creationSteps.createPlayer(adminParams, supervisorLogin);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(adminLogin, admin.getId());
        SoftAssert soft = new SoftAssert();
        soft.assertNotEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_NO_CONTENT, "Expecting status code not to be 204 for request by admin to delete other admin");
        var response = getByIdSteps.getPlayerByIdResponse(admin.getId());
        //FIXME workaround for bug with 200 for get non existing player
        soft.assertFalse(response.extract().body().asString().isEmpty(), "Get player should have data after admin request to delete other admin");
        soft.assertAll();
    }


    @Description("Verify that admin can't delete himself")
    @Test
    public void testDeleteAdminHimself() {
        var adminParams = CreatePlayerData.getDefaultCreateParams("ForDelete", ROLE.ADMIN);
        var admin = creationSteps.createPlayer(adminParams, supervisorLogin);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(admin.getLogin(), admin.getId());
        SoftAssert soft = new SoftAssert();
        soft.assertNotEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_NO_CONTENT, "Expecting status code not to be 204 for request by admin to delete himself");
        var response = getByIdSteps.getPlayerByIdResponse(admin.getId());
        //FIXME workaround for bug with 200 for get non existing player
        soft.assertFalse(response.extract().body().asString().isEmpty(), "Get player should have data after admin request to delete himself");
        soft.assertAll();
    }

    @Description("Verify that admin can delete user")
    @Test
    public void testDeleteUserByAdmin() {
        var userParams = CreatePlayerData.getDefaultCreateParams("ForDelete", ROLE.USER);
        var user = creationSteps.createPlayer(userParams, supervisorLogin);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(adminLogin, user.getId());
        assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_NO_CONTENT, "Expecting status code 403 for request to delete supervisor");
        var response = getByIdSteps.getPlayerByIdResponse(user.getId());
        //FIXME workaround for bug with 200 for get non existing player
        assertTrue(response.extract().body().asString().isEmpty(), "Get player after delete returned data");
    }

    @Description("Verify that user can't delete other user")
    @Test
    public void testDeleteUserByOtherUser() {
        var userParams = CreatePlayerData.getDefaultCreateParams("ForDelete", ROLE.USER);
        var user = creationSteps.createPlayer(userParams, supervisorLogin);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(userLogin, user.getId());
        assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting status code 403 for request to delete user by other user");
    }

    @Description("Verify that user can't delete admin")
    @Test
    public void testDeleteAdminByUser() {
        var adminParams = CreatePlayerData.getDefaultCreateParams("ForDelete", ROLE.ADMIN);
        var admin = creationSteps.createPlayer(adminParams, supervisorLogin);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(userLogin, admin.getId());
        assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting status code 403 for request to delete admin by user");
    }

    @Description("Verify that user can't delete himself")
    @Test
    public void testDeleteUserHimself() {
        var userParams = CreatePlayerData.getDefaultCreateParams("ForDelete", ROLE.USER);
        var user = creationSteps.createPlayer(userParams, supervisorLogin);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(user.getLogin(), user.getId());
        SoftAssert soft = new SoftAssert();
        soft.assertNotEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_NO_CONTENT, "Expecting status code not to be 204 for request by user to delete himself");
        var response = getByIdSteps.getPlayerByIdResponse(user.getId());
        //FIXME workaround for bug with 200 for get non existing player
        soft.assertFalse(response.extract().body().asString().isEmpty(), "Get player should have data after user request to delete himself");
        soft.assertAll();
    }

    @Description("Verify that deletion of player with non-existing id is rejected")
    @Test
    public void testDeleteNonExistingPlayer() {

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(supervisorLogin, Long.MAX_VALUE);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting status code for delete non existing player to be 403");
        soft.assertAll();
    }
}
