package com.wearables.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.wearables.models.BiometricBOModel;
import com.wearables.models.BiometricBPModel;
import com.wearables.networking.NetworkConstants;

public class JSONParser {
	private Context mContext;

	public JSONParser(Context context) throws JSONException {
		this.mContext = context;

	}

	public void parseResponse(String response) {
		try {
//			System.out.println("Parse resposne");
//			System.out.println(response);
			JSONObject jObject = new JSONObject(response);
			SharedPrefs sp = SharedPrefs.getInstance(this.mContext);
			if (jObject != null) {
				
				String accessToken = (!jObject.isNull("AccessToken")) ? jObject
						.getString("AccessToken") : "";
//				System.out.println("AccessToken: " + accessToken);
				sp.setParameters(NetworkConstants.ACCESS_TOKEN, accessToken);
				sp.setLongParameters(NetworkConstants.TIMESTAMP, System.currentTimeMillis());
				String userID = (!jObject.isNull("UserID")) ? jObject
						.getString("UserID") : "";
				sp.setParameters(NetworkConstants.USER_ID, userID);
				String refreshToken = (!jObject.isNull("RefreshToken")) ? jObject
						.getString("RefreshToken") : "";
				//System.out.println(refreshToken);
				sp.setParameters(NetworkConstants.REFRESH_TOKEN, refreshToken);
//				System.out.println("Refresh TOKEN");
//				System.out.println(SharedPrefs.getInstance(this.mContext).getParameters(NetworkConstants.REFRESH_TOKEN));
//				String responseType = (!jObject.isNull("ResponseType")) ? jObject
//						.getString("ResponseType") : "";
//				sp.setParameters(NetworkConstants.RESPONSE_TYPE, responseType);
			}
		} catch (JSONException e) {

		}
	}

	public BiometricBOModel parseSP02(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject jObject = new JSONObject(response);
			if (jObject != null) {
				BiometricBOModel model = null;
				JSONArray boArray = (!jObject.isNull("BODataList")) ? jObject.getJSONArray("BODataList") : new JSONArray();
				for(int i=0; i < boArray.length(); i++){
					JSONObject currObj = boArray.getJSONObject(i);
					String bloodOxy = (!currObj.isNull("BO")) ? currObj
							.getString("BO") : "";
					String mDate = (!currObj.isNull("MDate")) ? currObj
							.getString("MDate") : "";
					
					model = new BiometricBOModel(Integer.parseInt(bloodOxy), Utils.getTotalMillisecondTime(Long.parseLong(mDate)));
//					System.out.println("Blood oxygen " + bloodOxy + ": MDATE" + mDate);
				}
				return model;
			}
		} catch (JSONException e) {

		}
		return null;
	}

	public BiometricBPModel parseBP(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject jObject = new JSONObject(response);
			if (jObject != null) {
				// TODE: write JSON parser
				BiometricBPModel model = null;
//				System.out.println(jObject.toString());
				JSONArray bpArray = (!jObject.isNull("BPDataList")) ? jObject.getJSONArray("BPDataList") : new JSONArray();
				
				for(int i=0; i < bpArray.length(); i++){
					JSONObject currObj = bpArray.getJSONObject(i);
					String systolic = (!currObj.isNull("HP")) ? currObj
							.getString("HP") : "";
					String dystolic = (!currObj.isNull("LP")) ? currObj
							.getString("LP") : "";
					String pulse = (!currObj.isNull("HR")) ? currObj
							.getString("HR") : "";
					String mDate = (!currObj.isNull("MDate")) ? currObj
							.getString("MDate") : "";
					model = new BiometricBPModel(Integer.parseInt(systolic),
							Integer.parseInt(dystolic), Integer.parseInt(pulse), Utils.getTotalMillisecondTime(Long.parseLong(mDate)));
					//System.out.println("SY " + systolic + ": dys" + dystolic + ": pulse" + pulse + ": MDATE" + mDate);
				}
				return model;
			}
		} catch (JSONException e) {

		}
		return null;
	}

	public void parseRefreshToken(String response) {
		try {
			JSONObject jObject = new JSONObject(response);
			SharedPrefs sp = SharedPrefs.getInstance(this.mContext);
			if (jObject != null) {
//				System.out.println(jObject.toString());
				String accessToken = (!jObject.isNull("AccessToken")) ? jObject
						.getString("AccessToken") : "";
				sp.setParameters(NetworkConstants.ACCESS_TOKEN, accessToken);

				String refreshToken = (!jObject.isNull("RefreshToken")) ? jObject
						.getString("RefreshToken") : "";
				//System.out.println(refreshToken);
				sp.setParameters(NetworkConstants.REFRESH_TOKEN, refreshToken);
				sp.setLongParameters(NetworkConstants.TIMESTAMP, System.currentTimeMillis());
				
			}
		} catch (JSONException e) {

		}
	}
}
