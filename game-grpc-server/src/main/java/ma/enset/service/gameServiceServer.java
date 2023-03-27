package ma.enset.service;

import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Game;
import ma.enset.stubs.gameServiceGrpc;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class gameServiceServer extends gameServiceGrpc.gameServiceImplBase{
    //int secretNumber;
    private int secretNumber = ThreadLocalRandom.current().nextInt(1, 21);

    public int getSecretNumber() {
        return secretNumber;
    }

    Map<String ,StreamObserver<Game.messageResponse>> players = new HashMap<>();
    @Override
    public void login(Game.connect request, StreamObserver<Game.messageResponse> responseObserver) {

        System.out.println("player "+request.getUserName()+" in The Game");
        String userName=request.getUserName();
        players.put(userName,responseObserver);
        Game.messageResponse response = Game.messageResponse.newBuilder()
                .setFrom("server")
                .setMessage("Welcome "+userName+" To Numbers Game")
                .build();
        responseObserver.onNext(response);
    }

    @Override
    public void guess(Game.messageRequest request, StreamObserver<Game.messageResponse> responseObserver) {
        String from=request.getFrom();
        String number=request.getNumber();
        if(Integer.parseInt(number) < secretNumber){
            Game.messageResponse response = Game.messageResponse.newBuilder()
                    .setFrom("Game Server")
                    .setMessage("Votre nombre est plus petit !")
                    .build();
            players.get(from).onNext(response);
        } else if (Integer.parseInt(number) > secretNumber) {
            Game.messageResponse response = Game.messageResponse.newBuilder()
                    .setFrom("Game Server")
                    .setMessage("Votre nombre est plus grand !")
                    .build();

            players.get(from).onNext(response);
        }else {
            Game.messageResponse response = Game.messageResponse.newBuilder()
                    .setFrom(from)
                    .setMessage("BRAVO vous avez gagne !")
                    .build();
            players.get(from).onNext(response);

            Game.messageResponse broadcast = Game.messageResponse.newBuilder()
                    .setFrom(from)
                    .setMessage("Le gagnant est "+ from)
                    .build();
            for (String name : players.keySet()) {
                if(!name.equals(from)){
                    players.get(name).onNext(broadcast);
                }
            }
        }

    }
}

