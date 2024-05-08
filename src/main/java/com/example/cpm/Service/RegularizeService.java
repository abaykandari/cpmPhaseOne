package com.example.cpm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cpm.Entity.Regularize;
import com.example.cpm.Repo.RegularizeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegularizeService {

    @Autowired
    private RegularizeRepository employeeRepository;

    public List<Regularize> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Regularize> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Regularize createEmployee(Regularize employee) {
        return employeeRepository.save(employee);
    }

    public Regularize updateEmployee(Long id, Regularize updatedEmployee) {
        if (employeeRepository.existsById(id)) {
            updatedEmployee.setEmpId(id);
            return employeeRepository.save(updatedEmployee);
        }
        return null;
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
