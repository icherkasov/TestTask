package org.testTask.controllerTests;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.testTask.BaseTest;
import org.testTask.DTO.CreatePlayerRequestDTO;
import org.testTask.DTO.CreatePlayerResponseDTO;
import org.testTask.DTO.GetPlayerByIdResponseDTO;
import org.testTask.ENUM.ROLE;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testData.CreatePlayerData;

import static org.testng.Assert.assertEquals;

public class GetPlayerByIdTests extends BaseTest {
    private CreatePlayerResponseDTO playerForTestsDto;
    private CreatePlayerRequestDTO playerForCreationDto;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        playerForCreationDto = CreatePlayerData.getDefaultCreateDto("ForGetById", ROLE.USER);
        var playerParams = CreatePlayerData.getDefaultCreateParams(playerForCreationDto);
        playerForTestsDto = creationSteps.createPlayer(playerParams,supervisorLogin);
    }

    @Description("Verify that /player/get returns correct result")
    @Test
    public void testGetPlayerById() {
        var response = getByIdSteps.getPlayerByIdResponse(playerForTestsDto.getId());

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.extract().statusCode(), HttpStatus.SC_OK, "Expected status code to be 200 for get by id");
        soft = validateDataSteps.collectVerificationsForUnexpectedFields(GetPlayerByIdResponseDTO.class, response, soft);

        var getByIdResponseDto = getByIdSteps.convertGetByIdResponseToDTO(response);

        //FIXME workaround until creation response is not fixed. Using data from original creation DTO instead of create response DTO
        playerForTestsDto.setAge(playerForCreationDto.getAge());
        playerForTestsDto.setGender(playerForCreationDto.getGender());
        playerForTestsDto.setRole(playerForCreationDto.getRole());
        playerForTestsDto.setLogin(playerForCreationDto.getLogin());
        playerForTestsDto.setPassword(playerForCreationDto.getPassword());
        playerForTestsDto.setScreenName(playerForCreationDto.getScreenName());
        soft = validateDataSteps.collectVerificationsForGetByIdResponseFields(playerForTestsDto, getByIdResponseDto, soft);
        soft.assertAll();

    }

    @Description("Verify that /player/get with non-existing id is rejected")
    @Test
    public void testGetNonExistingPlayer() {

        var response = getByIdSteps.getPlayerByIdResponse(Long.MAX_VALUE);
        assertEquals(response.extract().statusCode(), HttpStatus.SC_NOT_FOUND, "Expected status code to be 404 forget by non existing id");

    }

    @Description("Verify that /player/get with incorrect payload is rejected")
    @Test
    public void testGetPlayerBadRequest() {
        var response = getByIdSteps.getPlayerByIdBadRequestResponse();
        assertEquals(response.extract().statusCode(), HttpStatus.SC_BAD_REQUEST, "Expecting status code to be 400 for incorrect parameter type");
    }
}
