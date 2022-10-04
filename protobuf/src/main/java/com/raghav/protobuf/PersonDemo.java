package com.raghav.protobuf;

import com.raghav.models.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersonDemo {
    public static void main(String[] args) throws IOException {
/*        Person raghav = Person.newBuilder()
                .setName("Raghav")
                .setAge(37)
                .build();*/

        Path path = Paths.get("raghav.ser");
//        Files.write(path, raghav.toByteArray());

        byte[] bytes = Files.readAllBytes(path);
        Person newSam = Person.parseFrom(bytes);
        System.out.println(newSam);
    }
}
