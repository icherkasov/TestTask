package org.testTask.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.testTask.DTO.*;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


public class ValidateDataSteps {
    @Step("Validate /get/all response data")
    public static SoftAssert collectVerificationsForGetAllResponseFields(AllPlayersDTO data, SoftAssert soft) {
        Assert.assertFalse(data.getPlayers().isEmpty(), "No players were found");
        var players = data.getPlayers();

        List<PlayerItemDTO> incorrectPlayersResponses = players.stream().filter(PlayerItemDTO::hasFieldsWithoutValues).collect(Collectors.toList());
        soft.assertTrue(incorrectPlayersResponses.isEmpty(), incorrectPlayersResponses.size() + " players have empty field(s) " + incorrectPlayersResponses);
        return soft;
    }

    @Step("Validate create player response data")
    public static SoftAssert collectVerificationsForCreatedPlayerResponseFields(Map<String, Object> source, CreatePlayerResponseDTO response, SoftAssert soft) {
        return CreatePlayerResponseDTO.checkIfResponseHasCorrectValues(source, response, soft);
    }

    @Step("Validate /player/get response data")
    public static SoftAssert collectVerificationsForGetByIdResponseFields(CreatePlayerResponseDTO source, GetPlayerByIdResponseDTO response, SoftAssert soft) {
        return GetPlayerByIdResponseDTO.checkIfResponseHasCorrectValues(source, response, soft);
    }

    @Step("Validate update player response data")
    public static SoftAssert collectVerificationsForUpdateResponseFields(UpdatePlayerDTO source, UpdatePlayerDTO response, SoftAssert soft) {
        return UpdatePlayerDTO.validateResponseMatchesRequest(source, response, soft);
    }

    @Step("Validate updated values correctly shown in /player/get")
    public static SoftAssert collectVerificationsForUpdatedDataFromGetById(UpdatePlayerDTO updateRequestDto, GetPlayerByIdResponseDTO getByIdModified, SoftAssert soft) {
        return UpdatePlayerDTO.validateUpdateMatchesGetByIdRequest(updateRequestDto, getByIdModified, soft);
    }

    @Step("Validate if response data has unexpected fields")
    public static SoftAssert collectVerificationsForUnexpectedFields(Class expected, ValidatableResponse response, SoftAssert soft) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseNode;
        try {
            responseNode = mapper.readTree(response.extract().body().asString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Set<String> classFields = Arrays.stream(expected.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
        Set<String> jsonFields = new HashSet<>();
        responseNode.fieldNames().forEachRemaining(jsonFields::add);
        Set<String> unexpectedFields = new HashSet<>(jsonFields);
        unexpectedFields.removeAll(classFields);
        soft.assertTrue(unexpectedFields.isEmpty(), "Response contains unexpected field(s) " + unexpectedFields.toString());
        return soft;
    }
}
