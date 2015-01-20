package com.ies.mysensortag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.ies.blelib.sensor.TiAccelerometerSensor;
import com.ies.blelib.sensor.TiHumiditySensor;
import com.ies.blelib.sensor.TiMagnetometerSensor;
import com.ies.blelib.sensor.TiSensor;
import com.ies.blelib.sensor.TiTemperatureSensor;
import com.ies.mysensortag.ServerReporter.ReportThread;
import com.ies.mysensortag.ServerReporter.SensorValues;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class CkanReport {

	private final static String TAG_ = CkanReport.class.getSimpleName();

	private final static String DEFAULT_SERVER_ADDRESS = "202.121.178.242";
	private final static int DEFAULT_PORT = 80;
	private final static String URL_PATH_ACTION = "/api/3/action/";
	private final static String DEFAULT_API_KEY = "ffe51954-5968-4a31-9fea-4868f7626ebf";
	private final static String DEFAULT_RESOURCE_ID = "7e25697e-149e-4757-a313-75503514d1b3";
	private List<SensorValues> value_list_;
	private Date last_report_time_;
	private boolean is_transferring_;
	private String server_address_;
	private String apikey_;
	private int port_;
	private Handler post_handler_;
	private String post_data_;
	private String resource_id_;
	private boolean t1 = false;
	private boolean t2 = false;
	private String temp1;
	private String temp2;
	private String hum;
	private int i = 0;

	SensorValueRecord r = new SensorValueRecord();

	public CkanReport() {
		this(DEFAULT_SERVER_ADDRESS, DEFAULT_PORT, DEFAULT_API_KEY,
				DEFAULT_RESOURCE_ID);
	}

	public CkanReport(String url, int port, String apikey, String resource_id) {
		server_address_ = url;
		port_ = port;
		apikey_ = apikey;
		is_transferring_ = false;
		last_report_time_ = new Date();
		post_handler_ = new Handler();
		post_data_ = null;
		resource_id_ = resource_id;
	}

	private URL get_url(String path) {
		URL url;
		try {
			url = new URL("http://" + server_address_ + path);
		} catch (MalformedURLException mue) {
			System.err.println(mue);
			return null;
		}
		return url;
	}

	private URL get_action_url(String action) {
		return get_url(URL_PATH_ACTION + action);
	}

	private URL get_datastore_upsert_url() {
		return get_action_url("datastore_upsert");
	}

	protected String post(URL url, String data) {
		String body = "";

		Log.i(TAG_, "api key: " + this.apikey_);
		Log.i(TAG_, "url - " + url.toString() + "  data - " + data);

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost postRequest = new HttpPost(url.toString());
			postRequest.setHeader("X-CKAN-API-Key", this.apikey_);

			StringEntity input = new StringEntity(data);
			input.setContentType("application/json");
			postRequest.setEntity(input);

			HttpResponse response = httpclient.execute(postRequest);
			int statusCode = response.getStatusLine().getStatusCode();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String line = "";
			while ((line = br.readLine()) != null) {
				body += line;
			}

			Log.i(TAG_, "response: " + body);
		} catch (ClientProtocolException cpe) {
			Log.e(TAG_, "ClientProtocolException:" + cpe.toString());
			cpe.printStackTrace();
		} catch (HttpHostConnectException hhce) {
			Log.e(TAG_, "HttpHostConnectException:" + hhce.toString());
			hhce.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG_, "HttpHostConnectException:" + e.toString());
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return body;
	}

	public void report_sensor_data(TiSensor sensor, String id) {
		if (is_too_fast() || is_transferring_) {
			//
			// Just ignore if the sending interval is too fast.
			//
			return;
		}

		SensorValues sensor_values = new SensorValues();
		if (sensor instanceof TiAccelerometerSensor) {
			float[] data = (float[]) sensor.get_value();
			sensor_values.a_x = Float.toString(data[0]);
			sensor_values.a_y = Float.toString(data[1]);
			sensor_values.a_z = Float.toString(data[2]);
		} else if (sensor instanceof TiHumiditySensor) {
			Float data = (Float) sensor.get_value();
			sensor_values.humidity = Float.toString(data);
			t1 = true;
		} else if (sensor instanceof TiMagnetometerSensor) {
			float[] data = (float[]) sensor.get_value();
			sensor_values.m_x = Float.toString(data[0]);
			sensor_values.m_y = Float.toString(data[1]);
			sensor_values.m_z = Float.toString(data[2]);
		} else if (sensor instanceof TiTemperatureSensor) {
			float[] data = (float[]) sensor.get_value();
			sensor_values.ambient = Float.toString(data[0]);
			sensor_values.target = Float.toString(data[1]);
			t2 = true;
		} else {
			Log.e(TAG_, "Unknown sensor type");
		}
		if (t1) {
			if (sensor_values.humidity != null) {
				hum = sensor_values.humidity;
				if (!t2)
					i = i + 3;
				else
					i++;
			}
		}
		if (t2) {
			if (sensor_values.ambient != null) {
				temp1 = sensor_values.ambient;
				temp2 = sensor_values.target;
				if (!t1)
					i = i + 3;
				else
					i++;
			}
		}

		r = new SensorValueRecord();
		r.set_mac(id);
		r.set_environment_temperature(temp1);
		// r.set_environment_temperature(sensor_values.ambient);
		// r.set_surface_temperature(sensor_values.target);
		r.set_surface_temperature(temp2);
		r.set_humidity(hum);
		// r.set_humidity(sensor_values.humidity);

		Gson gson = new Gson();
		/**
		 * List<SensorValueRecord> records = new ArrayList<SensorValueRecord>();
		 * records.add(r); String records_json_str = gson.toJson(r); Log.i(TAG_,
		 * "records json string: " + records_json_str);
		 **/
		CkanDataStoreUpsertParam param = new CkanDataStoreUpsertParam();
		param.add_record(r);
		param.set_resource_id(resource_id_);
		post_data_ = gson.toJson(param);
		if (i > 9) {
			ReportThread report_thread = new ReportThread();
			report_thread.start();
			last_report_time_ = new Date();
		}
	}

	private boolean is_too_fast() {
		Date now = new Date();
		long diff_seconds = (now.getTime() - last_report_time_.getTime()) / 1000;
		if (diff_seconds < 0.5) {
			return true;
		}
		return false;
	}

	public class CkanDataStoreUpsertParam {
		public String resource_id;
		public boolean force;
		public String method;
		public List<SensorValueRecord> records;

		public CkanDataStoreUpsertParam() {
			force = true;
			method = "insert";
			records = new ArrayList<SensorValueRecord>();
		}

		public void add_record(SensorValueRecord r) {
			records.add(r);
		}

		public void set_resource_id(String id) {
			resource_id = id;
		}
	}

	public class SensorValueRecord {
		public String mac_address;
		public String environment_temperature;
		public String surface_temperature;
		public String date;
		public String humidity;

		public SensorValueRecord() {
			Date now = new Date();
			date = now.toString();
		}

		public void set_mac(String mac) {
			mac_address = mac;
		}

		public void set_environment_temperature(String id) {
			environment_temperature = id;
		}

		public void set_humidity(String v) {
			humidity = v;
		}

		public void set_surface_temperature(String sur) {
			surface_temperature = sur;
		}

		public String get_humidity() {
			return humidity;
		}

		public String get_envi() {
			return environment_temperature;
		}
	}

	public class ReportThread extends Thread {
		public void run() {
			is_transferring_ = true;
			try {
				Log.i(TAG_, "start post...");
				post(get_datastore_upsert_url(), post_data_);
				Log.i(TAG_, "End post...");
				is_transferring_ = false;
			} catch (Exception e) {

			}
			t1 = false;
			t2 = false;
			i = 0;
			temp1 = null;
			hum = null;
		}
	}

	class SensorValues {
		public String a_x;
		public String a_y;
		public String a_z;

		public String humidity;

		public String m_x;
		public String m_y;
		public String m_z;

		public String ambient;
		public String target;
	}
}
