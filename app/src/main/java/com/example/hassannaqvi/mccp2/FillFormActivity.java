package com.example.hassannaqvi.mccp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;

public class FillFormActivity extends AppCompatActivity {

    private static final String TAG = "FILL_FORM_ACTIVITY";
    public static String FORM_ID;
    private String mcFrmNo;
    private DatePicker mc101date;
    private TimePicker mc101time;
    private Spinner mc103town;
    private EditText mc104uc;
    private TextView mc104ucNm;
    private EditText mc105cluster;
    private TextView mc105clusterNm;
    private EditText mc106hhno;
    private Spinner mcExt;
    private RadioGroup mc107epimark;
    private RadioButton mc107epimark_yes;
    private RadioButton mc107epimark_no;
    private RadioButton mc107epimark_unclear;
    private int mc107Selected;
    private RadioGroup mc108permission;
    private RadioButton mc108permission_yes;
    private RadioButton mc108permission_no;
    private RadioButton mc108permission_close;
    private int mc108Selected;
    private TextView formErrorTxt;
    private Boolean formError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        formError = false;

        mcFrmNo = "";
        mc101date = (DatePicker) findViewById(R.id.MC_101DATE);
        mc101time = (TimePicker) findViewById(R.id.MC_101TIME);
        mc103town = (Spinner) findViewById(R.id.MC_103);
        mc104uc = (EditText) findViewById(R.id.MC_104);
        mc104ucNm = (TextView) findViewById(R.id.MC_104UCName);
        mc105cluster = (EditText) findViewById(R.id.MC_105);
        mc105clusterNm = (TextView) findViewById(R.id.MC_105Name);
        mc106hhno = (EditText) findViewById(R.id.MC_106);
        mcExt = (Spinner) findViewById(R.id.MC_Ext);

        mc107epimark = (RadioGroup) findViewById(R.id.MC_107);
            mc107epimark_no = (RadioButton) findViewById(R.id.MC_107_No);
        mc107epimark_yes = (RadioButton) findViewById(R.id.MC_107_Yes);
            mc107epimark_unclear = (RadioButton) findViewById(R.id.MC_107_Unclear);
            mc107Selected = mc107epimark.getCheckedRadioButtonId();


        mc108permission = (RadioGroup) findViewById(R.id.MC_108);
            mc108permission_yes = (RadioButton) findViewById(R.id.MC_108_Yes);
            mc108permission_no = (RadioButton) findViewById(R.id.MC_108_No);
            mc108permission_close = (RadioButton) findViewById(R.id.MC_108_Close);

        formErrorTxt = (TextView) findViewById(R.id.fromError);

        // Cluster Number (mc105cluster) Validation onFocusChange
        mc105cluster.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String clusterNo = mc105cluster.getText().toString();
                    Log.v(TAG, "index=" + clusterNo);
                    if (clusterNo.equals("407069")) {
                        mc105clusterNm.setText("Stylish Garden");
                        mc105clusterNm.setVisibility(View.VISIBLE);
                    } else {
                        mc105clusterNm.setText("Invalid Cluster Number!");
                        mc105clusterNm.setVisibility(View.VISIBLE);
                        mc105cluster.setError("Invalid Cluster Number!");
                        formError = true;

                    }
                }
            }
        });

        // Union Council (mc104uc) Validation onFocusChange
        mc104uc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String UCNo = mc104uc.getText().toString();

                    if (UCNo.equals("007")) {
                        mc104ucNm.setText("Yusuf Goth");
                        mc104ucNm.setVisibility(View.VISIBLE);
                    } else {
                        mc104ucNm.setText("Invalid Union Council Number!");
                        mc104ucNm.setVisibility(View.VISIBLE);
                        mc104uc.setError("Invalid Union Council Number!");
                        formError = true;

                    }
                }
            }
        });

        mc108permission.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Button btnContinue = (Button) findViewById(R.id.btn_Continue);
                Log.d(TAG, "Button Id " +checkedId);
                if(checkedId != mc108permission_yes.getId()){
                        btnContinue.setEnabled(false);
                    }else{
                    btnContinue.setEnabled(true);
                }

             }
        });

    }



    // Start Interview Form 1 - Section 1
    public void startInterview(View view) {
        FORM_ID = mc106hhno.getText().toString() + "-" + mcExt.getSelectedItem().toString();

        // Form Validation - Section 1
        formValidation();

        if(formError == false){
            GenerateFormId();
            StoreTempValues();
            Log.i(TAG, "Form Values Stored! Starting Interview... (S2)");
            Intent fill_form_S2_intent = new Intent(getApplicationContext(), FillFormS2Activity.class);
            startActivity(fill_form_S2_intent);

        } else {
            formError = false;
            formErrorTxt.setText("Please remove all errors to continue!");
            formErrorTxt.setVisibility(View.VISIBLE);

        }
    }



    private void formValidation(){
        //Todo: Issue with fromError status and field level validation (104 & 105)
        mc107Selected = mc107epimark.getCheckedRadioButtonId();
        mc108Selected = mc108permission.getCheckedRadioButtonId();


        if (mc104uc.getText().toString().isEmpty() || mc104uc.getText().toString() == null) {
            mc104uc.setError("Union Council Number not given!");
            formError = true;
            Log.d(TAG, "Error Type: 104");

        }

        if (mc105cluster.getText().toString().isEmpty() || mc105cluster.getText().toString() == null) {
            mc105cluster.setError("Cluster Number not given!");
            formError = true;
            Log.d(TAG, "Error Type: 105");
        }
        if (mc106hhno.getText().toString().isEmpty() || mc105cluster.getText().toString() == null) {
            mc106hhno.setError("Household Number not given!");
            formError = true;
            Log.d(TAG, "Error Type: 106");
        }

        if (mc107Selected == -1) {
            mc107epimark_unclear.setError("Please select an answer!");
            formError = true;
            Log.d(TAG, "Error Type: 107");
        }
        if (mc108Selected == -1) {
            mc108permission_close.setError("Please select an answer!");
            formError = true;
            Log.d(TAG, "Error Type: 108 "+ mc108Selected );
        }
    }
    private void GenerateFormId(){

        char ch = mcExt.getSelectedItem().toString().trim().charAt(0);
        int pos = ch - 'a' + 1;
        int len = mc106hhno.getText().toString().length();

        String hhCode = "";

        if (len < 3){
            String stCode = mc106hhno.getText().toString();
            hhCode = String.format("%03d", stCode);

        }

        mcFrmNo = mc105cluster.getText().toString()+ hhCode + pos;
    }

    private void StoreTempValues(){

        SharedPreferences sharedPref = getSharedPreferences(FORM_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String spDateT = DateFormat.getDateInstance().format(mc101date.getCalendarView().getDate());
        String spTimeT = mc101time.getCurrentHour() + ":" + mc101time.getCurrentMinute();

        editor.putBoolean("formOpen", true);
        editor.putString("spFrmNo", mcFrmNo);
        editor.putString("sp101", String.valueOf(spDateT));
        editor.putString("sp101Time", String.valueOf(spTimeT));

        String mc103Selected = getResources().getStringArray(R.array.MC_103_value)[mc103town.getSelectedItemPosition()];

        editor.putString("sp103", String.valueOf(mc103Selected));
        editor.putString("sp104", mc104uc.getText().toString());
        editor.putString("sp105", mc105cluster.getText().toString());
        editor.putString("sp106", mc106hhno.getText().toString());
        editor.putString("spExt", mcExt.getSelectedItem().toString());

        switch (mc107Selected) {
            case R.id.MC_107_No:
                editor.putString("sp107", "2");
                break;
            case R.id.MC_107_Unclear:
                editor.putString("sp107", "3");
                break;
            case R.id.MC_107_Yes:
                editor.putString("sp107", "1");
                break;
            default:
                editor.putString("sp107", "0");
                break;
        }

        switch (mc108Selected) {
            case R.id.MC_108_No:
                editor.putString("sp108", "2");
                break;
            case R.id.MC_108_Close:
                editor.putString("sp108", "3");
                break;
            case R.id.MC_108_Yes:
                editor.putString("sp108", "1");

                break;
            default:
                editor.putString("sp108", "0");
                break;

        }

        editor.commit();



    }
    public void noInterview(View view){
        FORM_ID = mc106hhno.getText().toString() + "-" + mcExt.getSelectedItem().toString();

        // Form Validation - Section 1
        formValidation();
        StoreTempValues();
        if(formError == false){
            StoreTempValues();
            Log.i(TAG, "Form Values Stored! Starting Interview... (S2)");
            Intent end_form_intent = new Intent(getApplicationContext(), EndFormActivity.class);
            startActivity(end_form_intent);

        } else {
            formError = false;
            formErrorTxt.setText("Please remove all errors to continue!");
            formErrorTxt.setVisibility(View.VISIBLE);

        }

    }


}