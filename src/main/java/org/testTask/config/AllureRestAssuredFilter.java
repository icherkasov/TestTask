package org.testTask.config;

import io.qameta.allure.Attachment;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class AllureRestAssuredFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        String requestLog = requestSpec.getMethod() + " " + requestSpec.getURI() + "\n"
                + "Headers: " + requestSpec.getHeaders() + "\n"
                + "Body: " + requestSpec.getBody();

        attachRequest(requestLog);

        Response response = ctx.next(requestSpec, responseSpec);

        String responseLog = "Status Code: " + response.getStatusCode() + "\n"
                + "Headers: " + response.getHeaders() + "\n"
                + "Body: " + response.getBody().asPrettyString();

        attachResponse(responseLog);

        return response;
    }

    @Attachment(value = "Request", type = "text/plain")
    private String attachRequest(String request) {
        return request;
    }

    @Attachment(value = "Response", type = "text/plain")
    private String attachResponse(String response) {
        return response;
    }
}
