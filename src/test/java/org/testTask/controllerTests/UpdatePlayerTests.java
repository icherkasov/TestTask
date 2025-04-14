package org.testTask.controllerTests;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.testTask.BaseTest;
import org.testTask.DTO.UpdatePlayerDTO;
import org.testTask.ENUM.ROLE;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testData.CreatePlayerData;
import testData.DataProviders;

import static org.testng.Assert.assertEquals;

public class UpdatePlayerTests extends BaseTest {


    @Description("Verify supervisor can update supervisor")
    @Test
    public void testUpdateSuperVisorWithSupervisor() {
        var supervisor = getByIdSteps.getPlayerById(String.valueOf(1));
        var originalScreenName = supervisor.getScreenName();
        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.builder().screenName(supervisor.getScreenName() + "1").build();

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(supervisorLogin, supervisor.getId(), updateRequestDto);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_OK, "Expecting status code for update to be 200");
        var getByIdModified = getByIdSteps.getPlayerById(supervisor.getId());
        soft.assertEquals(originalScreenName + "1", getByIdModified.getScreenName(), "ScreenName wasn't updated");
        soft.assertAll();
    }

    @Description("Verify that supervisor editor correctly updates players")
    @Test(dataProvider = "provideRolesForCRUD", dataProviderClass = DataProviders.class)
    public void testUpdateUserWithSupervisor(ROLE role) {
        var playerDto = CreatePlayerData.getDefaultCreateDto("ForUpdate", role);
        var playerParams = CreatePlayerData.getDefaultCreateParams(playerDto);
        var createdPlayer = creationSteps.createPlayer(playerParams, supervisorLogin);

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerDto);
        updateRequestDto.setScreenName(playerDto.getScreenName() + "01");

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(supervisorLogin, createdPlayer.getId(), updateRequestDto);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_OK, "Expecting status code for update to be 200");
        validateDataSteps.collectVerificationsForUnexpectedFields(UpdatePlayerDTO.class, updateResponse, soft);
        var updateResponseDto = updatePlayerSteps.convertUpdateResponseToDto(updateResponse);
        soft = validateDataSteps.collectVerificationsForUpdateResponseFields(updateRequestDto, updateResponseDto, soft);

        var getByIdModified = getByIdSteps.getPlayerById(createdPlayer.getId());
        soft = validateDataSteps.collectVerificationsForUpdatedDataFromGetById(updateRequestDto, getByIdModified, soft);
        soft.assertAll();
    }

    @Description("Verify that admin editor correctly updates admin and user players")
    @Test(dataProvider = "provideRolesForCRUD", dataProviderClass = DataProviders.class)
    public void testUpdateUserWithAdmin(ROLE role) {
        var adminplayerDto = CreatePlayerData.getDefaultCreateDto("ForUpdate", ROLE.ADMIN);
        var adminPlayerParams = CreatePlayerData.getDefaultCreateParams(adminplayerDto);
        var createdAdminPlayer = creationSteps.createPlayer(adminPlayerParams, supervisorLogin);
        var localAdminLogin = createdAdminPlayer.getLogin();

        var playerDto = CreatePlayerData.getDefaultCreateDto("ForUpdate", role);
        var playerParams = CreatePlayerData.getDefaultCreateParams(playerDto);
        var createdPlayer = creationSteps.createPlayer(playerParams, supervisorLogin);

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerDto);
        updateRequestDto.setScreenName(playerDto.getScreenName() + "01");

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(localAdminLogin, createdPlayer.getId(), updateRequestDto);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_OK, "Expecting status code for update to be 200");
        validateDataSteps.collectVerificationsForUnexpectedFields(UpdatePlayerDTO.class, updateResponse, soft);
        var updateResponseDto = updatePlayerSteps.convertUpdateResponseToDto(updateResponse);
        soft = validateDataSteps.collectVerificationsForUpdateResponseFields(updateRequestDto, updateResponseDto, soft);

        var getByIdModified = getByIdSteps.getPlayerById(createdPlayer.getId());
        soft = validateDataSteps.collectVerificationsForUpdatedDataFromGetById(updateRequestDto, getByIdModified, soft);
        soft.assertAll();
    }

    @Description("Verify that admin can update himself")
    @Test
    public void testUpdateAdminHimself() {
        var playerDto = CreatePlayerData.getDefaultCreateDto("ForUpdate", ROLE.ADMIN);
        var playerParams = CreatePlayerData.getDefaultCreateParams(playerDto);
        var createdPlayer = creationSteps.createPlayer(playerParams, supervisorLogin);

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerDto);
        updateRequestDto.setScreenName(playerDto.getScreenName() + "_1");

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(createdPlayer.getLogin(), createdPlayer.getId(), updateRequestDto);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_OK, "Expecting status code for update to be 200");
        validateDataSteps.collectVerificationsForUnexpectedFields(UpdatePlayerDTO.class, updateResponse, soft);
        var updateResponseDto = updatePlayerSteps.convertUpdateResponseToDto(updateResponse);
        soft = validateDataSteps.collectVerificationsForUpdateResponseFields(updateRequestDto, updateResponseDto, soft);

        var getByIdModified = getByIdSteps.getPlayerById(createdPlayer.getId());
        soft = validateDataSteps.collectVerificationsForUpdatedDataFromGetById(updateRequestDto, getByIdModified, soft);
        soft.assertAll();
    }

    @Description("Verify that user can update himself")
    @Test
    public void testUpdateUserHimself() {
        var playerDto = CreatePlayerData.getDefaultCreateDto("ForUpdate", ROLE.USER);
        var playerParams = CreatePlayerData.getDefaultCreateParams(playerDto);
        var createdPlayer = creationSteps.createPlayer(playerParams, supervisorLogin);

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerDto);
        updateRequestDto.setScreenName(playerDto.getScreenName() + "_1");

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(createdPlayer.getLogin(), createdPlayer.getId(), updateRequestDto);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_OK, "Expecting status code for update to be 200");
        validateDataSteps.collectVerificationsForUnexpectedFields(UpdatePlayerDTO.class, updateResponse, soft);
        var updateResponseDto = updatePlayerSteps.convertUpdateResponseToDto(updateResponse);
        soft = validateDataSteps.collectVerificationsForUpdateResponseFields(updateRequestDto, updateResponseDto, soft);

        var getByIdModified = getByIdSteps.getPlayerById(createdPlayer.getId());
        soft = validateDataSteps.collectVerificationsForUpdatedDataFromGetById(updateRequestDto, getByIdModified, soft);
        soft.assertAll();
    }

    @Description("Verify that user can't update other user")
    @Test
    public void testUpdateUserOtherUser() {
        var playerDto = CreatePlayerData.getDefaultCreateDto("ForUpdate", ROLE.USER);
        var playerParams = CreatePlayerData.getDefaultCreateParams(playerDto);
        var createdPlayer = creationSteps.createPlayer(playerParams, supervisorLogin);

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerDto);
        updateRequestDto.setScreenName(playerDto.getScreenName() + "_1");

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(userLogin, createdPlayer.getId(), updateRequestDto);

        assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting status code 403 for user updating other user");
    }

    @Description("Verify that user can't update admin")
    @Test
    public void testUpdateUserAdmin() {
        var playerDto = CreatePlayerData.getDefaultCreateDto("ForUpdate", ROLE.ADMIN);
        var playerParams = CreatePlayerData.getDefaultCreateParams(playerDto);
        var createdPlayer = creationSteps.createPlayer(playerParams, supervisorLogin);

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.copyFromCreateDto(playerDto);
        updateRequestDto.setScreenName(playerDto.getScreenName() + "_1");

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(userLogin, createdPlayer.getId(), updateRequestDto);

        assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting status code 403 for user updating admin");
    }

    @Description("Verify that user can't update supervisor")
    @Test
    public void testUpdateUserSupervisor() {
        var supervisor = getByIdSteps.getPlayerById(String.valueOf(1));
        var originalScreenName = supervisor.getScreenName();

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.builder().screenName(originalScreenName + "1").build();

        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(userLogin, supervisor.getId(), updateRequestDto);

        assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting status code 403 for user updating supervisor");
        var getByIdModified = getByIdSteps.getPlayerById(supervisor.getId());
        assertEquals(originalScreenName, getByIdModified.getScreenName(), "User updated supervisor");
    }


    @Description("Verify that login can't be changed to already existing with update")
    @Test
    public void testLoginDuplicateCantBeCreatedByUpdate() {
        var player1ForCreationDto = CreatePlayerData.getDefaultCreateDto("ForUpdate1", ROLE.USER);
        var player1Params = CreatePlayerData.getDefaultCreateParams(player1ForCreationDto);
        var player1ForTestsDto = creationSteps.createPlayer(player1Params, supervisorLogin);

        var player2ForCreationDto = CreatePlayerData.getDefaultCreateDto("ForUpdate2", ROLE.USER);
        var player2Params = CreatePlayerData.getDefaultCreateParams(player2ForCreationDto);
        var player2ForTestsDto = creationSteps.createPlayer(player2Params, supervisorLogin);

        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.builder().login(player1ForTestsDto.getLogin()).build();
        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(supervisorLogin, player2ForTestsDto.getId(), updateRequestDto);

        assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_CONFLICT, "Expecting status code for update to be 403 if updated user has already existing login");
    }

    @Description("Verify that screenName can't be changed to already existing with update")
    @Test
    public void testScreenNameDuplicateCantBeCreatedByUpdate() {
        var player1ForCreationDto = CreatePlayerData.getDefaultCreateDto("ForUpdate1", ROLE.USER);
        var player1Params = CreatePlayerData.getDefaultCreateParams(player1ForCreationDto);
        var player1ForTestsDto = creationSteps.createPlayer(player1Params, supervisorLogin);

        var player2ForCreationDto = CreatePlayerData.getDefaultCreateDto("ForUpdate2", ROLE.USER);
        var player2Params = CreatePlayerData.getDefaultCreateParams(player2ForCreationDto);
        var player2ForTestsDto = creationSteps.createPlayer(player2Params, supervisorLogin);


        UpdatePlayerDTO updateRequestDto = UpdatePlayerDTO.builder().screenName(player1ForTestsDto.getScreenName()).build();
        var updateResponse = updatePlayerSteps.getUpdatePlayerResponse(supervisorLogin, player2ForTestsDto.getId(), updateRequestDto);

        assertEquals(updateResponse.extract().statusCode(), HttpStatus.SC_CONFLICT, "Expecting status code for update to be 403 if updated user has already existing login");
    }
}
