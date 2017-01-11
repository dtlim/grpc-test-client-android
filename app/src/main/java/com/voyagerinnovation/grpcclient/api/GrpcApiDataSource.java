package com.voyagerinnovation.grpcclient.api;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.voyager.grpctest.GrpcApiGrpc;
import com.voyager.grpctest.Request;
import com.voyager.grpctest.Response;
import com.voyagerinnovation.grpcclient.BuildConfig;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Created by dale on 1/6/17.
 */

public class GrpcApiDataSource implements ApiDataSource {

    private ApiActivity.Listener listener;
    private ManagedChannel managedChannel;
    private GrpcApiGrpc.GrpcApiBlockingStub blockingStub;
    private GrpcApiGrpc.GrpcApiStub asyncStub;

    private Handler backgroundHandler;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    String host = "192.168.226.173";
    int port = 50052;

    public GrpcApiDataSource(ApiActivity.Listener listener) {
        this.listener = listener;
        host = BuildConfig.SERVER_IP;
        initilizeThread();
        initializeChannel();
    }

    private void initilizeThread() {
        HandlerThread handlerThread =  new HandlerThread("Grpc");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        backgroundHandler = new Handler(looper);
    }

    private void initializeChannel() {
        managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        blockingStub = GrpcApiGrpc.newBlockingStub(managedChannel);
        asyncStub = GrpcApiGrpc.newStub(managedChannel);
    }

    @Override
    public void requestFromServer(final String query) {
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                Request request = Request.newBuilder().setQuery(query).setPage(1).build();
                Response response = blockingStub.requestContent(request);
                sendToMainThread(response);
            }
        });
    }

    private void sendToMainThread(final Response response) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDataLoaded(response);
            }
        });
    }
}
