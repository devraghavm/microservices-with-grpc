package com.raghav.server.loadbalancing;

import com.raghav.models.*;
import com.raghav.server.rpctypes.AccountDatabase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        int accountNumber = request.getAccountNumber();
/*        // print host name
        HostnamePrinter.print();*/
        System.out.println(
                "Received request for accountNumber : " + accountNumber
        );
        Balance balance = Balance.newBuilder()
                                 .setAmount(new AccountDatabase().getBalance(accountNumber))
                                 .build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();//10, 20, 30
/*        // print host name
        HostnamePrinter.print();*/
        int balance = new AccountDatabase().getBalance(accountNumber);

        if (balance < amount) {
            Status status = Status.FAILED_PRECONDITION.withDescription("No enough money. You have only " + balance);
            responseObserver.onError(status.asRuntimeException());
            return;
        }
        //All Validations Passed
        for (int i = 0; i < (amount / 10); i++) {
            Money money = Money.newBuilder().setValue(10).build();
            responseObserver.onNext(money);
            new AccountDatabase().deductBalance(accountNumber, 10);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }
}
