package com.wearables.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.wearables.utils.LogUtils;
import com.wearables.utils.Utils;

import android.os.Parcel;
import android.os.Parcelable;

public class BiometricSummaryModel implements Parcelable {

	private final String TAG = getClass().getSimpleName();
	
	private int mPosture;
	private int mHeartRate;
	private double mBreathingRate;
	private double mCoreTemperature;
	private double mECGAmplitude;
	private long mTimeStampRecorded;
	private String mFormattedTime;
	/**
	 * 
	 */
	public static final Parcelable.Creator<BiometricSummaryModel> CREATOR = new Parcelable.Creator<BiometricSummaryModel>() {
		public BiometricSummaryModel createFromParcel(Parcel in) {
			return new BiometricSummaryModel(in);
		}

		public BiometricSummaryModel[] newArray(int size) {
			return new BiometricSummaryModel[size];
		}
	};

	/**
	 * 
	 * @param in
	 */
	public BiometricSummaryModel(Parcel in)
	{
		readFromParcel(in);
	}
	
	/**
	 * 
	 * @param mPosture
	 * @param mHeartRate
	 * @param mBreathingRate
	 * @param mCoreTemperature
	 * @param mECGAmplitude
	 * @param mTimeStampRecorded
	 */
	public BiometricSummaryModel(int mPosture, int mHeartRate,
			double mBreathingRate, double mCoreTemperature,
			double mECGAmplitude, long mTimeStampRecorded) {
		super();
		this.mPosture = mPosture;
		this.mHeartRate = mHeartRate;
		this.mBreathingRate = mBreathingRate;
		this.mCoreTemperature = mCoreTemperature;
		this.mECGAmplitude = mECGAmplitude;
		this.mTimeStampRecorded = mTimeStampRecorded;
		this.setmFormattedTime(Utils.getFormattedTime(mTimeStampRecorded));
	}

	public int getmPosture() {
		return mPosture;
	}

	public void setmPosture(int mPosture) {
		this.mPosture = mPosture;
	}

	public int getmHeartRate() {
		return mHeartRate;
	}

	public void setmHeartRate(int mHeartRate) {
		this.mHeartRate = mHeartRate;
	}

	public double getmBreathingRate() {
		return mBreathingRate;
	}

	public void setmBreathingRate(double mBreathingRate) {
		this.mBreathingRate = mBreathingRate;
	}

	public double getmCoreTemperature() {
		return mCoreTemperature;
	}

	public void setmCoreTemperature(double mCoreTemperature) {
		this.mCoreTemperature = mCoreTemperature;
	}

	public double getmECGAmplitude() {
		return mECGAmplitude;
	}

	public void setmECGAmplitude(double mECGAmplitude) {
		this.mECGAmplitude = mECGAmplitude;
	}

	public long getmTimeStampRecorded() {
		return mTimeStampRecorded;
	}

	public void setmTimeStampRecorded(long mTimeStampRecorded) {
		this.mTimeStampRecorded = mTimeStampRecorded;
	}

	public String getmFormattedTime() {
		return mFormattedTime;
	}

	public void setmFormattedTime(String mFormattedTime) {
		this.mFormattedTime = mFormattedTime;
	}

	
	@Override
	public int describeContents() {
			return 0;
	}

	private void readFromParcel(Parcel in)
	{
			mPosture = in.readInt();
			mHeartRate = in.readInt();
			
			mBreathingRate = in.readDouble();
			mCoreTemperature = in.readDouble();
			mECGAmplitude = in.readDouble();
			
			mTimeStampRecorded = in.readLong();
			mFormattedTime = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(mPosture);
		dest.writeInt(mHeartRate);
		
		dest.writeDouble(mBreathingRate);
		dest.writeDouble(mCoreTemperature);
		dest.writeDouble(mECGAmplitude);
		
		dest.writeLong(mTimeStampRecorded);
		dest.writeString(mFormattedTime);

	}
	
//	/**
//	 * 
//	 * @return
//	 */
//	public JSONObject getHDJSON()
//	{
//		try
//		{
//			JSONObject object = new JSONObject();
//			object.put("posture", mPosture);
//			object.put("skinTemperature", mCoreTemperature);
//			JSONObject breathingObject = new JSONObject();
//			breathingObject.put("breathing_rate", mBreathingRate);
//			object.put("breathingRate", breathingObject);
//			JSONObject heartObject = new JSONObject();
//			heartObject.put("heart_rate", mHeartRate);
//			object.put("heartRate", heartObject);
//			JSONObject ecgObject = new JSONObject();
//			ecgObject.put("ecg_amplitude", mECGAmplitude);
//			object.put("ecg", ecgObject);
//			object.put("timeRecorded", mFormattedTime);
//			
//			return object;
//		}
//		catch(JSONException e)
//		{
//			LogUtils.LOGE(TAG, "" + e.getMessage());
//		}
//		
//		return null;
//	}
	
	/**
	 * Get json object for the model
	 * @return
	 */
	public JSONObject getJSON()
	{
		try
		{
			JSONObject object = new JSONObject();
			object.put("posture", String.valueOf(mPosture));
			object.put("estimated_core_temperature", String.valueOf(mCoreTemperature));
			object.put("breathing_rate", String.valueOf(mBreathingRate));
			object.put("heart_rate", String.valueOf(mHeartRate));
			object.put("ecg", String.valueOf(mECGAmplitude));
			object.put("time_recorded", String.valueOf(mFormattedTime));
			
			return object;
		}
		catch(JSONException e)
		{
			LogUtils.LOGE(TAG, "" + e.getMessage());
		}
		
		return null;
	}

}
