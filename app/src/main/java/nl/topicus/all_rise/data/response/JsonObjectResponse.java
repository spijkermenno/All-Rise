package nl.topicus.all_rise.data.response;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonObjectResponse extends ProviderResponse {
    void response(JSONObject data) throws JSONException;
}
