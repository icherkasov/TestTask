package org.testTask.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerItemDTO {
    private Integer age;
    private String gender;
    private Long id;
    private String role;
    private String screenName;


    public static boolean hasFieldsWithoutValues(PlayerItemDTO data) {
        return data.age == null || data.gender == null || data.id == null || data.role == null || data.screenName == null;
    }
}
