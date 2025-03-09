package muhammadatta.employeemanager.service;

import java.util.UUID;
import java.util.List;
//import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import muhammadatta.employeemanager.myexceptions.UserNotFoundException;

import muhammadatta.employeemanager.model.Employee;
import muhammadatta.employeemanager.repo.EmployeeRepo;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    //@Autowired
    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Employee addEmployee(Employee employee){

        employee.setEmployeeCode(UUID.randomUUID().toString());
        return employeeRepo.save(employee);
    }

    public List<Employee> findAllEmployees(){

        return employeeRepo.findAll();
    }

    public Employee updEmployee(Employee employee){

        return employeeRepo.save(employee);
    }

    public Employee findSpecificEmployee(Long id) {
        return employeeRepo.findEmployeeById(id)
            .orElseThrow(() -> new UserNotFoundException("No employee with ID " + id + " was found"));
    }

    public void deleteEmployee(Long id){
        employeeRepo.deleteEmployeeById(id);
    }

}
