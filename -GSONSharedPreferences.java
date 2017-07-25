package com.github.hynra.gsonsharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Array;



/**
 *
 */

public class GSONSharedPreferences {

    private Context context;
    private String mPreferencesName;
    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mEditor;


    public GSONSharedPreferences(Context context, String mPreferencesName){
        this.context = context;
        this.mPreferencesName = mPreferencesName;
        mSharedPrefs = context.getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
        mEditor = mSharedPrefs.edit();
    }


    public GSONSharedPreferences(Context context, String mPreferencesName, int mode){
        this.context = context;
        this.mPreferencesName = mPreferencesName;
        mSharedPrefs = context.getSharedPreferences(mPreferencesName, mode);
        mEditor = mSharedPrefs.edit();
    }


    public GSONSharedPreferences(Context context){
        this.context = context;
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPrefs.edit();
    }



    /** save objects from objects  **/
    public void saveObjects(Object[] objects){
        String[] vals = new String[objects.length];
        for(int i = 0; i <objects.length; i++){
            vals[i] = new Gson().toJson(objects[i]);
            mEditor.putString(objects[i].getClass().getCanonicalName()+i, vals[i]);
        }
        mEditor.putInt(objects.getClass().getCanonicalName(), objects.length);
        commit();
    }


    /** save objects from json array  **/
    public void saveObjects(Object object, JSONArray array){
        try {
            String[] vals = new String[array.length()];
            for(int i = 0; i <vals.length; i++){

                vals[i] = array.getJSONObject(i).toString();
                mEditor.putString(object.getClass().getCanonicalName()+i, vals[i]);
            }
            mEditor.putInt(object.getClass().getCanonicalName()+"[]", array.length());
            commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /** save objects from json array string  **/
    public void saveObjects(Object object, String jArrayString){
        try {
            JSONArray array = new JSONArray(jArrayString);
            String[] vals = new String[array.length()];
            for(int i = 0; i <vals.length; i++){

                vals[i] = array.getJSONObject(i).toString();
                mEditor.putString(object.getClass().getCanonicalName()+i, vals[i]);
            }
            mEditor.putInt(object.getClass().getCanonicalName()+"[]", array.length());
            commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**  save object from object **/
    public void saveObject(Object object){
        String val = new Gson().toJson(object);
        mEditor.putString(object.getClass().getCanonicalName(), val);
        commit();
    }

    /**  save object from string **/
    public void saveObject(Object object, String values){
        mEditor.putString(object.getClass().getCanonicalName(), values);
        commit();
    }

    /**  save object from json object **/
    public void saveObject(Object object, JSONObject values){
        mEditor.putString(object.getClass().getCanonicalName(), values.toString());
        commit();
    }

    public SharedPreferences getSharedPreferences(){
        return mSharedPrefs;
    }



    private void commit(){
        mEditor.commit();
    }

    /**  get object **/
    public Object getObject(Object object) throws ParsingException{
        try {
            String val = mSharedPrefs.getString(object.getClass().getCanonicalName(), "");
            object = object.getClass();
            object = new Gson().fromJson(val, (Class<Object>) object);
        }catch (JsonSyntaxException exception){
            throw new ParsingException(exception.getMessage());
        }
        return object;
    }


    /**  get objects **/
    public Object[] getObjects(Object object) throws ParsingException{
        Object[] objects = null;
        try {
            int size = mSharedPrefs.getInt(object.getClass().getCanonicalName()+"[]", 0);
            objects = (Object[]) Array.newInstance(object.getClass(), size);
            String[] vals = new String[size];
            for(int i = 0; i < size; i++){
                vals[i] = mSharedPrefs.getString(object.getClass().getCanonicalName()+i, "");
                objects[i] = new Gson().fromJson(vals[i], (Class<Object>) object.getClass());
            }
        } catch (Exception e) {
            throw new ParsingException(e.getMessage());
        }
        return objects;
    }


    /**  get json object **/
    public JSONObject getJsonObject(Object object) throws ParsingException{
        String val = mSharedPrefs.getString(object.getClass().getCanonicalName(), "");
        JSONObject jsonObject = null;
        object = object.getClass();
        try {
            jsonObject = new JSONObject(val);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ParsingException(e.getMessage());
        }
        return jsonObject;
    }


    /**  get json object string **/
    public String getJsonObjectString(Object object) throws ParsingException{
        String val = "";
        try {
            val = mSharedPrefs.getString(object.getClass().getCanonicalName(), "");
        }catch (JsonSyntaxException exception){
            throw new ParsingException(exception.getMessage());
        }
        return val;
    }


    /**  get json array **/
    public JSONArray getJsonArray(Object object) throws ParsingException{
        JSONArray jsonArray = new JSONArray();
        try {
            int size = mSharedPrefs.getInt(object.getClass().getCanonicalName()+"[]", 0);
            for(int i =0; i < size; i++){
                JSONObject jsonObject = new JSONObject(mSharedPrefs.getString(object.getClass().getCanonicalName()+i, ""));
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParsingException(e.getMessage());
        }
        return jsonArray;
    }


    /**  get json array string **/
    public String getJsonArrayString(Object object) throws ParsingException{
        JSONArray jsonArray = new JSONArray();
        try {
            int size = mSharedPrefs.getInt(object.getClass().getCanonicalName()+"[]", 0);
            for(int i =0; i < size; i++){
                JSONObject jsonObject = new JSONObject(mSharedPrefs.getString(object.getClass().getCanonicalName()+i, ""));
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParsingException(e.getMessage());
        }
        return jsonArray.toString();
    }
}







package in.edelworks.sharedpreferences;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;

public class Sharedpreferences extends CordovaPlugin {
	public static final String GET_SHARED_PREFERENCES = "getSharedPreferences";
	public static final String PUT_STRING = "putString";
	public static final String GET_STRING = "getString";
	public static final String PUT_BOOLEAN = "putBoolean";
	public static final String GET_BOOLEAN = "getBoolean";
	public static final String PUT_INT = "putInt";
	public static final String GET_INT = "getInt";
	public static final String PUT_FLOAT = "putFloat";
	public static final String GET_FLOAT = "getFloat";
	public static final String PUT_LONG = "putLong";
	public static final String GET_LONG = "getLong";
	public static final String REMOVE = "remove";
	public static final String CLEAR = "clear";
	public static final String SHARED_PREFERENCES = "SharedPreferences";
	public static String PREF_FILE = "";
	public static final String[] MODE_ARRAY = {"MODE_APPEND", "MODE_PRIVATE"};
	SharedPreferences SharedPref;
	SharedPreferences.Editor editor;

	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		//create shared Preferences
		//two param filename and mode
		//returns true if created with success message and false if not with exception message
		if (GET_SHARED_PREFERENCES.equals(action)) {
			PREF_FILE = args.getString(0);
			String modeType = args.getString(1);
			Context context = cordova.getActivity();
			if(in_array(MODE_ARRAY, modeType)){
				if(modeType.equals("MODE_APPEND")){
					try{
						SharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_APPEND);
					}catch(Exception e){
						callbackContext.error("Error creating Shared Preferences" + e.getMessage());
						return false;
					}
				}else if(modeType.equals("MODE_PRIVATE")){
					try{
						SharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_APPEND);
					}catch(Exception e){
						callbackContext.error("Error creating Shared Preferences" + e.getMessage());
						return false;
					}
				}
				callbackContext.success("Shared Preferences Created");
				return true;
			}else{
				callbackContext.error("Invalid Mode provided");
				return false;
			}
		//Put a Sting into the Shared Preferences File
		//params key and value String type
		}else if(PUT_STRING.equals(action)){
			editor = SharedPref.edit();
			try{
				editor.putString(args.getString(0), args.getString(1));
				editor.commit();
			}catch (Exception e){
				callbackContext.error("Error editing Key " + args.getString(0) + " with value " + args.getString(1) + e.getMessage());
				return false;
			}
			callbackContext.success("Added Value " + args.getString(1) + " to Preferences key " + args.getString(0));
			return true;		
		}else if(GET_STRING.equals(action)){									
			String KeyVal;			
			try{				
				if(SharedPref.contains(args.getString(0))){
					KeyVal = SharedPref.getString(args.getString(0), "");
					callbackContext.success(KeyVal);
					return true;
				}else{
					callbackContext.error("No data");
					return false;
				}
			}catch (Exception e){
				callbackContext.error("Could Not Retreive " + args.getString(0) + e.getMessage());
				return false;
			}
		}else if(PUT_BOOLEAN.equals(action)){
			editor = SharedPref.edit();
			try{
				editor.putBoolean(args.getString(0), args.getBoolean(1));
				editor.commit();
			}catch (Exception e){
				callbackContext.error("Error editing Key " + args.getString(0) + " with value " + args.getBoolean(1) + e.getMessage());
				return false;
			}
			callbackContext.success("Added Value " + args.getBoolean(1) + " to Preferences key " + args.getString(0));
			return true;		
		}else if(GET_BOOLEAN.equals(action)){									
			Boolean KeyVal;
			try{				
				if(SharedPref.contains(args.getString(0))){
					KeyVal = SharedPref.getBoolean(args.getString(0), false);
					if(KeyVal.equals(true)){
						callbackContext.success(1);
					}else{
						callbackContext.success(0);
					}
					return true;
				}else{
					callbackContext.error("No data");
					return false;
				}
			}catch (Exception e){
				callbackContext.error("Could Not Retreive " + args.getString(0) + e.getMessage());
				return false;
			}
		}else if(PUT_INT.equals(action)){
			editor = SharedPref.edit();
			try{
				editor.putInt(args.getString(0), args.getInt(1));
				editor.commit();
			}catch (Exception e){
				callbackContext.error("Error editing Key " + args.getString(0) + " with value " + args.getInt(1) + e.getMessage());
				return false;
			}
			callbackContext.success("Added Value " + args.getInt(1) + " to Preferences key " + args.getString(0));
			return true;		
		}else if(GET_INT.equals(action)){									
			Integer KeyVal;
			try{				
				if(SharedPref.contains(args.getString(0))){
					KeyVal = SharedPref.getInt(args.getString(0), 0);
					callbackContext.success(KeyVal);
					return true;
				}else{
					callbackContext.error("No data");
					return false;
				}
			}catch (Exception e){
				callbackContext.error("Could Not Retreive " + args.getString(0) + e.getMessage());
				return false;
			}
		}else if(PUT_LONG.equals(action)){
			editor = SharedPref.edit();
			try{
				editor.putLong(args.getString(0), args.getLong(1));
				editor.commit();
			}catch (Exception e){
				callbackContext.error("Error editing Key " + args.getString(0) + " with value " + args.getLong(1) + e.getMessage());
				return false;
			}
			callbackContext.success("Added Value " + args.getLong(1) + " to Preferences key " + args.getString(0));
			return true;		
		}else if(GET_LONG.equals(action)){									
			Long KeyVal;
			try{				
				if(SharedPref.contains(args.getString(0))){
					KeyVal = SharedPref.getLong(args.getString(0), 0);
					callbackContext.success(KeyVal.toString());
					return true;
				}else{
					callbackContext.error("No data");
					return false;
				}
			}catch (Exception e){
				callbackContext.error("Could Not Retreive " + args.getString(0) + e.getMessage());
				return false;
			}
    }else if(PUT_FLOAT.equals(action)){
      editor = SharedPref.edit();
      try{
        editor.putFloat(args.getString(0), args.getLong(1));
        editor.commit();
      }catch (Exception e){
        callbackContext.error("Error editing Key " + args.getString(0) + " with value " + args.getLong(1) + e.getMessage());
        return false;
      }
      callbackContext.success("Added Value " + args.getLong(1) + " to Preferences key " + args.getString(0));
      return true;    
    }else if(GET_FLOAT.equals(action)){                  
      Float KeyVal;
      try{        
        if(SharedPref.contains(args.getString(0))){
          KeyVal = SharedPref.getFloat(args.getString(0), 0);
          callbackContext.success(KeyVal.toString());
          return true;
        }else{
          callbackContext.error("No data");
          return false;
        }
      }catch (Exception e){
        callbackContext.error("Could Not Retreive " + args.getString(0) + e.getMessage());
        return false;
      }
		}else if(REMOVE.equals(action)){
			editor = SharedPref.edit();
			try{
				editor.remove(args.getString(0));
				editor.commit();
			}catch (Exception e){
				callbackContext.error("Error editing Key " + args.getString(0) + " with value " + args.getLong(1) + e.getMessage());
				return false;
			}
			callbackContext.success("Removed Value from Key " + args.getString(0));
			return true;		
		}else if(CLEAR.equals(action)){
			editor = SharedPref.edit();
			try{				
				editor.clear();
				editor.commit();
			}catch (Exception e){
				callbackContext.error("Could Not Clear Shared preference File " + e.getMessage());
				return false;
			}
			callbackContext.success("Cleared preference File ");
			
			return true;
		}else{
			callbackContext.error("Invalid Action");
			return false;
		}
    }
	
	public static boolean in_array(String[] haystack, String needle) {
	    for(int i=0;i<haystack.length;i++) {
	        if(haystack[i].equals(needle)) {
	            return true;
	        }
	    }
	    return false;
	}
}
