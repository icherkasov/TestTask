package org.testTask.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.testTask.DTO.DeletePlayerByIdRequestDTO;
import org.testTask.ENUM.ROLE;
import org.testTask.ENUM.ENDPOINT;
import org.testTask.engine.RAcalls;
import org.testng.internal.collections.Pair;

public class DeletePlayerSteps {
    @Step("Delete player and get response")
    public ValidatableResponse getDeletePlayerResponse(String login, long id) {
        var deleteRequest = DeletePlayerByIdRequestDTO.builder().playerId(id).build();

        var response = RAcalls.sendDelete(ENDPOINT.DELETE, new Pair<>("editor", login), deleteRequest);
        return response
                .then().log().all();
    }
}
