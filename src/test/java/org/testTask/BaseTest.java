package org.testTask;

import io.restassured.RestAssured;
import org.testTask.config.AllureRestAssuredFilter;
import org.testTask.steps.*;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class BaseTest {
    protected PlayerCreationSteps creationSteps = new PlayerCreationSteps();
    protected GetAllPlayersSteps getAllSteps = new GetAllPlayersSteps();
    protected GetPlayerByIdSteps getByIdSteps = new GetPlayerByIdSteps();
    protected ValidateDataSteps validateDataSteps = new ValidateDataSteps();
    protected UpdatePlayerSteps updatePlayerSteps = new UpdatePlayerSteps();
    protected DeletePlayerSteps deletePlayerSteps = new DeletePlayerSteps();

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        RestAssured.baseURI = System.getProperty("uri");
        RestAssured.filters(new AllureRestAssuredFilter());
    }
}
