package com.raghav.protobuf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Int32Value;
import com.google.protobuf.InvalidProtocolBufferException;
import com.raghav.json.JPerson;
import com.raghav.models.Person;

import java.io.IOException;

public class PerformanceTest {
    public static void main(String[] args) {
        //json
        JPerson jPerson = new JPerson();
        jPerson.setName("Raghav");
        jPerson.setAge(37);
        ObjectMapper mapper = new ObjectMapper();
        Runnable json = () -> {
            try {
                byte[] bytes = mapper.writeValueAsBytes(jPerson);
                System.out.println(bytes.length);
                JPerson jPerson1 = mapper.readValue(bytes, JPerson.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        //protobuf
        Person raghav = Person.newBuilder()
                              .setName("Raghav")
                              .setAge(Int32Value.newBuilder().setValue(37).build())
                              .build();
        Runnable proto = () -> {
            try {
                byte[] bytes = raghav.toByteArray();
                System.out.println(bytes.length);
                Person raghav1 = Person.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }

        };

        for (int i = 0; i < 1; i++) {
            runPerformanceTest(json, "JSON");
            runPerformanceTest(proto, "PROTOBUF");
        }
    }

    public static void runPerformanceTest(Runnable runnable, String method) {
        long time1 = System.currentTimeMillis();

        for (int i = 0; i < 1; i++) {
            runnable.run();
        }
        long time2 = System.currentTimeMillis();

        System.out.println(
                method + " : " + (time2 - time1) + " ms"

        );
    }
}
