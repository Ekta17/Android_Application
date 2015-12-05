/**
 * Author : Ekta Arora (14115153)
 * Description : This file handles database related functions 
 */
package org.drexel.cs575.drexelbusalarm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private String DATABASE_PATH = "DataBasePath";
			
			/*"/data/data/org.drexel.cs575.drexelbusalarm/databases/";*/
	private static final String DATABASE_NAME = "BusAlarmDB";
	private static final String TABLE_SQLITE_MASTER = "sqlite_master";
	private static final int SCHEMA_VERSION = 1;

	public SQLiteDatabase dbSqlite;
	private final Context context;

	/**
	 * DatabaseHandler Constructor
	 * @param context
	 */
	public DatabaseHandler(Context context) {
		super(context, context.getDatabasePath(DATABASE_NAME).toString(), null, SCHEMA_VERSION);
		this.context = context;
		this.DATABASE_PATH=context.getDatabasePath(DATABASE_NAME).toString();
		Log.d("DataBase Path : ",DATABASE_PATH);
	}

	/**
	 * This is a public function called by other classes using DatabaseHandler object to create database.
	 * 
	 */
	public void createDatabase() {
		Log.i("DatabaseHandler.createDatabase()::", "Creating the database");
		createDB();
	}

	/**
	 * This function creates the database if it does not exist  
	 */
	private void createDB() {
		boolean dbExist = DBExists();
		Log.d("DatabaseHandler.createDB()::dbExist?", dbExist ? "true" : "false");

		if (!dbExist) {
			Log.i("DatabaseHandler.createDB::", "DB doesnot exist, calling copyDBFromResourse()");
			this.getReadableDatabase();
			copyDBFromResource();
		}
	}

	/**
	 * This function checks if database exists or not
	 */
	private boolean DBExists() {

		SQLiteDatabase database = null;

		try {
			
			String databasePath = DATABASE_PATH ;
			database = SQLiteDatabase.openDatabase(databasePath, null,
					SQLiteDatabase.OPEN_READWRITE);
			database.setLocale(Locale.getDefault());
			database.setVersion(1);
		} catch (SQLiteException ex) {
			Log.e("DatabaseHandler.DBExists()::", "Database not Found");
		}

		if (database != null) {
			database.close();
		}
		return database != null ? true : false;
	}

	/**
	 * This function creates a database if it does not exist using Database file provided in /assets folder 
	 */
	private void copyDBFromResource() {

		InputStream inputStream = null;
		OutputStream outputStream = null;
		String dbFilePath = DATABASE_PATH;

		try {

			inputStream = context.getAssets().open(DATABASE_NAME);
			outputStream = new FileOutputStream(dbFilePath);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0)
				outputStream.write(buffer, 0, length);

			outputStream.flush();
			outputStream.close();
			inputStream.close();
		} catch (IOException ioException) {
			Log.e("DatabaseHandler.copyDBFromResource()::", "Error occured while creating Database", new Error("Problem copying database from resource file"));
		}
	}

	/**
	 * This function fetches all the Bus routes from the database
	 * @return Returns a List of Bus Routes as Strings  
	 */
	public List<String> getAllRoutes() {
		List<String> routes = new ArrayList<String>();

		String selectQuery = "SELECT name FROM "+TABLE_SQLITE_MASTER +" WHERE TYPE ='table'";
		Log.i("DatabaseHandler.getAllRoutes()::", "Query to select Bus Routes = "+selectQuery );
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				
				if(!cursor.getString(0).equalsIgnoreCase("android_metadata")){
					routes.add(cursor.getString(0));
				}
				else{
					continue;
				}
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return routes;
	}

	/**
	 * This function fetches all the streets from the database for a particular value of 'Route'
	 * @param route Name of the selected Route
	 * @return Returns a List of Streets as Strings  
	 */
	public List<String> getAllStreets(String route) {
		List<String> streets = new ArrayList<String>();

		String selectQuery = "PRAGMA TABLE_INFO("+route+")";
		Log.i("DatabaseHandler.getAllStreets()::", "Query to select Streets = "+selectQuery );
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				streets.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return streets;
	}
	
	/**
	 * This function fetches all the timings from the database for a particular value of 'Street' and 'Route'
	 * @param street Name of the selected Street 
	 * @param route Name of the selected Route
	 * @return Returns a List of Bus Timings as Strings  
	 */
	public List<String> getAllTimings(String street, String route) {
		List<String> time = new ArrayList<String>();

		
		String selectQuery = "SELECT "+street+" FROM " + route +" WHERE "+street+" IS NOT NULL " ;
		Log.i("DatabaseHandler.getAllTimings()::", "Query to select Bus timings = "+selectQuery );
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				time.add(cursor.getString(cursor.getColumnIndex(street)));
			}while (cursor.moveToNext()); 
		}
		
		cursor.close();
		db.close();

		return time;
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
