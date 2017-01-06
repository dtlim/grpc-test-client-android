package com.voyagerinnovation.grpcclient.api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.voyager.grpctest.Response;
import com.voyagerinnovation.grpcclient.R;

/**
 * Created by dale on 1/6/17.
 */

public class ApiActivity extends AppCompatActivity {

    WebView webView;
    EditText editTextRequest;
    Button buttonSendRequest;
    TextView textViewServerResponse;

    private ApiDataSource apiDataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        editTextRequest = (EditText) findViewById(R.id.edittext_api_request);
        buttonSendRequest = (Button) findViewById(R.id.button_api_send_request);
        textViewServerResponse = (TextView) findViewById(R.id.textview_api_server_response);

        textViewServerResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiDataSource.requestFromServer(editTextRequest.getText().toString());
            }
        });

        apiDataSource = new GrpcApiDataSource(new Listener() {
            @Override
            public void onDataLoaded(Response response) {
                textViewServerResponse.setText(response.getContent());
            }
        });

//        webView = (WebView) findViewById(R.id.webview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadData("<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "<body>\n" +
//                "\n" +
//                "<p>Open link in a new window or tab: <a href=\"http://www.w3schools.com\" target=\"_blank\">Visit W3Schools!</a></p>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>", "text/html", "UTF-8");
        //webView.loadUrl("http://www.w3schools.com/TAgs/tryit.asp?filename=tryhtml_a_target");
    }

    public interface Listener {
        void onDataLoaded(Response response);
    }
}
