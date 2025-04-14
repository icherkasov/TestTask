package org.testTask.controllerTests;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.testTask.BaseTest;
import org.testTask.DTO.AllPlayersDTO;
import org.testTask.DTO.PlayerItemDTO;
import org.testTask.steps.ValidateDataSteps;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashSet;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public class GetAllPlayersTests extends BaseTest {

    @Description("Verify that get all players endpoint returns correct values")
    @Test
    public void testGetAllPLayers() {
        var getAllResponse = getAllSteps.getAllPlayersResponse();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(getAllResponse.extract().statusCode(), HttpStatus.SC_OK, "Expected status code to be 200 for get all players");
        soft = validateDataSteps.collectVerificationsForUnexpectedFields(AllPlayersDTO.class, getAllResponse, soft);

        var getAllDTO = getAllSteps.convertGetAllResponseToDTO(getAllResponse);

        soft = ValidateDataSteps.collectVerificationsForGetAllResponseFields(getAllDTO, soft);
        soft.assertAll();
    }

    @Description("Verify that there are no id duplicates in get all players response")
    @Test
    public void testIdsHaveNoDuplicates() {
        var allPlayers = getAllSteps.getAllPlayers();
        var ids = allPlayers.getPlayers().stream().map(PlayerItemDTO::getId).collect(Collectors.toList());
        var uniqueIds = new HashSet<>(ids);
        assertEquals(ids.size(), uniqueIds.size(), "Expecting ids to be unique. Found duplicates " + ids.removeAll(uniqueIds));

    }
}
