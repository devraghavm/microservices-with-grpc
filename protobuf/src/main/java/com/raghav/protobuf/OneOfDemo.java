package com.raghav.protobuf;

import com.raghav.models.Credentials;
import com.raghav.models.EmailCredentials;
import com.raghav.models.PhoneOTP;

public class OneOfDemo {
    public static void main(String[] args) {
        EmailCredentials emailCredentials = EmailCredentials.newBuilder()
                                                            .setEmail("raghav@myself.com")
                                                            .setPassword("Nikitha")
                                                            .build();

        PhoneOTP phoneOTP = PhoneOTP.newBuilder()
                                    .setNumber(1231231234)
                                    .setCode(3456)
                                    .build();
        Credentials credentials = Credentials.newBuilder()
                                             .setEmailMode(emailCredentials)
                                             .setPhoneMode(phoneOTP)
                                             .build();

        login(credentials);

    }

    private static void login(Credentials credentials) {
        switch (credentials.getModeCase()) {
            case EMAILMODE -> System.out.println(credentials.getEmailMode());
            case PHONEMODE -> System.out.println(credentials.getPhoneMode());
            case MODE_NOT_SET -> System.out.println("Mode is not set");
        }

    }
}
