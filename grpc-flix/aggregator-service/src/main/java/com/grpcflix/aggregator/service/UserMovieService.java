package com.grpcflix.aggregator.service;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.raghav.grpcflix.common.Genre;
import com.raghav.grpcflix.movie.MovieSearchRequest;
import com.raghav.grpcflix.movie.MovieSearchResponse;
import com.raghav.grpcflix.movie.MovieServiceGrpc;
import com.raghav.grpcflix.user.UserGenreUpdateRequest;
import com.raghav.grpcflix.user.UserSearchRequest;
import com.raghav.grpcflix.user.UserSearchResponse;
import com.raghav.grpcflix.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieServiceBlockingStub;

    public List<RecommendedMovie> getUserMovieSuggestions(String loginId) {
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserSearchResponse userSearchResponse = this.userServiceBlockingStub.getUserGenre(userSearchRequest);
        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder().setGenre(userSearchResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse = this.movieServiceBlockingStub.getMovies(movieSearchRequest);
        List<RecommendedMovie> recommendedMovies = movieSearchResponse.getMovieList()
                                                                      .stream()
                                                                      .map(movieDto -> new RecommendedMovie(movieDto.getTitle(), movieDto.getYear(), movieDto.getRating()))
                                                                      .collect(Collectors.toList());
        return recommendedMovies;
    }

    public void updateUserGenre(UserGenre userGenre) {
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                                                                              .setLoginId(userGenre.getLoginId())
                                                                              .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                                                                              .build();
        UserSearchResponse userSearchResponse = this.userServiceBlockingStub.updateUserGenre(userGenreUpdateRequest);

    }

}
