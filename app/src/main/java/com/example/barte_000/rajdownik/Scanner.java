package com.example.barte_000.rajdownik;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.AmbientLightManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;


public class Scanner extends AppCompatActivity implements OnClickListener{

    @BindView(R.id.scan_button)         Button scanBtn;
    @BindView(R.id.scan_button2)        Button scanBtn2;
    @BindView(R.id.registrationName)    TextView registrationName;
    @BindView(R.id.registrationSurname) TextView registrationSurname;
    @BindView(R.id.registrationIndex)   TextView registrationStudentId;
    @BindView(R.id.shirtSize)           TextView shirtSize;
    @BindView(R.id.phoneNumber)         TextView phoneNumber;
    @BindView(R.id.accepted)            CheckBox registrationAccepted;
    @BindView(R.id.signedDeclaration)   CheckBox signedDeclaration;
    @BindView(R.id.acceptedTerms)       CheckBox acceptedTerms;


    private AmbientLightManager alm;
    private API.APIInterface apiInterface;
    private String scanFormat,scanContent ;
    private ZXingScannerView mScannerView;
    private IntentIntegrator scanIntegrator;
    private boolean contScan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        apiInterface = API.getClient();
        scanBtn.setOnClickListener(this);
        scanBtn2.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.scan_button){
            scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
            contScan = false;
        }
        if(v.getId() == R.id.scan_button2){
            scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
            contScan = true;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

            scanContent = scanningResult.getContents();
            scanFormat = scanningResult.getFormatName();

            Call<Registrations> call = apiInterface.sendIndexNumber(scanContent.substring(4));
            call.enqueue(new Callback<Registrations>() {

                @Override
                public void onResponse(Call<Registrations> call, retrofit2.Response<Registrations> response) {

                    if(response.isSuccessful()) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Index found and changed! " + scanContent.substring(4), Toast.LENGTH_SHORT);
                        toast.show();

                        Registrations acceptedRegistration = response.body();
                        registrationName.setText("Name: " + acceptedRegistration.getName());
                        registrationSurname.setText("Surname: " + acceptedRegistration.getSurname());
                        registrationStudentId.setText("StudentId: " +acceptedRegistration.getStudent_id());
                        shirtSize.setText("Shirt shize: " +acceptedRegistration.getShirt_size());
                        phoneNumber.setText("Phone number: " + acceptedRegistration.getPhone_number());

                        registrationAccepted.setChecked(acceptedRegistration.isAccepted());
                        registrationAccepted.setVisibility(View.VISIBLE);
                        registrationAccepted.setClickable(false);
                        acceptedTerms.setChecked(acceptedRegistration.isAccepted_terms());
                        acceptedTerms.setVisibility(View.VISIBLE);
                        acceptedTerms.setClickable(false);
                        signedDeclaration.setChecked(acceptedRegistration.isSigned_declaration());
                        signedDeclaration.setVisibility(View.VISIBLE);
                        signedDeclaration.setClickable(false);
                    }else{
                        if(response.code() == 409){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Too many people: " + scanContent.substring(4), Toast.LENGTH_SHORT);
                            toast.show();

                            // send the tone to the "alarm" stream (classic beeps go there) with 50% volume
                            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
                            toneG.startTone(ToneGenerator.TONE_DTMF_S, 1000); // 200 is duration in ms
                        }else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Not found" + scanContent.substring(4), Toast.LENGTH_SHORT);
                            toast.show();

                            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 1000);
                            toneG.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP, 3000); // 200 is duration in ms
                        }
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
        if(contScan)
            scanIntegrator.initiateScan();
    }
}
