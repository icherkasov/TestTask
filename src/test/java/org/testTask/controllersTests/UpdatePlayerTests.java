package org.testTask.controllersTests;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.testTask.BaseTest;
import org.testTask.DTO.CreatePlayerRequestDTO;
import org.testTask.DTO.CreatePlayerResponseDTO;
import org.testTask.DTO.UpdatePlayerDTO;
import org.testTask.ENUM.EDITOR;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testData.CreatePlayerData;

import static org.testng.Assert.assertEquals;

public class UpdatePlayerTests extends BaseTest {
    private CreatePlayerRequestDTO playerForCreationDto;
    private CreatePlayerResponseDTO playerForTestsDto;

    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        playerForCreationDto = CreatePlayerData.getDefaultCreateDto("ForUpdate");
        var playerParams = CreatePlayerData.getDefaultCreateParams(playerForCreationDto);
        playerForTestsDto = creationSteps.createPlayer(playerParams);
    }

    @Description("Verify that /player/update/ with supervisor editor correctly updates player")
    @Test
    public void testUpdateUserWithSupervisor() {
        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerForCreationDto);
        updateRequestDto.setScreenName(playerForCreationDto.getScreenName() + "_1");
        // var getByIdOriginal = getByIdSteps.getPlayerById(playerForTestsDto.getId());

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(EDITOR.SUPERVISOR, playerForTestsDto.getId(), updateRequestDto);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_OK, "Expecting status code for update to be 200");
        validateDataSteps.collectVerificationsForUnexpectedFields(UpdatePlayerDTO.class, updateResponse, soft);
        var updateResponseDto = updatePlayerSteps.convertUpdateResponseToDto(updateResponse);
        soft = validateDataSteps.collectVerificationsForUpdateResponseFields(updateRequestDto, updateResponseDto, soft);

        var getByIdModified = getByIdSteps.getPlayerById(playerForTestsDto.getId());
        soft = validateDataSteps.collectVerificationsForUpdatedDataFromGetById(updateRequestDto, getByIdModified, soft);
        soft.assertAll();
    }

    @Description("Verify that /player/update/ with admin editor correctly updates player")
    @Test
    public void testUpdateUserWithAdmin() {
        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerForCreationDto);
        updateRequestDto.setScreenName(playerForCreationDto.getScreenName() + "_1");
        // var getByIdOriginal = getByIdSteps.getPlayerById(playerForTestsDto.getId());

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(EDITOR.ADMIN, playerForTestsDto.getId(), updateRequestDto);
        assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_OK, "Expecting status code for update to be 200");
        SoftAssert soft = new SoftAssert();
        validateDataSteps.collectVerificationsForUnexpectedFields(UpdatePlayerDTO.class, updateResponse, soft);
        var updateResponseDto = updatePlayerSteps.convertUpdateResponseToDto(updateResponse);
        soft = validateDataSteps.collectVerificationsForUpdateResponseFields(updateRequestDto, updateResponseDto, soft);

        var getByIdModified = getByIdSteps.getPlayerById(playerForTestsDto.getId());
        soft = validateDataSteps.collectVerificationsForUpdatedDataFromGetById(updateRequestDto, getByIdModified, soft);
        soft.assertAll();
    }

    @Description("Verify that /player/update/ can't change login to already existing")
    @Test
    public void testLoginDuplicateCantBeCreatedByUpdate() {
        var player2ForCreationDto = CreatePlayerData.getDefaultCreateDto("ForUpdate2");
        var player2Params = CreatePlayerData.getDefaultCreateParams(player2ForCreationDto);
        var player2ForTestsDto = creationSteps.createPlayer(player2Params);

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerForCreationDto);

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(EDITOR.SUPERVISOR, player2ForTestsDto.getId(), updateRequestDto);

        assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_CONFLICT, "Expecting status code for update to be 403 if updated user has already existing login");

    }
}
