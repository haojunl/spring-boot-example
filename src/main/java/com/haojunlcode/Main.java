package com.haojunlcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
//@ComponentScan(basePackages = "com.haojunlcode" )
//@EnableAutoConfiguration
//@Configuration
@RestController
public class Main {
    private static List<Customer> customers;//db

    static{
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                21

        );
        customers.add(alex);

        Customer jamila = new Customer(
                2,
                "jamila",
                "jamila@gmail.com",
                19

        );
        customers.add(jamila);
    }


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @RequestMapping(
            path = "api/v1/customers",
            method = RequestMethod.GET
    )//same as @GetMapping("api/v1/customer")
    public List<Customer> getCustomers(){
        return customers;
    }
    @RequestMapping(
            path = "api/v1/customers/{customerId}",//{ } path variable
            method = RequestMethod.GET
    )//same as @GetMapping("api/v1/customer")
    public Customer getCustomers(@PathVariable("customerId") Integer customerId){//capture path variable
        Customer customer = customers.stream()
                .filter(c -> c.id.equals(customerId))
                .findFirst()//
                .orElseThrow(
                        () -> new IllegalArgumentException("customer with id [%s] not found" .formatted(customerId)));
        return customer;

    }



    static class Customer{
        private Integer id;
        private String name;
        private String email;
        private Integer age;

        public Customer(){}
        public Customer(Integer id, String name, String email, Integer age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Customer customer)) return false;
            return Objects.equals(getId(), customer.getId()) && Objects.equals(getName(), customer.getName()) && Objects.equals(getEmail(), customer.getEmail()) && Objects.equals(getAge(), customer.getAge());
        }
        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName(), getEmail(), getAge());
        }

        @Override
        public String toString() {
            return "Customer{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

}
