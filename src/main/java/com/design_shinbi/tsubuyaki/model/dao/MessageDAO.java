package com.design_shinbi.tsubuyaki.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.design_shinbi.tsubuyaki.model.entity.Message;

public class MessageDAO {
	protected Connection connection;
	
	public MessageDAO(Connection connection) {
		this.connection = connection;
	}
	
	public void add(int userId, String text) throws SQLException {
		
		// 実装しよう
		
	}
	
	public void update(Message message) throws SQLException {
		
		// 実装しよう
		
	}
	
	public void delete(int id) throws SQLException {
		
		// 実装しよう
		
	}
	
	public List<Message> findAll() throws SQLException {
		
		// 実装しよう
		
		return null;
	}
}
