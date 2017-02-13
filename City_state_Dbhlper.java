package com.example.sushilverma.mavync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class City_state_Dbhlper extends SQLiteOpenHelper {

   private static final String DATABASE_NAME = "City_list.db";
   private static final String TABLE_NAME ="city_list";
   private static final String city_name ="city_name";




   public City_state_Dbhlper(Context context)
   {
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
       db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
      db.execSQL(
      "create table "+ TABLE_NAME +
      "(city_name text)"
      );
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
      onCreate(db);
   }

   public boolean insertContact  (String city)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("city_name", city);
      db.insert("city_list", null, contentValues);
      return true;
   }
   
   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from"+ TABLE_NAME, null );
      return res;
   }
   
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
      return numRows;
   }

   public Integer deleteCity (Integer id)
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