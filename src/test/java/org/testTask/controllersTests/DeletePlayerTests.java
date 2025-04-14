package org.testTask.controllersTests;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.testTask.BaseTest;
import org.testTask.ENUM.EDITOR;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testData.CreatePlayerData;

public class DeletePlayerTests extends BaseTest {

    @Description("Verify that created player can be correctly deleted")
    @Test
    public void testDeleteCreatedPlayer() {

        var playerParams = CreatePlayerData.getDefaultCreateParams("ForDelete");
        var player = creationSteps.createPlayer(playerParams);

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(EDITOR.SUPERVISOR, player.getId());

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_NO_CONTENT, "Expecting status code for delete to be 204");
    }

    @Description("Verify that deletion of player with non-existing id is rejected")
    @Test
    public void testDeleteNonExistingPlayer() {

        var deleteResponse = deletePlayerSteps.getDeletePlayerResponse(EDITOR.SUPERVISOR, Long.MAX_VALUE);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deleteResponse.extract().statusCode(), HttpStatus.SC_FORBIDDEN, "Expecting status code for delete non existing player to be 403");
    }
}
