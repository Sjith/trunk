package com.shandagames.android.preferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * @file SettingUtils.java
 * @create 2013-5-22 下午05:48:12
 * @author lilong
 * @description TODO dreamxsky@gmail.com
 */
public final class SettingUtils {
	
	private SettingUtils() {
	}
	
	public static boolean contains(Context context, int resId) {
		return contains(context, context.getString(resId));
	}

	public static boolean contains(Context context, String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.contains(key);
	}

	public static void remove(Context context, int resId) {
		remove(context, context.getString(resId));
	}

	public static void remove(Context context, String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(key);
		commitOrApply(editor);
	}

	public static void set(Context context, int resId, boolean value) {
		set(context, context.getString(resId), value);
	}

	public static void set(Context context, String key, boolean value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		commitOrApply(editor);
	}

	public static void set(Context context, int resId, float value) {
		set(context, context.getString(resId), value);
	}

	public static void set(Context context, String key, float value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putFloat(key, value);
		commitOrApply(editor);
	}

	public static void set(Context context, int resId, int value) {
		set(context, context.getString(resId), value);
	}

	public static void set(Context context, String key, int value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(key, value);
		commitOrApply(editor);
	}

	public static void set(Context context, int resId, long value) {
		set(context, context.getString(resId), value);
	}

	public static void set(Context context, String key, long value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(key, value);
		commitOrApply(editor);
	}

	public static void set(Context context, int resId, String value) {
		set(context, context.getString(resId), value);
	}

	public static void set(Context context, String key, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		commitOrApply(editor);
	}

	public static boolean get(Context context, int resId, boolean defValue) {
		return get(context, context.getString(resId), defValue);
	}

	public static boolean get(Context context, String key, boolean defValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(key, defValue);
	}

	public static float get(Context context, int resId, float defValue) {
		return get(context, context.getString(resId), defValue);
	}

	public static float get(Context context, String key, float defValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getFloat(key, defValue);
	}

	public static int get(Context context, int resId, int defValue) {
		return get(context, context.getString(resId), defValue);
	}

	public static int get(Context context, String key, int defValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getInt(key, defValue);
	}

	public static long get(Context context, int resId, long defValue) {
		return get(context, context.getString(resId), defValue);
	}

	public static long get(Context context, String key, long defValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getLong(key, defValue);
	}

	public static String get(Context context, int resId, String defValue) {
		return get(context, context.getString(resId), defValue);
	}

	public static String get(Context context, String key, String defValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(key, defValue);
	}

	public static SharedPreferences.Editor getEditor(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).edit();
	}

	//////////////////////////////////////////////////////////////////////////
	// Apply method via reflection

	private static final Method APPLY_METHOD = findApplyMethod();

	private static Method findApplyMethod() {
		try {
			Class<Editor> cls = SharedPreferences.Editor.class;
			return cls.getMethod("apply");
		}
		catch (NoSuchMethodException unused) {
			Log.i("",
					"Failed to retrieve Editor.apply(); probably doesn't exist on this phone's OS.  Using Editor.commit() instead.");
			return null;
		}
	}

	public static void commitOrApply(Editor editor) {
		if (APPLY_METHOD != null) {
			try {
				APPLY_METHOD.invoke(editor);
				return;
			}
			catch (InvocationTargetException e) {
				Log.d("", "Failed while using Editor.apply().  Using Editor.commit() instead.", e);
			}
			catch (IllegalAccessException e) {
				Log.d("", "Failed while using Editor.apply().  Using Editor.commit() instead.", e);
			}
		}

		editor.commit();
	}
}