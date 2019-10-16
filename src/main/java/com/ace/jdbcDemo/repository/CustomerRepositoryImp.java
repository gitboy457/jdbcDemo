package com.ace.jdbcDemo.repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Properties;

import com.ace.jdbcDemo.entity.Customer;
import com.mysql.cj.protocol.Resultset;

public class CustomerRepositoryImp implements CustomerRepository {
	private static final Properties properties;
	private Connection con;

	static {
		// load properties file
		properties = new Properties();
		try {
			properties.load(CustomerRepositoryImp.class.getClassLoader().getResourceAsStream("customer.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// loading driver class
		try {
			Class.forName(properties.getProperty("db.mysql.driver.class"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public CustomerRepositoryImp() {

		try {

			// creating connection
			con = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.username"),
					properties.getProperty("db.password"));
			//always make autocommit false
			con.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean saveCustomer(Customer customer) {
		Savepoint sp=null;
		try {

			PreparedStatement pstm = con.prepareStatement(CustomerConstants.INSERT_CUSTOMER);
			pstm.setString(1, customer.getCustId());
			pstm.setString(2, customer.getCust_name());
			pstm.setString(3, customer.getEmail());
			pstm.setString(4, customer.getMobile());
			pstm.setDate(5, customer.getCreateDate());
			//create savepoint
			//savepoints should be created before calling execute statement
		 sp=con.setSavepoint(customer.getCustId()+"Savepoint1");
			int i = pstm.executeUpdate();
			if (i == 1) {
				
				con.commit();
				
				return true;
			}
			
		} catch (SQLException e) {
			try {
				//if any exception occure in the insersation of data then rollback all the un commited changes
				con.rollback(sp);
				//con.rollback();
			
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;

	}

	@Override
	public Customer getByCustId(String CustId) {
		Customer customer = null;
		try {
			customer = new Customer();
			PreparedStatement pstm = con.prepareStatement(CustomerConstants.FIND_BY_CUSTID);
			pstm.setString(1, CustId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				customer.setCustId(rs.getString("cust_id"));
				customer.setCust_name(rs.getString("cust_name"));
				customer.setEmail(rs.getString("email"));
				customer.setMobile("mobile");
				customer.setCreateDate(rs.getDate("create_date"));
				customer.setUpdateDate(rs.getDate("update_date"));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return customer;
	}

}
