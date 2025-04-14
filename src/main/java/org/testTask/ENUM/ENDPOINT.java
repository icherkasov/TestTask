package org.testTask.ENUM;

import lombok.Getter;

@Getter
public enum ENDPOINT {
    CREATE("/player/create/{editor}"),
    GET_ALL("/player/get/all"),
    GET_BY_ID("/player/get"),
    UPDATE("/player/update/{editor}/{id}"),
    DELETE("/player/delete/{editor}");

    private String endpoint;

    ENDPOINT(String endpoint) {
        this.endpoint = endpoint;
    }
}
