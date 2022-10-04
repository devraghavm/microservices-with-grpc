package com.raghav.protobuf;

import com.google.protobuf.Int32Value;
import com.raghav.models.Address;
import com.raghav.models.Car;
import com.raghav.models.Person;

import java.util.ArrayList;
import java.util.List;

public class CompositionDemo {
    public static void main(String[] args) {
        Address address = Address.newBuilder()
                                 .setPostbox(123)
                                 .setStreet("Main St")
                                 .setCity("Atlanta")
                                 .build();

        Car car1 = Car.newBuilder()
                      .setMake("Acura")
                      .setModel("MDX")
                      .setYear(2022)
                      .build();

        Car car2 = Car.newBuilder()
                      .setMake("Honda")
                      .setModel("Accord")
                      .setYear(2010)
                      .build();

        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);

        Person person = Person.newBuilder()
                              .setName("Raghav")
                              .setAge(Int32Value.newBuilder().setValue(37).build())
                              .setAddress(address)
                              .addAllCar(cars)
                              .build();

        System.out.println(
                person
        );
    }
}
