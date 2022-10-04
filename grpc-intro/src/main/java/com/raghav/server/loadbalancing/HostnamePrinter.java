package com.raghav.server.loadbalancing;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostnamePrinter {
    public static void print() {
        try {
            System.out.println("Processed by host :: " + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
