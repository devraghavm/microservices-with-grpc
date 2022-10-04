package com.grpcflix.user.service;


import com.grpcflix.user.repository.UserRepository;
import com.raghav.grpcflix.common.Genre;
import com.raghav.grpcflix.user.UserGenreUpdateRequest;
import com.raghav.grpcflix.user.UserSearchRequest;
import com.raghav.grpcflix.user.UserSearchResponse;
import com.raghav.grpcflix.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserSearchResponse> responseObserver) {
        UserSearchResponse.Builder userSearchBuilder = UserSearchResponse.newBuilder();
        this.userRepository.findById(request.getLoginId())
                           .ifPresent(user -> {
                               userSearchBuilder.setName(user.getName())
                                                .setLoginId(user.getLogin())
                                                .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                           });
        responseObserver.onNext(userSearchBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserSearchResponse> responseObserver) {
        UserSearchResponse.Builder userSearchBuilder = UserSearchResponse.newBuilder();
        this.userRepository.findById(request.getLoginId())
                           .ifPresent(user -> {
                               user.setGenre(request.getGenre().toString());
                               userSearchBuilder.setName(user.getName())
                                                .setLoginId(user.getLogin())
                                                .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                           });
        responseObserver.onNext(userSearchBuilder.build());
        responseObserver.onCompleted();
    }
}
