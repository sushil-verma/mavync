package com.example.sushilverma.mavync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Confirmd_Dbhlper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "Confirmed_vhcl.db";
   public static final String CONTACTS_TABLE_NAME ="cnfrmd_vehicle";
   public static final String vehicle_no ="vehicle_no";
   public static final String vehicle_Id ="id";

   private Cnfrmd_Vehicle_record records;
   /* private Vehicle_record records1;
   private Vehicle_record records2;
 
   private HashMap hp;*/

   public Confirmd_Dbhlper(Context context)
   {
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
      db.execSQL(
      "create table cnfrm_vehicle " +
      "(id text,vehicle_no text)"
      );
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS cnfrm_vehicle");
      onCreate(db);
   }

   public boolean insertContact  (String id, String vehicle_no)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("id", id);
      contentValues.put("vehicle_no", vehicle_no);

      db.insert("cnfrm_vehicle", null, contentValues);
      return true;
   }
   
   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from cnfrm_vehicle", null );
      return res;
   }
   
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
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
      return db.delete("cnfrm_vehicle",
      "id = ? ", 
      new String[] { Integer.toString(id) });
   }
   
   public ArrayList<Cnfrmd_Vehicle_record> getAlldata()
   {
      ArrayList<Cnfrmd_Vehicle_record> array_list = new ArrayList<Cnfrmd_Vehicle_record>();
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from cnfrm_vehicle", null );
      res.moveToFirst();
      
      while(res.isAfterLast() == false){
      
        records=new Cnfrmd_Vehicle_record(res.getString(res.getColumnIndex(vehicle_Id)),
        		res.getString(res.getColumnIndex(vehicle_no)));

    	  
    	   array_list.add(records);
           res.moveToNext();
   }
      
 
     return array_list;
   }
   
   
   
   public List<String>  getVehicleid()
   {
	  SQLiteDatabase db = this.getWritableDatabase();
	  Cursor res =  db.rawQuery( "select id from vehicle ", null );
      res.moveToFirst();
      List<String> array_list = new ArrayList<String>();
      while(res.isAfterLast() == false){
          
         String records=res.getString(res.getColumnIndex(vehicle_Id));
         array_list.add(records);
         res.moveToNext();
     }

      return array_list;
   }
   
   
   // to be continue from here next day
   
   /*public List<String>  getAllVehicleNojourney()
   {
	  String records=null;
	  SQLiteDatabase db = this.getWritableDatabase();
	  Cursor res =  db.rawQuery( "select id from vehicle where vehicle_load_status=1", null );
      res.moveToFirst();
      List<String> array_list = new ArrayList<String>();
      while(res.isAfterLast() == false){
          
         //if(res.getString(res.getColumnIndex(vehicle_no)).equals(String.valueOf(1)))
        	 
    	 records=res.getString(res.getColumnIndex(vehicle_no));
         array_list.add(records);
         
        
         res.moveToNext();
     }
        
   
       return array_list;
   }
   */
 
   
   
   //....................................................
   
   
   public List<String> getAllVehicle_no(){
       List<String> labels = new ArrayList<String>();
        
       // Select All Query
       String selectQuery = "SELECT  vehicle_no FROM " + CONTACTS_TABLE_NAME;
     
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
     
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
               labels.add(cursor.getString(cursor.getColumnIndex(vehicle_no)));
           } while (cursor.moveToNext());
       }
        
       // closing connection
       cursor.close();
       db.close();
        
       // returning lables
       return labels;
   }
   
   
  public void deletedb()
   {
	  SQLiteDatabase db = this.getWritableDatabase();
	  db.execSQL("DROP TABLE IF EXISTS cnfrmd_vehicle");
   }
  
  public void deletetb()
  {
	  SQLiteDatabase db = this.getWritableDatabase();
	  db.execSQL("delete from "+ CONTACTS_TABLE_NAME);
	  db.close();
  }
}