package com.voyagerinnovation.grpcclient.secure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.voyager.grpctest.secure.Response;
import com.voyagerinnovation.grpcclient.R;

/**
 * Created by amw on 1/11/2017.
 */

public class SecureActivity extends AppCompatActivity {
    EditText editTextRequest;
    Button buttonSendRequest;
    TextView textViewServerResponse;

    private SecureDataSource secureDataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        editTextRequest = (EditText) findViewById(R.id.edittext_api_request);
        buttonSendRequest = (Button) findViewById(R.id.button_api_send_request);
        textViewServerResponse = (TextView) findViewById(R.id.textview_api_server_response);

        buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secureDataSource.requestFromServer(editTextRequest.getText().toString(), "password123");
            }
        });

        secureDataSource = new GrpcSecureDataSource(this, new SecureActivity.Listener() {
            @Override
            public void onDataLoaded(Response response) {
                textViewServerResponse.setText(response.getContent());
            }
        });


    }

    public interface Listener {
        void onDataLoaded(Response response);
    }
}
