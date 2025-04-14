package org.testTask.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testng.asserts.SoftAssert;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePlayerDTO {

    private Integer age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;


    public static UpdatePlayerDTO copyFromCreateDto(CreatePlayerRequestDTO origin) {
        return UpdatePlayerDTO.builder()
                .age(origin.getAge())
                .gender(origin.getGender())
                .login(origin.getLogin())
                .password(origin.getPassword())
                .role(origin.getRole())
                .screenName(origin.getScreenName())
                .build();
    }

    public static SoftAssert validateResponseMatchesRequest(UpdatePlayerDTO source, UpdatePlayerDTO response, SoftAssert soft) {
        soft.assertEquals(source.getAge(),response.age, String.format("Expected age in update response to be %s, but was %s", source.getAge(), response.getAge()));
        soft.assertEquals(source.getGender(),response.gender, String.format("Expected gender in update response to be %s, but was %s", source.getGender(), response.getGender()));
        soft.assertEquals(source.getLogin(),response.login, String.format("Expected login in update response to be %s, but was %s", source.getLogin(), response.getLogin()));
        soft.assertEquals(source.getPassword(),response.password, String.format("Expected password in update response to be %s, but was %s", source.getPassword(), response.getPassword()));
        soft.assertEquals(source.getRole(),response.role, String.format("Expected role in update response to be %s, but was %s", source.getRole(), response.getRole()));
        soft.assertEquals(source.getScreenName(),response.screenName, String.format("Expected screenName in update response to be %s, but was %s", source.getScreenName(), response.getScreenName()));
        return soft;
    }

    public static SoftAssert validateUpdateMatchesGetByIdRequest(UpdatePlayerDTO expected, GetPlayerByIdResponseDTO actual, SoftAssert soft) {
        soft.assertEquals(expected.getAge(),actual.getAge(), String.format("Expected age after update to be %s, but was %s", expected.getAge(), actual.getAge()));
        soft.assertEquals(expected.getGender(),actual.getGender(), String.format("Expected gender after update to be %s, but was %s", expected.getGender(), actual.getGender()));
        soft.assertEquals(expected.getLogin(),actual.getLogin(), String.format("Expected login after update to be %s, but was %s", expected.getLogin(), actual.getLogin()));
        soft.assertEquals(expected.getPassword(),actual.getPassword(), String.format("Expected password after update to be %s, but was %s", expected.getPassword(), actual.getPassword()));
        soft.assertEquals(expected.getRole(),actual.getRole(), String.format("Expected role after update to be %s, but was %s", expected.getRole(), actual.getRole()));
        soft.assertEquals(expected.getScreenName(),actual.getScreenName(), String.format("Expected screenName after update to be %s, but was %s", expected.getScreenName(), actual.getScreenName()));
        return soft;
    }
}
