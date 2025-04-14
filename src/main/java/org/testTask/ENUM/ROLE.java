package org.testTask.ENUM;

import lombok.Getter;

@Getter
public enum ROLE {
    ADMIN("admin"),
    SUPERVISOR("supervisor"),
    USER("user"),
    INCORRECT("incorrect_role");

    String role;

    ROLE(String role) {
        this.role = role;
    }


}
