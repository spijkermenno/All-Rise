package nl.topicus.all_rise.data.response;

import nl.topicus.all_rise.model.Workout;

public interface WorkoutResponse extends ProviderResponse{
    void response(Workout workout);
}
