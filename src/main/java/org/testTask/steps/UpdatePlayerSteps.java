package org.testTask.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.testTask.DTO.UpdatePlayerDTO;
import org.testTask.ENUM.ROLE;
import org.testTask.ENUM.ENDPOINT;
import org.testTask.engine.RAcalls;
import org.testng.internal.collections.Pair;

public class UpdatePlayerSteps {

    @Step("Update player get as response")
    public ValidatableResponse getUpdatePlayerResponse(String login, Long id, UpdatePlayerDTO body) {
        var response = RAcalls.sendPatch(ENDPOINT.UPDATE, new Pair<>("editor", login), new Pair<>("id", id), body);
        return response
                .then().log().all();
    }

    public UpdatePlayerDTO convertUpdateResponseToDto(ValidatableResponse response) {
        return response.extract().as(UpdatePlayerDTO.class);
    }


}
