package ma.enset;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Game;
import ma.enset.stubs.gameServiceGrpc;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    static gameServiceGrpc.gameServiceStub stub;
    static String username;

    public static void login(){
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",1818)
                .usePlaintext()
                .build();
        stub = gameServiceGrpc.newStub(managedChannel);
        Scanner sc = new Scanner(System.in);
        System.out.println("Give your Player a name : ");
        username =sc.nextLine();
        Game.connect requestLogin= Game.connect.newBuilder()
                .setUserName(username)
                .build();
        stub.login(requestLogin, new StreamObserver<Game.messageResponse>() {

            @Override
            public void onNext(Game.messageResponse messageResponse) {
                System.out.println(messageResponse + "\n");
            }
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onCompleted() {
            }
        });
    }

    public static void guess(){
        System.out.println("Guess the Magic number 🌟 : ");
        while (true){
            Scanner sc = new Scanner(System.in);

            String number=sc.nextLine();
            Game.messageRequest request= Game.messageRequest.newBuilder()
                    .setFrom(username)
                    .setNumber(number)
                    .build();
            stub.guess(request, new StreamObserver<Game.messageResponse>() {
                    @Override
                    public void onNext(Game.messageResponse messageResponse) {
                    }
                    @Override
                    public void onError(Throwable throwable) {
                    }
                    @Override
                    public void onCompleted() {
                    }
            });
        }

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        login();
        Thread.sleep(2500);
        guess();
        System.in.read();
    }
}