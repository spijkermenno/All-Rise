package nl.topicus.all_rise.data.response;

import org.json.JSONArray;
import org.json.JSONException;

public interface JsonArrayResponse extends ProviderResponse {
    void response(JSONArray data) throws JSONException;
}
