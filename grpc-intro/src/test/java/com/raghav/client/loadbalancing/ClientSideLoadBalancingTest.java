package com.raghav.client.loadbalancing;

import com.raghav.client.rpctypes.BalanceStreamObserver;
import com.raghav.models.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientSideLoadBalancingTest {
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        List<String> instances = List.of("localhost:6565", "localhost:7575");
        ServiceRegistry.register("bank-service", instances);
        NameResolverRegistry.getDefaultRegistry().register(new TempNameResolverProvider());

        ManagedChannel managedChannel = ManagedChannelBuilder
                //.forAddress("localhost", 8585)
                .forTarget("http://bank-service")
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void balanceTest() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                                                                         .setAccountNumber(ThreadLocalRandom.current().nextInt(1, 11))
                                                                         .build();
            Balance balance = this.blockingStub.getBalance(balanceCheckRequest);
            System.out.println(
                    "Received : " + balance.getAmount()
            );
        }

    }

    @Test
    public void withdrawTest() {
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                                                         .setAccountNumber(8)
                                                         .setAmount(40)
                                                         .build();
        this.blockingStub.withdraw(withdrawRequest)
                         .forEachRemaining(money -> System.out.println("Received : " + money.getValue()));

    }


    @Test
    public void cashStreamingRequestTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> requestStreamObserver = this.bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));
        for (int i = 0; i < 10; i++) {
            DepositRequest depositRequest = DepositRequest.newBuilder()
                                                          .setAccountNumber(8)
                                                          .setAmount(10)
                                                          .build();
            requestStreamObserver.onNext(depositRequest);
        }
        requestStreamObserver.onCompleted();
        latch.await();
    }
}
