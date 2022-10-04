package com.raghav.server.rpctypes;

import com.raghav.models.Account;
import com.raghav.models.TransferRequest;
import com.raghav.models.TransferResponse;
import com.raghav.models.TransferStatus;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {
    private StreamObserver<TransferResponse> responseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> responseStreamObserver) {
        this.responseStreamObserver = responseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int fromAccount = transferRequest.getFromAccount();
        int toAccount = transferRequest.getToAccount();
        int amount = transferRequest.getAmount();
        AccountDatabase accountDatabase = new AccountDatabase();
        int balance = accountDatabase.getBalance(fromAccount);
        TransferStatus status = TransferStatus.FAILED;

        if (balance >= amount && fromAccount != toAccount) {
            accountDatabase.deductBalance(fromAccount, amount);
            accountDatabase.addBalance(toAccount, amount);
            status = TransferStatus.SUCCESS;
        }
        Account fromAccountInfo = Account.newBuilder().setAccountNumber(fromAccount).setAmount(accountDatabase.getBalance(fromAccount)).build();
        Account toAccountInfo = Account.newBuilder().setAccountNumber(toAccount).setAmount(accountDatabase.getBalance(toAccount)).build();
        TransferResponse transferResponse = TransferResponse.newBuilder()
                                                            .setStatus(status)
                                                            .addAllAccounts(List.of(fromAccountInfo, toAccountInfo))
                                                            .build();
        this.responseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        new AccountDatabase().printAccountDetails();
        this.responseStreamObserver.onCompleted();
    }
}
