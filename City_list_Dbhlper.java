package com.example.sushilverma.mavync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;


public class City_list_Dbhlper extends SQLiteOpenHelper {

   private static final String DATABASE_NAME = "OperationCity.db";
   private static final String TABLE_NAME ="city_table";
   private static final String city_name ="name";
    private static final String city_lat ="latitude";
    private static final String city_long ="longitude";
    private static final String id ="id";

   public City_list_Dbhlper(Context context)
   {
      super(context, DATABASE_NAME , null, 5);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
       db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
      db.execSQL(
      "create table "+ TABLE_NAME +
      "(name text,latitude text,longitude text)"
      );
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
       if(newVersion>oldVersion) {


           if (newVersion > oldVersion) {
               db.execSQL("ALTER TABLE city_table ADD COLUMN id TEXT");

           }

       }

   }

   public boolean insertContact  (String city,String lat,String lon,String id)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("name", city);
       contentValues.put("latitude", lat);
       contentValues.put("longitude", lon);
       contentValues.put("id", id);

       db.insert("city_table", null, contentValues);
      return true;
   }
   
   public Bundle getData(String cityname){
      SQLiteDatabase db = this.getReadableDatabase();
       Bundle local = new Bundle();
       Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME +" where name='"+cityname+"'", null );

       res.moveToFirst();

       int abc=res.getCount();

       while(res.isAfterLast() == false){

           String lati=res.getString(res.getColumnIndex(city_lat));
           String longi=res.getString(res.getColumnIndex(city_long));
           local.putString("latitude",lati);
           local.putString("longitude",longi);
           res.moveToNext();
       }
      return local;
   }
   
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
      return numRows;
   }

   public Integer deleteCity (String id)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(TABLE_NAME,
      "id = ? ", 
      new String[] { id });
   }
   
   public ArrayList<String> getAlldata()
   {
      ArrayList<String> array_list = new ArrayList<String>();
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select name from "+TABLE_NAME, null );
      res.moveToFirst();
      
      while(res.isAfterLast() == false){
      
           String records=res.getString(res.getColumnIndex(city_name));
           array_list.add(records);
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