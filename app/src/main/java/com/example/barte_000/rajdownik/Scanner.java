package com.example.barte_000.rajdownik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class Scanner extends AppCompatActivity implements OnClickListener{

    @BindView(R.id.scan_button)         Button scanBtn;
    @BindView(R.id.registrationName)    TextView registrationName;
    @BindView(R.id.registrationSurname) TextView registrationSurname;
    @BindView(R.id.registrationIndex)   TextView registrationStudentId;
    @BindView(R.id.shirtSize)           TextView shirtSize;
    @BindView(R.id.phoneNumber)         TextView phoneNumber;
    @BindView(R.id.accepted)            CheckBox registrationAccepted;
    @BindView(R.id.signedDeclaration)   CheckBox signedDeclaration;
    @BindView(R.id.acceptedTerms)       CheckBox acceptedTerms;



    private API.APIInterface apiInterface;
    private String scanFormat,scanContent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        apiInterface = API.getClient();
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
                        acceptedTerms.setChecked(acceptedRegistration.isAccepted_terms());
                        acceptedTerms.setVisibility(View.VISIBLE);
                        signedDeclaration.setChecked(acceptedRegistration.isSigned_declaration());
                        signedDeclaration.setVisibility(View.VISIBLE);
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
