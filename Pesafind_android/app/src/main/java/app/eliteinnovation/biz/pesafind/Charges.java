package app.eliteinnovation.biz.pesafind;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Geofrey on 2/12/2016.
 */
public class Charges extends DatabaseHandler {
private  static final String TABLE_MPESA="mpesa_charges";
    private  static final String TABLE_ATM="atm_charges";
SQLiteDatabase db;


    public Charges(Context context) {
        super(context);
    }

    public HashMap<String,Integer> getMpesaCharges(double amount){
//int charges=0;
    HashMap<String, Integer> charges = new HashMap<String,Integer>();

        db = this.getReadableDatabase();
        String sql="SELECT registered,unregistered,withdrawal FROM "+TABLE_MPESA+" WHERE "+amount+" BETWEEN min_amount AND max_amount";
        Log.d("query is : ",sql);
        Cursor charges_cursor=db.rawQuery(sql,null);
if(charges_cursor.moveToFirst()){
    do{
     charges.put("registered",charges_cursor.getInt(charges_cursor.getColumnIndexOrThrow("registered")));
     charges.put("unregistered",charges_cursor.getInt(charges_cursor.getColumnIndexOrThrow("unregistered")));
     charges.put("withdrawal",charges_cursor.getInt(charges_cursor.getColumnIndexOrThrow("withdrawal")));
    } while (charges_cursor.moveToNext());
}

        return charges;
    }

    public ArrayList<HashMap<String,String>> getATMCharges(int bank_id, int amount){
int balance=0;
        ArrayList<HashMap<String,String>> arrayMap = new ArrayList<HashMap<String, String>>();
      db = this.getReadableDatabase();
        String sql="SELECT atm_name,amount FROM "+TABLE_ATM+" WHERE banktype="+bank_id+"";
        Log.d("query is : ",sql);
        Cursor charges_cursor=db.rawQuery(sql,null);
        if(charges_cursor.moveToFirst()){
            do{
                HashMap<String, String> charges = new HashMap<String,String>();

                balance=amount-charges_cursor.getInt(charges_cursor.getColumnIndexOrThrow("amount"));
                charges.put("atm_name",charges_cursor.getString(charges_cursor.getColumnIndexOrThrow("atm_name")));
                charges.put("charges",""+charges_cursor.getInt(charges_cursor.getColumnIndexOrThrow("amount")));
                charges.put("balance",""+balance);

                arrayMap.add(charges);

            } while (charges_cursor.moveToNext());
        }

        return arrayMap;
    }
}
