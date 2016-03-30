package app.eliteinnovation.biz.pesafind;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Geofrey on 2/12/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="pesapap";
    private static final String TABLE_MPESA="mpesa_charges";
    private static final String TABLE_ATM="atm_charges";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table_mpesa="CREATE TABLE IF NOT EXISTS "+TABLE_MPESA+" (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  min_amount INTEGER," +
                "  max_amount INTEGER," +
                "  registered INTEGER," +
                "  unregistered INTEGER," +
                "  withdrawal INTEGER" +
                ")";
        String addData="INSERT INTO "+TABLE_MPESA+" VALUES (1,10,49,1,0,0),(2,50,100,3,0,10),(3,101,500,11,44,27),(4,501,1000,15,48,27),(5,1001,1500,25,58,27),(6,1501,2500,33,73,27),(7,2501,3500,55,110,49),(8,3501,5000,60,132,66),(9,5001,7500,75,163,82),(10,7501,10000,85,201,110),(11,10001,15000,95,260,159),(12,15001,20000,100,282,176),(13,20001,25000,110,303,187),(14,25001,30000,110,303,187),(15,30001,35000,110,303,187),(16,35001,40000,110,0,275),(17,40001,45000,110,0,275),(18,45001,50000,110,0,275),(19,50001,70000,110,0,330)";
        db.execSQL(table_mpesa);
   db.execSQL(addData);

     String tableATM="CREATE TABLE IF NOT EXISTS "+TABLE_ATM+" (" +
             "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "  banktype INTEGER," +
             "  atm_name VARCHAR(45)," +
             "  amount INTEGER )";


        String addATMs="INSERT INTO "+TABLE_ATM+" VALUES(1, 1, 'VISA', 30),(2, 1, 'KENSWITCH', 50),(3, 1, 'Master Card', 100),(4, 2, 'VISA', 70),(5, 2, 'Master Card', 60),(6, 2, 'KENSWITCH', 80),(7, 3, 'VISA', 30),(8, 3, 'Master Card', 60),(9, 3, 'KENSWITCH', 90),(10, 4, 'VISA', 30),(11, 4, 'KENSWITCH', 50),(12, 4, 'Master Card', 200),(13, 5, 'VISA', 50),(14, 5, 'Master Card', 50),(15, 5, 'KENSWITCH', 50)";
        db.execSQL(tableATM);
        db.execSQL(addATMs);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
