package com.raghav.game.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.raghav.models.Die;
import com.raghav.models.GameState;
import com.raghav.models.Player;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameStateStreamingResponse implements StreamObserver<GameState> {
    private StreamObserver<Die> dieStreamObserver;
    private CountDownLatch latch;

    public GameStateStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(GameState gameState) {
        List<Player> players = gameState.getPlayerList();
        players.forEach(player -> System.out.println(player.getName() + " : " + player.getPosition()));
        boolean isGameOver = players.stream()
                                    .anyMatch(player -> player.getPosition() >= 100);
        if (isGameOver) {
            System.out.println("Game Over!!!");
            this.dieStreamObserver.onCompleted();
        } else {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            this.roll();
        }
        System.out.println("----------------------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        this.latch.countDown();
    }

    public void setDieStreamObserver(StreamObserver<Die> dieStreamObserver) {
        this.dieStreamObserver = dieStreamObserver;
    }

    public void roll() {
        int roll = ThreadLocalRandom.current().nextInt(1, 7);
        Die die = Die.newBuilder()
                     .setValue(roll)
                     .build();
        this.dieStreamObserver.onNext(die);
    }
}
