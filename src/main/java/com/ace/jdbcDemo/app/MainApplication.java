package com.ace.jdbcDemo.app;

import java.sql.Date;

import com.ace.jdbcDemo.entity.Customer;
import com.ace.jdbcDemo.repository.CustomerRepository;
import com.ace.jdbcDemo.repository.CustomerRepositoryImp;

public class MainApplication {
	
	
	private static CustomerRepository customerRepository;
	
	static {
		customerRepository = new CustomerRepositoryImp();
	}
	


	public static void main(String[] args) {
		
		Customer customer= new Customer();
		customer.setCustId("avinash123");
		customer.setCreateDate(new Date(new java.util.Date().getTime()));
		customer.setCust_name("avinash");
		customer.setEmail("avinash123@gmail.com");
		customer.setMobile("8765432356");
				
	if (customerRepository.saveCustomer(customer))	{
		System.out.println("customer saved successfully");
	}
	else {
		System.out.println("customer cannot saved ");
	}
		
	
		//System.out.println(	customerRepository.getByCustId("anil123"));
		
	}

}
