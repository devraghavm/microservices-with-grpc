package com.raghav.client.rpctypes;

import com.raghav.models.TransferRequest;
import com.raghav.models.TransferServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {
    private TransferServiceGrpc.TransferServiceStub transferServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                                                             .usePlaintext()
                                                             .build();
        this.transferServiceStub = TransferServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void transferTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        TransferStreamingResponse transferStreamingResponse = new TransferStreamingResponse(latch);
        StreamObserver<TransferRequest> transferRequestStreamObserver = this.transferServiceStub.transfer(transferStreamingResponse);
        for (int i = 0; i < 100; i++) {
            TransferRequest transferRequest = TransferRequest.newBuilder()
                                                             .setFromAccount(ThreadLocalRandom.current().nextInt(1, 11))
                                                             .setToAccount(ThreadLocalRandom.current().nextInt(1, 11))
                                                             .setAmount(ThreadLocalRandom.current().nextInt(1, 21))
                                                             .build();
            transferRequestStreamObserver.onNext(transferRequest);
        }
        transferRequestStreamObserver.onCompleted();
        latch.await();
    }
}
