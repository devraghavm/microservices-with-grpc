package com.grpcflix.movie.service;

import com.grpcflix.movie.repository.MovieRepository;
import com.raghav.grpcflix.movie.MovieDto;
import com.raghav.grpcflix.movie.MovieSearchRequest;
import com.raghav.grpcflix.movie.MovieSearchResponse;
import com.raghav.grpcflix.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDto> movieDtos = this.movieRepository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                                                       .stream()
                                                       .map(movie -> MovieDto.newBuilder()
                                                                             .setTitle(movie.getTitle())
                                                                             .setYear(movie.getYear())
                                                                             .setRating(movie.getRating())
                                                                             .build())
                                                       .collect(Collectors.toList());
        MovieSearchResponse movieSearchResponse = MovieSearchResponse.newBuilder().addAllMovie(movieDtos).build();
        responseObserver.onNext(movieSearchResponse);
        responseObserver.onCompleted();
    }
}
