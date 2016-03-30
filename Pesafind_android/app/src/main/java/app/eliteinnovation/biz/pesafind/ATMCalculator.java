package app.eliteinnovation.biz.pesafind;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ATMCalculator extends AppCompatActivity {
    EditText txtAmount;
    Spinner listBanks;
    int bank_id,amount;
    ListView lstDataWithdrawal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_atmcalculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lstDataWithdrawal= (ListView)findViewById(R.id.lstBankWithdrawalData2);


        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mActionBar.setDisplayShowHomeEnabled(true);
        LayoutInflater mInflator = LayoutInflater.from(this);

        listBanks = (Spinner) findViewById(R.id.lstBanksWithdraw2);
        listBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank_id = position + 1;
                Toast.makeText(getApplicationContext(), " position is : " + bank_id, Toast.LENGTH_SHORT).show();
                getDisplayCharges();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        txtAmount = (EditText) findViewById(R.id.bankAmount2);
        mActionBar.setHomeButtonEnabled(true);
//        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        txtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!txtAmount.getText().toString().trim().equals("")) {
                    amount = Integer.parseInt(txtAmount.getText().toString().trim());
//                    Toast.makeText(getApplicationContext(), "before change value is : " + amount, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//         Toast.makeText(getActivity(),"Got amount : "+amount,Toast.LENGTH_SHORT).show();
                if (txtAmount.getText().toString().trim().equals("")) {

                } else {
                    amount = Integer.parseInt(txtAmount.getText().toString().trim());
//            if(amount>500) {
                    getDisplayCharges();
//            }
                }
            }
        });
    }
    public  void getDisplayCharges(){
        if(amount>500) {
            Toast.makeText(getApplicationContext(), "Entered here : " + amount   +"bank id : "+bank_id, Toast.LENGTH_SHORT).show();
            ArrayList<HashMap<String, String>> atmCharges = new Charges(getApplicationContext().getApplicationContext()).getATMCharges(bank_id, amount);
            if (atmCharges.size() > 0) {
                Toast.makeText(getApplicationContext(), " With size : " + atmCharges.size(), Toast.LENGTH_SHORT).show();
                ListAdapter adapter = new SimpleAdapter(getApplicationContext(), atmCharges,
                        R.layout.list_bank_withdrawal, new String[]{"atm_name", "charges", "balance"}, new int[]{R.id.txtATMType, R.id.txtATMCharges, R.id.txtATMReceive});
                lstDataWithdrawal.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//                Toast.makeText(getApplication(), "Back", Toast.LENGTH_LONG).show();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
