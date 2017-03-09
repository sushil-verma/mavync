package com.example.sushilverma.mavync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Confirm_vehicle_no_Db extends SQLiteOpenHelper {

   private static final String DATABASE_NAME = "Tracking.db";
   private static final String TABLE_NAME ="Confirmvehicle";

    private static final String Vehicle_number ="v_no";


   public Confirm_vehicle_no_Db(Context context)
   {

       super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
       db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
      db.execSQL(
      "create table "+TABLE_NAME +
              "(v_no text)"
      );
   }



   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
      onCreate(db);
   }

   public boolean insert  (String vehicleeno)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("v_no", vehicleeno);


      db.insert("Confirmvehicle", null, contentValues);
      return true;
   }
   

   
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
      return numRows;
   }
   


   public Integer deleterecord (Integer id)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(TABLE_NAME,
      "id = ? ", 
      new String[] { Integer.toString(id) });
   }
   
   public ArrayList<String> getAlldata()
   {
      ArrayList<String> array_list = new ArrayList<String>();
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
      res.moveToFirst();
      
      while(res.isAfterLast() == false){
      
                String vn=res.getString(res.getColumnIndex(Vehicle_number));
                array_list.add(vn);
                res.moveToNext();
      }
      
 
     return array_list;
   }
   
   

   
  public void deletedb()
   {
	  SQLiteDatabase db = this.getWritableDatabase();
	  db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
   }
  
  public void deletetb()
  {
	  SQLiteDatabase db = this.getWritableDatabase();
	  db.execSQL("delete from "+ TABLE_NAME);
	  db.close();
  }


   // closing database
   public void closeDB() {
      SQLiteDatabase db = this.getReadableDatabase();
      if (db != null && db.isOpen())
         db.close();
   }
}