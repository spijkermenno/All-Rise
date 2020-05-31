package nl.topicus.all_rise.data.response;

import nl.topicus.all_rise.model.Employee;

public interface EmployeeResponse extends ProviderResponse {

    void response(Employee employee);

}
