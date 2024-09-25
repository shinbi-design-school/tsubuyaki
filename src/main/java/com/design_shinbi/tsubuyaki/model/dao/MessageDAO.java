package com.design_shinbi.tsubuyaki.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.design_shinbi.tsubuyaki.model.entity.Message;

public class MessageDAO {
	private Connection connection;

	public MessageDAO(Connection connection) {
		this.connection = connection;
	}

	public void add(int userId, String text) throws SQLException {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		String sql = "INSERT INTO messages (user_id, text, created_at, updated_at) "
				+ "VALUES(?, ?, ?, ?)";

		PreparedStatement statement = this.connection.prepareStatement(sql);

		statement.setInt(1, userId);
		statement.setString(2, text);
		statement.setTimestamp(3, now);
		statement.setTimestamp(4, now);

		statement.executeUpdate();
		statement.close();
	}

	public void update(Message message) throws SQLException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        String sql = "UPDATE messages SET user_id = ?, text = ?, "
        		+ "updated_at = ? WHERE id = ?";
        
        PreparedStatement statement = this.connection.prepareStatement(sql);
        
        statement.setInt(1, message.getUserId());
        statement.setString(2, message.getText());
        statement.setTimestamp(3, now);
        statement.setInt(4, message.getId());
        
        statement.executeUpdate();
        statement.close();
    }

	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM messages WHERE id = ?";

		PreparedStatement statement = this.connection.prepareStatement(sql);

		statement.setInt(1, id);

		statement.executeUpdate();
		statement.close();
	}

	private Message createMessage(ResultSet resultSet) throws SQLException {
		Message message = new Message();
		message.setId(resultSet.getInt("id"));
		message.setUserId(resultSet.getInt("user_id"));
		message.setText(resultSet.getString("text"));
		message.setCreatedAt(resultSet.getTimestamp("created_at"));
		message.setUpdatedAt(resultSet.getTimestamp("updated_at"));
		return message;
	}

	public List<Message> findAll() throws SQLException {
		List<Message> list = new ArrayList<Message>();

		String sql = "SELECT * FROM messages";
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			Message message = createMessage(resultSet);
			list.add(message);
		}
		resultSet.close();
		statement.close();

		return list;
	}

	public Message find(int id) throws SQLException {
		Message message = null;

		String sql = "SELECT * FROM messages WHERE user_id = ?";
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setInt(1, id);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			message = createMessage(resultSet);
		}
		resultSet.close();
		statement.close();

		return message;
	}
}
