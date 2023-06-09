package com.haojunlcode.customer;



import com.haojunlcode.exception.DuplicateResourceException;
import com.haojunlcode.exception.RequestValidationException;
import com.haojunlcode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }



    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(//if does not exist throw
                        () -> new ResourceNotFoundException("customer with id [%s] not found" .formatted(id))
                );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exist
        String email = customerRegistrationRequest.email();
        if(customerDao.existPersonWithEmail(email)){
            throw new DuplicateResourceException("email already taken");
        }
        //add
        Customer customer = new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age()
                );
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer customerId){

        if(!customerDao.existPersonWithId(customerId)){
            throw new ResourceNotFoundException("customer with id [%s] not found" .formatted(customerId));
        }
        customerDao.deleteCustomerById(customerId);
    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest customerUpdateRequest){
        Customer customer = getCustomer(customerId);

        boolean change = false;
        if(customerUpdateRequest.name()!=null && !customerUpdateRequest.name().equals(customer.getName()) ){
            customer.setName(customerUpdateRequest.name());
            change=true;
        }
        if(customerUpdateRequest.email()!=null && !customerUpdateRequest.email().equals(customer.getEmail()) ){
            if(customerDao.existPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(customerUpdateRequest.email());
            change=true;
        }
        if(customerUpdateRequest.age()!=null && !customerUpdateRequest.age().equals(customer.getAge()) ){
            customer.setAge(customerUpdateRequest.age());
            change=true;
        }
        if(!change){
            throw new RequestValidationException("no data changes found");
        }

        customerDao.updateCustomer(customer);
    }

}
