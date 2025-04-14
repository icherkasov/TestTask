package org.testTask.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testng.asserts.SoftAssert;

import java.util.Map;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePlayerResponseDTO {

    private Integer age;
    private String gender;
    public Long id;
    private String login;
    private String password;
    private String role;
    private String screenName;

    public static SoftAssert checkIfResponseHasCorrectValues(Map<String, Object> source, CreatePlayerResponseDTO response, SoftAssert soft) {
        soft.assertEquals(source.get("age"), response.getAge(), String.format("Expected age to be %s, but was %s", source.get("age"), response.age));
        soft.assertEquals(source.get("gender"), response.getGender(), String.format("Expected gender to be %s, but was %s", source.get("gender"), response.gender));
        soft.assertTrue(response.getId() != null, "Expected id to be not null");
        soft.assertEquals(source.get("login"), response.getLogin(), String.format("Expected login to be %s, but was %s", source.get("login"), response.login));
        soft.assertEquals(source.get("password"), response.getPassword(), String.format("Expected password to be %s, but was %s", source.get("password"), response.password));
        soft.assertEquals(source.get("role"), response.getRole(), String.format("Expected role to be %s, but was %s", source.get("role"), response.role));
        soft.assertEquals(source.get("screenName"), response.getScreenName(), String.format("Expected screenName to be %s, but was %s", source.get("screenName"), response.screenName));
        return soft;
    }
}
