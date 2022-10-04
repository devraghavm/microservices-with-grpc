package com.raghav.client.loadbalancing;

import io.grpc.EquivalentAddressGroup;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceRegistry {
    private final static Map<String, List<EquivalentAddressGroup>> MAP = new HashMap<>();

    //bank-service 127.0.0.1:6565,127.0.0.1:7575
    public static void register(String service, List<String> instances) {
        List<EquivalentAddressGroup> addressGroups = instances.stream()
                                                              .map(i -> i.split(":"))
                                                              .map(a -> new InetSocketAddress(a[0], Integer.parseInt(a[1])))
                                                              .map(EquivalentAddressGroup::new)
                                                              .collect(Collectors.toList());
        MAP.put(service, addressGroups);
    }

    public static List<EquivalentAddressGroup> getInstances(String service) {
        return MAP.get(service);
    }
}
