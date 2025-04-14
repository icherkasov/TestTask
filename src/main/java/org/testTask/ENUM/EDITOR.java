package org.testTask.ENUM;

import lombok.Getter;

@Getter
public enum EDITOR {
    ADMIN("admin"),
    SUPERVISOR("supervisor");

    String editor;

    EDITOR(String role) {
        this.editor = role;
    }


}
