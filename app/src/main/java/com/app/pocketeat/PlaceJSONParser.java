package com.app.pocketeat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlaceJSONParser {

	ArrayList<String> data=new ArrayList<>();
	
	/** Receives a JSONObject and returns a list */
	public List<HashMap<String,String>> parse(JSONObject jObject){
		
		JSONArray jPlaces = null;
		try {			
			/** Retrieves all the elements in the 'places' array */
			jPlaces = jObject.getJSONArray("predictions");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */		
		return getPlaces(jPlaces);
	}
	
	
	private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
		int placesCount = jPlaces.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> place = null;

		/** Taking each place, parses and adds to list object */
		for(int i=0; i<placesCount;i++){
			try {
				/** Call getPlace with place JSON object to parse the place */

				place = getPlace((JSONObject)jPlaces.get(i));
				boolean isExist=false;
				for(int j=0;j<placesList.size();j++)
				{
					if(placesList.get(0).get("description").toLowerCase().equalsIgnoreCase(place.get("description").toLowerCase()))
					{
						isExist=true;
					}
				}
				if(!isExist)
					placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return placesList;
	}
	
	/** Parsing the Place JSON object */
	private HashMap<String, String> getPlace(JSONObject jPlace){

		HashMap<String, String> place = new HashMap<String, String>();
		
		String id="";
		String reference="";
		String description="";
		
		try {

			JSONObject jobj=jPlace.getJSONObject("structured_formatting");
			description = jobj.getString("main_text");
			id = jPlace.getString("id");
			String full_text = jPlace.getString("description");
			reference = jPlace.getString("reference");

			place.put("full_desc", full_text);
			place.put("description", description);
			place.put("_id",id);
			place.put("reference",reference);

			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return place;
	}
}
