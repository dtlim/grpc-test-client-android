package com.voyagerinnovation.grpcclient.secure;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.RawRes;

import com.voyager.grpctest.secure.Request;
import com.voyager.grpctest.secure.Response;
import com.voyager.grpctest.secure.GrpcSecureGrpc;
import com.voyagerinnovation.grpcclient.BuildConfig;
import com.voyagerinnovation.grpcclient.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

/**
 * Created by amw on 1/11/2017.
 */

public class GrpcSecureDataSource implements SecureDataSource {

    private Context context;
    private SecureActivity.Listener listener;
    private GrpcSecureGrpc.GrpcSecureBlockingStub blockingStub;

    private Handler backgroundHandler;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    String host = "192.168.226.173";
    int port = 8443;

    public GrpcSecureDataSource(Context context, SecureActivity.Listener listener) {
        this.context = context;
        this.listener = listener;
        host = BuildConfig.SERVER_IP;
        initializeChannel();
        initializeThreads();
    }

    @Override
    public void requestFromServer(final String query, String password) {
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                Request request = Request.newBuilder().setQuery(query).setPage(1).build();
                Response response = blockingStub.requestContent(request);
                sendToMainThread(response);
            }
        });
    }

    private void initializeChannel() {
        SSLSocketFactory sslSocketFactory = createSSLSocketFactory(context, R.raw.server);

        ManagedChannel managedChannel = OkHttpChannelBuilder.forAddress(host, port)
                .sslSocketFactory(sslSocketFactory)
                .build();
        blockingStub = GrpcSecureGrpc.newBlockingStub(managedChannel);
    }

    private void initializeThreads() {
        HandlerThread handlerThread =  new HandlerThread("Grpc");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        backgroundHandler = new Handler(looper);
    }

    private void sendToMainThread(final Response response) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDataLoaded(response);
            }
        });
    }

    // from: https://gist.github.com/aurae/c24ba9a06825cc271f92
    public SSLSocketFactory createSSLSocketFactory(Context context, @RawRes int caRawFile) {
        InputStream caInput = null;
        try {
            // Generate the CA Certificate from the raw resource file
            caInput = context.getResources().openRawResource(caRawFile);
            Certificate ca = CertificateFactory.getInstance("X.509").generateCertificate(caInput);

            // Load the key store using the CA
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Initialize the TrustManager with this CA
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // Create an SSL context that uses the created trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception ex) {
            throw new RuntimeException(ex);

        } finally {
            if (caInput != null) {
                try {
                    caInput.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
