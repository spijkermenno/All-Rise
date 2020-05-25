package nl.topicus.all_rise.data.response;

import nl.topicus.all_rise.model.History;

public interface HistoryResponse extends ProviderResponse{
    void response(History history);
}
