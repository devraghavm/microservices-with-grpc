package com.raghav.client.loadbalancing;

import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import java.net.URI;

public class TempNameResolverProvider extends NameResolverProvider {
    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 5;
    }

    @Override
    public NameResolver newNameResolver(URI uri, NameResolver.Args args) {
        System.out.println(
                "Looking for service : " + uri.getAuthority()
        );
        return new TempNameResolver(uri.getAuthority());
    }

    @Override
    public String getDefaultScheme() {
        return "http";
    }
}
