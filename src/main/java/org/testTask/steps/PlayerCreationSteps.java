package org.testTask.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.testTask.DTO.CreatePlayerResponseDTO;
import org.testTask.ENUM.ROLE;
import org.testTask.ENUM.ENDPOINT;
import org.testTask.engine.RAcalls;
import org.testng.internal.collections.Pair;

import java.util.Map;

public class PlayerCreationSteps {

    @Step("Create player as response")
    public ValidatableResponse getCreatePlayerResponse(Map<String, Object> playerParams, String login) {
        var response = RAcalls.sendGet(ENDPOINT.CREATE, new Pair<>("editor", login), playerParams);
        return response
                .then().log().all();
    }

    public CreatePlayerResponseDTO convertCreateResponseToDTO(ValidatableResponse response) {
        return response.extract().as(CreatePlayerResponseDTO.class);
    }

    @Step("Create player as DTO")
    public CreatePlayerResponseDTO createPlayer(Map<String, Object> playerParams, String login) {
        return convertCreateResponseToDTO(getCreatePlayerResponse(playerParams, login));
    }
}
