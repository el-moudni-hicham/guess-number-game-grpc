package ma.enset.server;

import io.grpc.ServerBuilder;
import ma.enset.service.gameServiceServer;

import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 1818;
        gameServiceServer game = new gameServiceServer();
        io.grpc.Server server= ServerBuilder.forPort(port)
                .addService(game)
                .build();
        server.start();
        System.out.println("Game Server Started at port "+ port + " ...");
        System.out.println("Secret number is " + game.getSecretNumber());
        server.awaitTermination();
    }
}
