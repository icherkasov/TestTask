package org.testTask.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testng.asserts.SoftAssert;

import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPlayerByIdResponseDTO {

    private Integer age;
    private String gender;
    private Long id;
    private String login;
    private String password;
    private String role;
    private String screenName;


    public static SoftAssert checkIfResponseHasCorrectValues(CreatePlayerResponseDTO source, GetPlayerByIdResponseDTO response, SoftAssert soft) {
        soft.assertEquals(source.getAge(), response.age, String.format("Expected age to be %s, but was %s", source.getAge(), response.getAge()));
        soft.assertEquals(source.getGender(), response.gender, String.format("Expected gender to be %s, but was %s", source.getGender(), response.getGender()));
        soft.assertTrue(Objects.equals(source.getId(), response.getId()), String.format("Expected id to be to be %s, but was %s", source.getId(), response.getId()));
        soft.assertEquals(source.getLogin(), response.login, String.format("Expected login to be %s, but was %s", source.getLogin(), response.getLogin()));
        soft.assertEquals(source.getPassword(), response.password, String.format("Expected password to be %s, but was %s", source.getPassword(), response.getPassword()));
        soft.assertEquals(source.getRole(), response.role, String.format("Expected role to be %s, but was %s", source.getRole(), response.getRole()));
        soft.assertEquals(source.getScreenName(), response.screenName, String.format("Expected screenName to be %s, but was %s", source.getScreenName(), response.getScreenName()));
        return soft;
    }

}


