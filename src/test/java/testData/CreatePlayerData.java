package testData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testTask.DTO.CreatePlayerRequestDTO;
import org.testng.internal.collections.Pair;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CreatePlayerData {

    public static Map getDefaultCreateParams() {
        return getDefaultCreateParams("");
    }

    public static CreatePlayerRequestDTO getDefaultCreateDto(String tag) {
        return CreatePlayerRequestDTO.builder()
                .age(ThreadLocalRandom.current().nextInt(21, 60))
                .role("user")
                .gender("male")
                .login("createLogin" + tag + ThreadLocalRandom.current().nextInt(0, 1000))
                .password("createPassword" + tag + ThreadLocalRandom.current().nextInt(0, 1000))
                .screenName("createScreenName" + tag + ThreadLocalRandom.current().nextInt(0, 1000))
                .build();
    }

    public static Map getDefaultCreateParams(String tag) {
        var dto = getDefaultCreateDto(tag);
        return getDefaultCreateParams(dto);
    }

    public static Map getDefaultCreateParams(CreatePlayerRequestDTO dto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.convertValue(dto, Map.class);
    }

    public static List<Pair<Map<String, Object>, String>> getMissingRequiredFieldsData() {

        Map defaultParams = getDefaultCreateParams();
        List<String> fields = Arrays.asList("age", "gender", "login", "role", "screenName");

        List<Pair<Map<String, Object>, String>> missingFieldParams = new ArrayList<>();
        for (String field : fields) {
            Map<String, Object> tempParam = new HashMap<>(defaultParams);
            tempParam.remove(field);
            missingFieldParams.add(new Pair<>(tempParam, field));
        }
        return missingFieldParams;
    }
}
