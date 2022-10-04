package com.raghav.client.deadline;

import com.raghav.client.rpctypes.MoneyStreamingResponse;
import com.raghav.models.Balance;
import com.raghav.models.BalanceCheckRequest;
import com.raghav.models.BankServiceGrpc;
import com.raghav.models.WithdrawRequest;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeadlineClientTest {
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                                                             .intercept(new DeadlineInterceptor())
                                                             .usePlaintext()
                                                             .build();
        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void balanceTest() throws InterruptedException {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                                                                     .setAccountNumber(2)
                                                                     .build();
        try {
            Balance balance = this.blockingStub
                    .getBalance(balanceCheckRequest);
            System.out.println(
                    "Received : " + balance.getAmount()
            );
        } catch (StatusRuntimeException e) {
            // go with default values
        }

    }

    @Test
    public void withdrawTest() {
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                                                         .setAccountNumber(7)
                                                         .setAmount(50)
                                                         .build();
        try {
            this.blockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .withdraw(withdrawRequest)
                    .forEachRemaining(money -> System.out.println("Received : " + money.getValue()));
        } catch (StatusRuntimeException e) {
            //
        }

    }

    @Test
    public void withdrawAsyncTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                                                         .setAccountNumber(10)
                                                         .setAmount(50)
                                                         .build();
        this.bankServiceStub.withdraw(withdrawRequest, new MoneyStreamingResponse(latch));
        latch.await();
    }
}
