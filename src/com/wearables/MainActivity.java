package com.wearables;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wearables.networking.NetworkConstants;
import com.wearables.networking.NetworkConstants.METHOD_TYPE;
import com.wearables.networking.NetworkConstants.REQUEST_TYPE;
import com.wearables.networking.NetworkUtils;
import com.wearables.networking.NetworkingTask;
import com.wearables.utils.Constants;
import com.wearables.utils.Constants.SERVICE_ACTIONS;
import com.wearables.utils.SharedPrefs;

public class MainActivity extends Activity implements OnClickListener {

	private Button mBPMonitoringBtn, mBloodOxygenMonitoringBtn, mPiPBtn,
			mDashboardBtn;

	private TextView mBioMetricDetailsView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setActionBar();
		mBioMetricDetailsView = (TextView) findViewById(R.id.biometricDetailsView);

		mBPMonitoringBtn = (Button) findViewById(R.id.ihealthBPBtn);
		mBloodOxygenMonitoringBtn = (Button) findViewById(R.id.ihealthBOBtn);
		mBPMonitoringBtn.setOnClickListener(this);
		mBloodOxygenMonitoringBtn.setOnClickListener(this);
		mPiPBtn = (Button) findViewById(R.id.pipBtn);
		mDashboardBtn = (Button) findViewById(R.id.dashboardBtn);
		mDashboardBtn.setOnClickListener(this);
		mPiPBtn.setOnClickListener(this);

		// Initiate Service to manage bioharness
		Intent intent = new Intent(this, DataCollectService.class);
		intent.putExtra(Constants.INTENT_TASK_ACTION,
				SERVICE_ACTIONS.START_SERVICE);
		startService(intent);
	}

	private void initiateDataPush(int id) {
		if (TextUtils.isEmpty(SharedPrefs.getInstance(MainActivity.this)
				.getParameters(NetworkConstants.ACCESS_TOKEN))) {
			loadWebViewForAuth();

		} else {
			int diff = (int) (System.currentTimeMillis() - SharedPrefs
					.getInstance(MainActivity.this).getLongParameters(
							NetworkConstants.TIMESTAMP));

			/*
			 * If the time is greater than access token expiry and call refresh
			 * the access token
			 */
			if (diff > Constants.EXPIRY_TIME) {
				String url = NetworkUtils.generateUrl(
						NetworkConstants.USER_AUTH_URL,
						NetworkUtils.getRefreshTokenParams(MainActivity.this));

				new NetworkingTask(url, true, METHOD_TYPE.GET,
						REQUEST_TYPE.REFRESH_TOKEN, MainActivity.this)
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				String accessToken = SharedPrefs.getInstance(MainActivity.this)
						.getParameters(NetworkConstants.ACCESS_TOKEN);
				String userID = SharedPrefs.getInstance(MainActivity.this)
						.getParameters(NetworkConstants.USER_ID);

				String url = "";
				REQUEST_TYPE reqType = null;
				if (id == R.id.ihealthBOBtn) {
					url = NetworkUtils.generateUrl(
							NetworkConstants.GET_BIODATA_URL + "/" + userID
									+ "/spo2.json", NetworkUtils.getDataParams(
									accessToken, NetworkConstants.SPO2_SV));

					reqType = REQUEST_TYPE.SP02;

				} else {
					url = NetworkUtils.generateUrl(
							NetworkConstants.GET_BIODATA_URL + "/" + userID
									+ "/bp.json", NetworkUtils.getDataParams(
									accessToken, NetworkConstants.BP_SV));
					reqType = REQUEST_TYPE.BP;
				}

				new NetworkingTask(url, true, METHOD_TYPE.GET, reqType,
						MainActivity.this)
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		}

	}

	private void loadWebViewForAuth() {
		Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
		String url = NetworkUtils.generateUrl(NetworkConstants.USER_AUTH_URL,
				NetworkUtils.getAuthorizationParams()); // TODO:
														// Change
														// here
		intent.putExtra("url", url);
		startActivityForResult(intent, 100);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ihealthBPBtn:
		case R.id.ihealthBOBtn:
			initiateDataPush(v.getId());
			break;
		case R.id.dashboardBtn:
			break;
		case R.id.pipBtn:
			break;
		}
	}

	private void setActionBar() {
		ActionBar actionbar = getActionBar();
		actionbar.setTitle(getString(R.string.tag_cura));
		actionbar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_drawable));
		actionbar.setHomeButtonEnabled(false);
		actionbar.setIcon(R.drawable.ic_launcher);
		actionbar.show();
	}

	@Override
	protected void onStart() {

		super.onStart();
		IntentFilter filter = new IntentFilter("com.wearable.ui");
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				mBioMetricDetailsView.setText(intent.getExtras().getString(
						"summary"));

			}
		}, filter);
		// IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		// case REQUEST_ENABLE_BT:
		// if (resultCode == RESULT_OK) {
		//
		// } else {
		// Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
		// }
		// break;
		case 100:
			String code = data.getStringExtra("code");
			String url = NetworkUtils.generateUrl(
					NetworkConstants.USER_AUTH_URL,
					NetworkUtils.getAccessTokenParams(code));
			new NetworkingTask(url, true, METHOD_TYPE.GET,
					REQUEST_TYPE.ACCESS_TOKEN, this)
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			break;

		case 300:
			// discoverable now
			break;
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

}
