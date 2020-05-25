package nl.topicus.all_rise.data.response;

import nl.topicus.all_rise.model.ExerciseType;

public interface ExerciseTypeResponse extends ProviderResponse {
    void response(ExerciseType exerciseType);
}
