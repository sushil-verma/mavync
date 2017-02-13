package com.example.sushilverma.mavync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Open_vehicle_Db extends SQLiteOpenHelper {

   private static final String DATABASE_NAME = "vehicle.db";
   private static final String TABLE_NAME ="o_vehicl";

    private static final String vehicle_latitute ="latitute";
    private static final String vehicle_longitute ="longitude";
    private static final String vehicle_description ="description";
    private Vehicle_Data_lat_long records;


   public Open_vehicle_Db(Context context)
   {

       super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
       db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
      db.execSQL(
      "create table "+TABLE_NAME +
              "(latitute text,longitude text,description text)"
      );
   }



   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
      onCreate(db);
   }

   public boolean insertCategry  (String lat, String lon,String des)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("latitute", lat);
      contentValues.put("longitude", lon);
       contentValues.put("description", des);

      db.insert("o_vehicl", null, contentValues);
      return true;
   }
   
   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
      return res;
   }
   
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
      return numRows;
   }
   
  /* public boolean updatevehicle (String id, String longi, String lati, String address)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("id", id);
      contentValues.put("longi", longi);
      contentValues.put("lati", lati);
      contentValues.put("address", address);
      
      db.update("vehicle", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
      return true;
   }*/

   public Integer deleteContact (Integer id)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(TABLE_NAME,
      "id = ? ", 
      new String[] { Integer.toString(id) });
   }
   
   public ArrayList<Vehicle_Data_lat_long> getAlldata()
   {
      ArrayList<Vehicle_Data_lat_long> array_list = new ArrayList<Vehicle_Data_lat_long>();
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
      res.moveToFirst();
      
      while(res.isAfterLast() == false){
      
        records=new Vehicle_Data_lat_long(res.getString(res.getColumnIndex(vehicle_latitute)),
        		res.getString(res.getColumnIndex(vehicle_longitute)),
                res.getString(res.getColumnIndex(vehicle_description)));

    	  
    	   array_list.add(records);
           res.moveToNext();
   }
      
 
     return array_list;
   }
   
   
   
  /* public List<String>  getVehicleid()
   {
	  SQLiteDatabase db = this.getWritableDatabase();
	  Cursor res =  db.rawQuery( "select id from "+TABLE_NAME, null );
      res.moveToFirst();
      List<String> array_list = new ArrayList<String>();
      while(res.isAfterLast() == false){
          
         String records=res.getString(res.getColumnIndex(vehicle_category_id));
         array_list.add(records);
         res.moveToNext();
     }

      return array_list;
   }*/

   
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