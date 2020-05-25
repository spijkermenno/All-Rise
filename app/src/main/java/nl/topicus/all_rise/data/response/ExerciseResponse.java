package nl.topicus.all_rise.data.response;

import nl.topicus.all_rise.model.Exercise;

public interface ExerciseResponse extends ProviderResponse{
    void response(Exercise exercise);
}
