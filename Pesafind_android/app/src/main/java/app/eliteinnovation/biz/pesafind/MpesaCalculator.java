package app.eliteinnovation.biz.pesafind;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MpesaCalculator extends AppCompatActivity {
    TextView txtValueAmount,txtChargeReg,txtValueReg,txtChargeUnreg,txtValueUnreg,
            txtChargesWithdraw,txtValueWithdraw, withdrawTitle, sendToRegisteredTitle, sendToUnregisteredTitle;
    double chargeReg,chargeUnreg,chargeWithdraw,valueReg,valueUnreg,valueWithdraw;
    String st_chargeReg,st_chargeUnreg,st_chargeWithdraw,st_valueReg,st_valueUnreg,st_valueWithdraw;
    double amount;
    EditText txtTotalAmount;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_mpesa_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
//            actionBar.setSubtitle(getString(R.string.subtitle));
            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        actionBar.setSubtitle("Enter amount to send or withdraw");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        withdrawTitle = (TextView) findViewById(R.id.withdrawTitle);
        sendToRegisteredTitle  = (TextView) findViewById(R.id.sendToRegistered);
        sendToUnregisteredTitle  = (TextView) findViewById(R.id.sendToUnRegistered);

        txtTotalAmount = (EditText) findViewById(R.id.txtTotalAmount);
        txtChargeReg = (TextView) findViewById(R.id.txtChargeReg);
        txtValueReg = (TextView) findViewById(R.id.txtValueReg);
        txtChargeUnreg = (TextView) findViewById(R.id.txtChargeUnreg);
        txtValueUnreg = (TextView) findViewById(R.id.txtValueUnreg);
        txtChargesWithdraw = (TextView) findViewById(R.id.txtChargesWithdraw);
        txtValueWithdraw = (TextView) findViewById(R.id.txtValueWithdraw);

        txtTotalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!txtTotalAmount.getText().toString().trim().equals("")) {
                    amount = Double.parseDouble(txtTotalAmount.getText().toString().trim());
//                    Toast.makeText(getApplicationContext(), "before change value is : " + amount, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtTotalAmount.getText().toString().trim().equals("")){
//                    txtValueAmount.setText("");
                    txtChargeReg.setText("");
                    txtValueReg.setText("");
                    txtChargeUnreg.setText("");
                    txtValueUnreg.setText("");
                    txtChargesWithdraw.setText("");
                    txtValueWithdraw.setText("");
                    withdrawTitle.setText("Withdrawal Charges");
                    sendToRegisteredTitle.setText("Send money registered subscriber");
                    sendToUnregisteredTitle.setText("Send money to a non-registered subscriber");
                }
                else if(Double.parseDouble(txtTotalAmount.getText().toString().trim()) < 50
                        || Double.parseDouble(txtTotalAmount.getText().toString().trim()) > 70000){
                    txtChargeReg.setText("N/A");
                    txtValueReg.setText("N/A");
                }
                else if(Double.parseDouble(txtTotalAmount.getText().toString().trim()) < 100
                        || Double.parseDouble(txtTotalAmount.getText().toString().trim()) > 35000){
                    txtChargeUnreg.setText("N/A");
                    txtValueUnreg.setText("N/A");
                }
                else {
                    amount = Double.parseDouble(txtTotalAmount.getText().toString().trim());
                    withdrawTitle.setText("Withdraw Ksh "+amount+" from agent");
                    sendToRegisteredTitle.setText("Send Ksh "+amount+" to a registered subscriber");
                    sendToUnregisteredTitle.setText("Send Ksh "+amount+" to a non-registered subscriber");
//                Toast.makeText(getApplicationContext(), "after change value is : " + amount, Toast.LENGTH_SHORT).show();

                    HashMap<String, Integer> charges = new Charges(getApplicationContext()).getMpesaCharges(amount);
                    Log.d("size of map : ", "" + charges.size());
                    if (charges.size() > 0) {
                        chargeReg = Integer.parseInt(charges.get("registered").toString());
                        chargeUnreg = Integer.parseInt(charges.get("unregistered").toString());
                        chargeWithdraw = Integer.parseInt(charges.get("withdrawal").toString());
                        valueReg = amount - chargeReg;
                        valueUnreg = amount - chargeUnreg;
                        valueWithdraw = amount - chargeWithdraw;
                        if (chargeReg == 0) {
                            st_chargeReg = st_valueReg = "N/A";
                        } else {
                            st_chargeReg = "Ksh. " + chargeReg;
                            st_valueReg = "Ksh. " + valueReg;
                        }
                        if (chargeUnreg == 0) {
                            st_chargeUnreg = st_valueUnreg = "N/A";
                        } else {
                            st_chargeUnreg = "Ksh. " + chargeUnreg;
                            st_valueUnreg = "Ksh. " + valueUnreg;

                        }
                        if (chargeWithdraw == 0) {
                            st_chargeWithdraw = st_valueWithdraw = "N/A";
                        } else {
                            st_chargeWithdraw = "Ksh. " + chargeWithdraw;
                            st_valueWithdraw = "Ksh. " + valueWithdraw;
                        }

//                        txtValueAmount.setText("Ksh. " + amount);
                        txtChargeReg.setText("Sending fee: "+st_chargeReg);
                        txtValueReg.setText("Transferable amount: "+st_valueReg);
                        txtChargeUnreg.setText("Sending fee: "+st_chargeUnreg);
                        txtValueUnreg.setText("Transferable amount: "+st_valueUnreg);
                        txtChargesWithdraw.setText("Withdrawal fee: "+st_chargeWithdraw);
                        txtValueWithdraw.setText("Withdraw amount: "+st_valueWithdraw);

                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to retrieve charges : ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
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
