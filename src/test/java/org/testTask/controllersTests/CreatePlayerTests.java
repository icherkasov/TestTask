package org.testTask.controllersTests;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.testTask.BaseTest;
import org.testTask.DTO.CreatePlayerResponseDTO;
import org.testTask.DTO.PlayerItemDTO;
import org.testTask.ENUM.EDITOR;
import org.testTask.steps.ValidateDataSteps;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testData.CreatePlayerData;
import testData.DataProviders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CreatePlayerTests extends BaseTest {


    @Description("Verify that player can be correctly created with GET endpoint and supervisor editor")
    @Test
    public void testCreateWithGetAsSupervisor() {
        var playerParams = CreatePlayerData.getDefaultCreateParams();

        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, EDITOR.SUPERVISOR);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_CREATED, "Expected Status code to be 201 for creation");
        soft = validateDataSteps.collectVerificationsForUnexpectedFields(CreatePlayerResponseDTO.class, createdPlayerResponse, soft);

        var createPlayerDTO = creationSteps.convertCreateResponseToDTO(createdPlayerResponse);
        soft = ValidateDataSteps.collectVerificationsForCreatedPlayerResponseFields(playerParams, createPlayerDTO, soft);

        var player = getByIdSteps.getPlayerById(createPlayerDTO.getId());
        soft.assertTrue(player != null);
        soft.assertAll();
    }


    @Description("Verify that player can be correctly created with GET endpoint and admin editor")
    @Test
    public void testCreateWithGetAsAdmin() {
        var playerParams = CreatePlayerData.getDefaultCreateParams();

        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(playerParams, EDITOR.ADMIN);


        /* FIXME workaround for admin editor to fail test by assert.
        Should be removed if admin role gets power to create or replace with correct status code if works as expected
        and data verifications should be removed
         */
        assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_CREATED, "Expected Status code to be 201 for creation");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_CREATED, "Expected Status code to be 201 for creation");
        soft = validateDataSteps.collectVerificationsForUnexpectedFields(CreatePlayerResponseDTO.class, createdPlayerResponse, soft);
        var createPlayerDTO = creationSteps.convertCreateResponseToDTO(createdPlayerResponse);
        soft = ValidateDataSteps.collectVerificationsForCreatedPlayerResponseFields(playerParams, createPlayerDTO, soft);
        soft.assertAll();
    }

    @Description("Verify that id for created player is unique")
    @Test
    public void testCreatedIdIsUnique() {
        var playerParams = CreatePlayerData.getDefaultCreateParams();

        var createdPlayer = creationSteps.createPlayer(playerParams);
        createdPlayer.getId();
        var allPlayers = getAllSteps.getAllPlayers();
        List<Long> idsOfCreated = allPlayers.getPlayers().stream().map(PlayerItemDTO::getId).filter(x -> x.equals(createdPlayer.getId())).collect(Collectors.toList());
        assertTrue(idsOfCreated.size() == 1, "Expecting id of recently created user to be unique, but found " + idsOfCreated.size() + " duplicates");
    }

    @Description("Verify that player can't be created without required fields")
    @Test(dataProvider = "missingFieldCreateParams", dataProviderClass = DataProviders.class)
    public void testCreateWithGetAsSupervisorWithMissingField(Map<String, Object> params, String field) {

        var createdPlayerResponse = creationSteps.getCreatePlayerResponse(params, EDITOR.ADMIN);
        assertEquals(createdPlayerResponse.extract().statusCode(), HttpStatus.SC_BAD_REQUEST, "Expecting status code to be 403 if create request is missing required field " + field);
    }

    @Description("Verify that create is not updating player")
    @Test
    public void testCreateNotUpdates() {
        var playerParams = CreatePlayerData.getDefaultCreateParams();
        var createdPlayer1 = creationSteps.createPlayer(playerParams);
        var player1GetDto = getByIdSteps.getPlayerById(createdPlayer1.getId());
        playerParams.put("screenName", playerParams.get("screenName") + "!1");
        var createdPlayer2Response = creationSteps.getCreatePlayerResponse(playerParams, EDITOR.SUPERVISOR);
        var createdPlayer2 = creationSteps.convertCreateResponseToDTO(createdPlayer2Response);
        var player2GetDto = getByIdSteps.getPlayerById(createdPlayer2.getId());
        var player1UpdatedGetDto = getByIdSteps.getPlayerById(createdPlayer1.getId());
        SoftAssert soft = new SoftAssert();
        soft.assertNotEquals(createdPlayer2Response.extract().statusCode(), HttpStatus.SC_OK, "Not expecting 200 status when trying to create player with same login again");
        soft.assertNotEquals(createdPlayer1.getId(), createdPlayer2.getId(), "Second create request not created another user");
        soft.assertNotEquals(player1UpdatedGetDto.getScreenName(), player2GetDto.getScreenName(), "Create updated already existing player");
        soft.assertAll();
    }


}
