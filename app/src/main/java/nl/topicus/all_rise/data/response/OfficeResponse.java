package nl.topicus.all_rise.data.response;

import nl.topicus.all_rise.model.Office;

public interface OfficeResponse extends ProviderResponse{
    void response(Office office);
}
