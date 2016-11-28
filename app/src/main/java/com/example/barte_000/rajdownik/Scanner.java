package com.example.barte_000.rajdownik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;

public class Scanner extends AppCompatActivity implements OnClickListener{

    private Button scanBtn;
    private TextView formatTxt, contentTxt, registrationName, registrationSurname;
    private API.APIInterface apiInterface;
    private String scanFormat,scanContent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        apiInterface = API.getClient();
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        registrationName = (TextView)findViewById(R.id.registrationName);
        registrationSurname = (TextView)findViewById(R.id.registrationSurname);

        scanBtn.setOnClickListener(this);
    }

    public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

            scanContent = scanningResult.getContents();
            scanFormat = scanningResult.getFormatName();

            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);

            Call<Registrations> call = apiInterface.sendIndexNumber(scanContent.substring(4));
            call.enqueue(new Callback<Registrations>() {

                @Override
                public void onResponse(Call<Registrations> call, retrofit2.Response<Registrations> response) {

                    if(response.isSuccessful()) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Index found and changed! " + scanContent.substring(4), Toast.LENGTH_SHORT);
                        toast.show();

                        Registrations acceptedRegistration = response.body();
                        registrationName.setText(acceptedRegistration.getName());

                    }else{

                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Not found" + scanContent.substring(4), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                @Override
                public void onFailure(Call<Registrations> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Host not responding!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
