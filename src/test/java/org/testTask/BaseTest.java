package org.testTask;

import io.restassured.RestAssured;
import org.testTask.ENUM.ROLE;
import org.testTask.config.AllureRestAssuredFilter;
import org.testTask.steps.*;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import testData.CreatePlayerData;

@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class BaseTest {
    protected PlayerCreationSteps creationSteps = new PlayerCreationSteps();
    protected GetAllPlayersSteps getAllSteps = new GetAllPlayersSteps();
    protected GetPlayerByIdSteps getByIdSteps = new GetPlayerByIdSteps();
    protected ValidateDataSteps validateDataSteps = new ValidateDataSteps();
    protected UpdatePlayerSteps updatePlayerSteps = new UpdatePlayerSteps();
    protected DeletePlayerSteps deletePlayerSteps = new DeletePlayerSteps();


    protected static String supervisorLogin = "supervisor";
    protected static String adminLogin;
    protected static String userLogin;

    @BeforeSuite(alwaysRun = true)
    public static void setupSuite() {
        RestAssured.baseURI = System.getProperty("uri");
        RestAssured.filters(new AllureRestAssuredFilter());

        //create basic admin player for login
        var playerAdminParams = CreatePlayerData.getDefaultCreateParams(ROLE.ADMIN);
        var createdPlayerAdminResponse = new PlayerCreationSteps().getCreatePlayerResponse(playerAdminParams, supervisorLogin);
        adminLogin = new PlayerCreationSteps().convertCreateResponseToDTO(createdPlayerAdminResponse).getLogin();

        //create basic user player for login
        var playerUserParams = CreatePlayerData.getDefaultCreateParams(ROLE.USER);
        var createdPlayerUserResponse = new PlayerCreationSteps().getCreatePlayerResponse(playerUserParams, supervisorLogin);
        userLogin = new PlayerCreationSteps().convertCreateResponseToDTO(createdPlayerUserResponse).getLogin();
    }
}
