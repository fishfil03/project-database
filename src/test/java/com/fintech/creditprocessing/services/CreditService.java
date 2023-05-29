package com.fintech.creditprocessing.services;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.creditprocessing.constant.Status;
import com.fintech.creditprocessing.entity.Tariff;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@JsonRootName(value = "data")
public class CreditService {
    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public static long apply(long userId, Tariff tariff) {
        if (userId <= 0 || tariff == null) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("tariffId", tariff.getId());

        Response response =
                given().
                        accept(ContentType.JSON).
                        log().all().
                        contentType(ContentType.JSON).
                        body(map).
                        when().
                        post("loan-service/order");

        if (response.getStatusCode() == 400) {
            var error = HttpStatusCode.valueOf(400);
            var message = response.body().jsonPath().get("error.message").toString();

            throw new HttpClientErrorException(error, message);
        }

        return response.body().jsonPath().getLong("data.orderId");
    }

    public static Status getStatusOrder(String orderId) {
        Response response =
                given().contentType(ContentType.JSON)
                        .when()
                        .get("loan-service/getStatusOrder?orderId=" + orderId)
                        .then()
                        .extract().response();


        if (response.getStatusCode() == 400) {
            var error = HttpStatusCode.valueOf(400);
            var message = response.body().jsonPath().get("error.message").toString();

            throw new HttpClientErrorException(error, message);
        }

        var json = response.body().jsonPath().get("data.orderStatus");
        return objectMapper.convertValue(json, Status.class);
    }

    public static void delete(long userId, String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("orderId", orderId);

        Response response =
                given().
                        accept(ContentType.JSON).
                        log().all().
                        body(map).
                        when().
                        delete("loan-service/deleteOrder");

        if (response.getStatusCode() == 400) {
            var error = HttpStatusCode.valueOf(400);
            var message = response.body().jsonPath().get("error.message").toString();

            throw new HttpClientErrorException(error, message);
        }
    }
}
