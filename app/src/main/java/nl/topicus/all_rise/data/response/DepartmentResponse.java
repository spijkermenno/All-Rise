package nl.topicus.all_rise.data.response;

import nl.topicus.all_rise.model.Department;

public interface DepartmentResponse extends ProviderResponse{
    void response(Department department);
}
