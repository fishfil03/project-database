package com.fintech.creditprocessing.services;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.creditprocessing.entity.Tariff;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TariffService {
    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public static Tariff[] getAll(){
        Response response =
                given().contentType(ContentType.JSON)
                        .when()
                        .get("/loan-service/getTariffs")
                        .then()
                        .extract().response();


        var json = response.body().jsonPath().get("data.tariffs");
        return objectMapper.convertValue(json, Tariff[].class);
    }

    public static Tariff getFirst(){
       var tariffs = getAll();

       return tariffs[0];
    }
}
