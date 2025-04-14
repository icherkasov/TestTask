package org.testTask.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.testTask.DTO.GetPlayerByIDBadRequestDTO;
import org.testTask.DTO.GetPlayerByIdResponseDTO;
import org.testTask.DTO.GetPlayerRequestDTO;
import org.testTask.ENUM.ENDPOINT;
import org.testTask.engine.RAcalls;

public class GetPlayerByIdSteps {

    @Step("Get player by ID as response")
    public ValidatableResponse getPlayerByIdResponse(Long id) {
        GetPlayerRequestDTO requestDTO = GetPlayerRequestDTO.builder().playerId(id).build();
        var response = RAcalls.sendPost(ENDPOINT.GET_BY_ID, requestDTO);
        return response
                .then().log().all();
    }

    @Step("Get player by wrong type ID as response")
    public ValidatableResponse getPlayerByIdBadRequestResponse() {
        var badIdRequestDto = GetPlayerByIDBadRequestDTO.builder().playerId("randomName").build();
        var response = RAcalls.sendPost(ENDPOINT.GET_BY_ID, badIdRequestDto);
        return response
                .then().log().all();
    }

    @Step("Get player by ID as DTO")
    public GetPlayerByIdResponseDTO getPlayerById(String id) {
        return getPlayerByIdResponse(Long.valueOf(id)).extract().as(GetPlayerByIdResponseDTO.class);
    }


    public GetPlayerByIdResponseDTO getPlayerById(Long id) {
        return getPlayerById(String.valueOf(id));
    }

    public GetPlayerByIdResponseDTO convertGetByIdResponseToDTO(ValidatableResponse response) {
        return response.extract().as(GetPlayerByIdResponseDTO.class);
    }


}
