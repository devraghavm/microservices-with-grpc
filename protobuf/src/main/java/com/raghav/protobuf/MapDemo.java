package com.raghav.protobuf;

import com.raghav.models.BodyStyle;
import com.raghav.models.Car;
import com.raghav.models.Dealer;

public class MapDemo {
    public static void main(String[] args) {
        Car car1 = Car.newBuilder()
                      .setMake("Acura")
                      .setModel("MDX")
                      .setYear(2022)
                      .setBodyStyle(BodyStyle.SUV)
                      .build();

        Car car2 = Car.newBuilder()
                      .setMake("Honda")
                      .setModel("Accord")
                      .setYear(2010)
                      .setBodyStyle(BodyStyle.SEDAN)
                      .build();

        Dealer dealer = Dealer.newBuilder()
                              .putModel(2022, car1)
                              .putModel(2010, car2)
                              .build();

/*        System.out.println(
                dealer
        );

        System.out.println(
                dealer.getModelOrThrow(2022)
        );

        System.out.println(
                dealer.getModelOrDefault(2021, car2)
        );*/
        System.out.println(
                dealer.getModelOrThrow(2010).getBodyStyle()

        );


    }
}
