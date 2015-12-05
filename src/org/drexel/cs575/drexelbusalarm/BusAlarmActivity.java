/**
 * Author: Ekta Arora (14115153)
 * Description: This file is the Main Activity class of the Bus Alarm Application.
 */
package org.drexel.cs575.drexelbusalarm;

import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.NumberPicker.OnValueChangeListener;

public class BusAlarmActivity extends Activity {

	// Spinner element
	private Spinner spinnerRoute, spinnerStreet, spinnerTime;
	private String route = "";
	private String street = "";
	private String time = "";

	private String selectedDate = "";
	private String busAlarmTime = "";

	private Button btnDatePicker;

	private int year;
	private int month;
	private int day;

	private Calendar schedulecal;
	static final int DATE_DIALOG_ID = 999;

	private TextView txtDate;

	private AlarmManagerBroadcastReceiver alarm;
	private Button btnSetAlarm;

	private NumberPicker setMinutes;
	private int setMinuteForAlarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_alarm);

		
		//	Creating the database for the application
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.createDatabase();
		
		
		//Setting Alarm properties
		alarm = new AlarmManagerBroadcastReceiver();

		// set date picker as current date
		final Calendar calender = Calendar.getInstance();
		year = calender.get(Calendar.YEAR);
		month = calender.get(Calendar.MONTH);
		day = calender.get(Calendar.DAY_OF_MONTH);

		Format formatter = new SimpleDateFormat("MMM,dd yyyy");
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtDate.setText("Date Selected: " + formatter.format((calender.getTime())));

		selectedDate = formatter.format(calender.getTime()).toString();
		schedulecal = new GregorianCalendar(year, month, day);

		btnSetAlarm = (Button) findViewById(R.id.btnSubmit);
		btnSetAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onetimeTimer();
			}
		});

		
		//Setting the Range of Number Picker Values
		setMinutes = (NumberPicker) findViewById(R.id.setMinutes);
		setMinutes.setMinValue(1);
		setMinutes.setMaxValue(60);
		setMinutes.setWrapSelectorWheel(false);
		addListenerOnNumberPicker();

		btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
		btnDatePicker.setHorizontalFadingEdgeEnabled(true);
		addDatePickerListenerOnButton();
		
		spinnerRoute = (Spinner) findViewById(R.id.spinner_route);
		spinnerStreet = (Spinner) findViewById(R.id.spinner_street);
		spinnerTime = (Spinner) findViewById(R.id.spinner_time);
		
		loadSpinnerRoute();

		//Loading Route Spinner data 
		spinnerRoute.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				route = spinnerRoute.getSelectedItem().toString();
				Log.d("OnItemSelected ", "selected route is " + route);
				loadSpinnerStreet(route);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
		//Loading Street Spinner data
		spinnerStreet.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				street = spinnerStreet.getSelectedItem().toString();
				Log.d("OnItemSelected ", "selected street is " + street);
				loadSpinnerTime(street, route);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
		
		//Loading Bus Timing spinner data
		spinnerTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				time = spinnerTime.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}

		});

	}

	protected void OnStart() {
		Log.i("BusAlarmActivity.onStart()::", "Starting now");
		super.onStart();
	}

	/**
	 * This function is used to set one time Alarm based on the time and date
	 * selected by the user
	 */
	public void onetimeTimer() {

		Context context = this.getApplicationContext();
		if (!alarm.equals(null)) {

			busAlarmTime = alarm.setOneTimeAlarm(context, time, setMinuteForAlarm, selectedDate);
			alarmSetAlertDialogBox(busAlarmTime);

		} else {
			Toast.makeText(context, "Alarm needs parameter", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * This function defines a Listener for Number Picker element
	 */
	public void addListenerOnNumberPicker() {

		setMinutes.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				setMinuteForAlarm = newVal;
			}
		});

	}

	/**
	 * This function defines a Listener for Date Picker Button
	 */
	public void addDatePickerListenerOnButton() {

		btnDatePicker = (Button) findViewById(R.id.btnDatePicker);

		btnDatePicker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		}
		return null;
	}

	/**
	 * This Function is called when Date Picker Dilog box is closed after
	 * selecting the date
	 * 
	 */
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			schedulecal = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);

			Calendar calender =new GregorianCalendar(year, month, day);

			String[] weekdays = new DateFormatSymbols().getWeekdays();
			String dayOfWeek = weekdays[schedulecal.get(Calendar.DAY_OF_WEEK)];

			Format formatter = new SimpleDateFormat("MMM,dd yyyy");
			Date dateSelected = schedulecal.getTime();
			//Date dateToday = calender.getTime();

			if (dayOfWeek.equalsIgnoreCase("Saturday") || dayOfWeek.equalsIgnoreCase("Sunday")) {
				Toast.makeText(getApplicationContext(), "Bus service unavailable on Saturdays and Sunday",
						Toast.LENGTH_LONG).show();
			} else if (calender.getTime().compareTo(dateSelected) > 0) {
				Toast.makeText(getApplicationContext(), "Please enter a valid date", Toast.LENGTH_LONG).show();
				txtDate.setText("Date Selected: " + formatter.format((calender.getTime())));
				selectedDate = formatter.format(calender.getTime()).toString();
			} else {
				txtDate.setText("Date Selected: " + formatter.format((schedulecal.getTime())));
				selectedDate = formatter.format(schedulecal.getTime()).toString();
			}
		}
	};

	/**
	 * This function loads the spinner Bus timings data from SQLite database
	 * using ArrayAdapter
	 */
	private void loadSpinnerTime(String street, String route) {

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		List<String> timings = db.getAllTimings(street, route);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				timings);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerTime.setAdapter(dataAdapter);
	}

	/**
	 * This function loads the spinner Street data from SQLite database using
	 * ArrayAdapter
	 */
	private void loadSpinnerStreet(String route) {

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		List<String> streets = db.getAllStreets(route);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				streets);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerStreet.setAdapter(dataAdapter);
	}

	/**
	 * This function loads the spinner Route data from SQLite database using
	 * ArrayAdapter
	 */
	private void loadSpinnerRoute() {

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		List<String> routes = db.getAllRoutes();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routes);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerRoute.setAdapter(dataAdapter);

	}

	/**
	 * This function displays an Alert Dialog Box with 'Ok' button, informing
	 * user that alarm has been set for the selected date and time
	 * 
	 * @param busTime
	 *            This parameter holds the value of time for which alarm is set
	 *
	 */
	private void alarmSetAlertDialogBox(String busTime) {

		Log.i("AlarmManagerBroadcastReceiver.callAlertDialogBox()::",
				"Showing the Alert Dialog Box after setting the Alarm");

		AlertDialog.Builder alertDialogBuilder;
		alertDialogBuilder = new AlertDialog.Builder(BusAlarmActivity.this);

		alertDialogBuilder.setTitle("Drexel Bus Alert");

		alertDialogBuilder.setMessage("Your Alarm is set for Time : " + busTime).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}
}
