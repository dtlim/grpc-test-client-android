package com.voyagerinnovation.grpcclient.chat;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.voyager.grpctest.ChatResponse;
import com.voyager.grpctest.GrpcChatGrpc;
import com.voyagerinnovation.grpcclient.BuildConfig;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by dale on 1/9/17.
 */

public class GrpcChatDataSource implements ChatDataSource {

    private ChatActivity.Listener listener;
    private ManagedChannel managedChannel;
    private GrpcChatGrpc.GrpcChatStub asyncStub;

    private Handler backgroundHandler;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private StreamObserver<com.voyager.grpctest.ChatMessage> requestObserver;
    private StreamObserver<ChatResponse> responseObserver;

    String host = "192.168.226.173";
    int port = 50054;

    public GrpcChatDataSource(ChatActivity.Listener listener) {
        this.listener = listener;
        host = BuildConfig.SERVER_IP;
        initilizeThread();
        initializeChannel();
        initializeObservers();
    }

    private void initilizeThread() {
        HandlerThread handlerThread =  new HandlerThread("Grpc");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        backgroundHandler = new Handler(looper);
    }

    private void initializeChannel() {
        managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        asyncStub = GrpcChatGrpc.newStub(managedChannel);
    }

    private void initializeObservers() {
        responseObserver = new StreamObserver<ChatResponse>() {
            @Override
            public void onNext(ChatResponse value) {
                ChatMessage message = new ChatMessage(value.getUser(), value.getMessage());
                sendToMainThread(message);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };

        requestObserver = asyncStub.chatStream(responseObserver);
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
        com.voyager.grpctest.ChatMessage grpcChatMessage =
                com.voyager.grpctest.ChatMessage.newBuilder()
                        .setUser(message.getName())
                        .setMessage(message.getMessage())
                        .build();
        requestObserver.onNext(grpcChatMessage);
    }

    private void sendToMainThread(final ChatMessage message) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onReceiveChatMessage(message);
            }
        });
    }
}
