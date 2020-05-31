package nl.topicus.all_rise.data.response;

import java.util.ArrayList;

public interface ArrayListResponse extends ProviderResponse {
    void response(ArrayList<?> data);
}
