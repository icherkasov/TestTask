package org.testTask.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.testTask.DTO.AllPlayersDTO;
import org.testTask.ENUM.ENDPOINT;
import org.testTask.engine.RAcalls;

public class GetAllPlayersSteps {
    @Step("Get all players as response")
    public ValidatableResponse getAllPlayersResponse() {
        var response = RAcalls.sendGet(ENDPOINT.GET_ALL);
        return response
                .then().log().all();
    }

    public AllPlayersDTO convertGetAllResponseToDTO(ValidatableResponse response) {
        return response.extract().as(AllPlayersDTO.class);
    }

    @Step("Get all players as DTO")
    public AllPlayersDTO getAllPlayers() {
        return convertGetAllResponseToDTO(getAllPlayersResponse());
    }
}
