package org.testTask.controllerTests;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.testTask.BaseTest;
import org.testTask.DTO.CreatePlayerResponseDTO;
import org.testTask.DTO.PlayerItemDTO;
import org.testTask.ENUM.ROLE;
import org.testTask.steps.ValidateDataSteps;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testData.CreatePlayerData;
import testData.DataProviders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CreatePlayerTests extends BaseTest {


    @Description("Verify that player with correct role can be created with supervisor editor")
    @Test(dataProvider = "provideRolesForCRUD", dataProviderClass = DataProviders.class)
    public void testCreatePlayerAsSupervisor(ROLE role) {
        var playerParams = CreatePlayerData.getDefaultCreateParams(role);

        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, supervisorLogin);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_CREATED, "Expected Status code to be 201 for creation");
        soft = validateDataSteps.collectVerificationsForUnexpectedFields(CreatePlayerResponseDTO.class, createdPlayerResponse, soft);

        var createPlayerDTO = creationSteps.convertCreateResponseToDTO(createdPlayerResponse);
        soft = ValidateDataSteps.collectVerificationsForCreatedPlayerResponseFields(playerParams, createPlayerDTO, soft);

        var player = getByIdSteps.getPlayerById(createPlayerDTO.getId());
        soft.assertTrue(player != null);
        soft.assertAll();
    }

    @Description("Verify that player with correct role can be created with admin editor")
    @Test(dataProvider = "provideRolesForCRUD", dataProviderClass = DataProviders.class)
    public void testCreatePlayerAsAdmin(ROLE role) {
        var playerParams = CreatePlayerData.getDefaultCreateParams(role);

        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, adminLogin);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_CREATED, "Expected Status code to be 201 for creation");
        soft = validateDataSteps.collectVerificationsForUnexpectedFields(CreatePlayerResponseDTO.class, createdPlayerResponse, soft);
        var createPlayerDTO = creationSteps.convertCreateResponseToDTO(createdPlayerResponse);
        soft = ValidateDataSteps.collectVerificationsForCreatedPlayerResponseFields(playerParams, createPlayerDTO, soft);
        soft.assertAll();
    }

    @Description("Verify that player can't be created with user editor")
    @Test
    public void testCreateAsUser() {
        var playerParams = CreatePlayerData.getDefaultCreateParams(ROLE.USER);
        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, userLogin);
        assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expected Status code to be 403 for creation by user role");
    }

    @Description("Verify that supervisor can't be created")
    @Test
    public void testCreateSupervisor() {
        var playerParams = CreatePlayerData.getDefaultCreateParams(ROLE.SUPERVISOR);
        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, supervisorLogin);
        assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting Status code to be 403 for request to create supervisor");
    }

    @Description("Verify that player can't be created with incorrect role")
    @Test
    public void testCreateIncorrectRole() {
        var playerParams = CreatePlayerData.getDefaultCreateParams(ROLE.INCORRECT);
        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, supervisorLogin);
        assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting Status code to be 403 for request to create player with incorrect role");
    }

    @Description("Verify that id for created player is unique")
    @Test
    public void testCreatedIdIsUnique() {
        var playerParams = CreatePlayerData.getDefaultCreateParams(ROLE.USER);

        var createdPlayer = creationSteps.createPlayer(playerParams, supervisorLogin);
        createdPlayer.getId();
        var allPlayers = getAllSteps.getAllPlayers();
        List<Long> idsOfCreated = allPlayers.getPlayers().stream().map(PlayerItemDTO::getId).filter(x -> x.equals(createdPlayer.getId())).collect(Collectors.toList());
        assertTrue(idsOfCreated.size() == 1, "Expecting id of recently created user to be unique, but found " + idsOfCreated.size() + " duplicates");
    }

    @Description("Verify that login for created player is unique")
    @Test
    public void testCreatedLoginIsUnique() {
        var player1Params = CreatePlayerData.getDefaultCreateParams(ROLE.USER);
        var createdPlayer1 = creationSteps.createPlayer(player1Params,supervisorLogin);

        var player2Params = new HashMap<>(player1Params);
        player2Params.put("screenName", "createScreenNameUnique" + ThreadLocalRandom.current().nextInt(0, 1000));
        var createdPlayer2 = creationSteps.createPlayer(player2Params,supervisorLogin);
        assertEquals(createdPlayer1.getId(), createdPlayer2.getId(),"Second user with same login was created");
    }

    @Description("Verify that screenName for created player is unique")
    @Test
    public void testCreatedScreenNameIsUnique() {
        var player1Params = CreatePlayerData.getDefaultCreateParams(ROLE.USER);
        var createdPlayer1 = creationSteps.createPlayer(player1Params,supervisorLogin);

        var player2Params = new HashMap<>(player1Params);
        player2Params.put("login", "createLoginUnique" + ThreadLocalRandom.current().nextInt(0, 1000));
        var createdPlayer2 = creationSteps.createPlayer(player2Params,supervisorLogin);
        assertEquals(createdPlayer1.getId(), createdPlayer2.getId(),"Second user with same screenName was created");
    }

    @Description("Verify that player can't be created without required fields")
    @Test(dataProvider = "missingFieldCreateParams", dataProviderClass = DataProviders.class)
    public void testCreateAsSupervisorWithMissingField(Map<String, Object> params, String field) {
        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(params, supervisorLogin);
        assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_BAD_REQUEST, "Expecting status code to be 403 if create request is missing required field " + field);
    }

    @Description("Verify that passwords must match requirements")
    @Test(dataProvider = "provideIncorrectPasswords", dataProviderClass = DataProviders.class)
    public void testCreateWithIncorrectPassword( String password) {
        var playerParams = CreatePlayerData.getDefaultCreateParams(ROLE.USER);
        playerParams.put("password",password);
        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, supervisorLogin);
        assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Password that not matches requirement can be used. Password = " + password);
    }

    @Description("Verify that gender must be male or female only")
    @Test
    public void testCreateWithIncorrectGender( ) {
        var playerParams = CreatePlayerData.getDefaultCreateParams(ROLE.USER);
        playerParams.put("gender","walmart_bag1");
        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, supervisorLogin);
        assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Gender other than male or female can be used");
    }

    @Description("Verify that create is not updating player")
    @Test
    public void testCreateNotUpdates() {
        var playerParams = CreatePlayerData.getDefaultCreateParams(ROLE.USER);
        var createdPlayer1 = creationSteps.createPlayer(playerParams,supervisorLogin);

        playerParams.put("screenName", playerParams.get("screenName") + "!1");
        var createdPlayer2Response = creationSteps.getCreatePlayerResponse(playerParams, supervisorLogin);
        var createdPlayer2 = creationSteps.convertCreateResponseToDTO(createdPlayer2Response);
        var player2GetDto = getByIdSteps.getPlayerById(createdPlayer2.getId());
        var player1UpdatedGetDto = getByIdSteps.getPlayerById(createdPlayer1.getId());
        SoftAssert soft = new SoftAssert();
        soft.assertNotEquals(createdPlayer2Response.extract().statusCode(), HttpStatus.SC_OK, "Not expecting 200 status when trying to create player with same login again");
        soft.assertNotEquals(player1UpdatedGetDto.getScreenName(), player2GetDto.getScreenName(), "Create updated already existing player");
        soft.assertAll();
    }


}
