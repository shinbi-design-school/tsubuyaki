package com.design_shinbi.tsubuyaki.model.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.design_shinbi.tsubuyaki.model.entity.User;

public class UserDAO {
	protected Connection connection;

	public UserDAO(Connection connection) {
		this.connection = connection;
	}

	public static String createHash(String password) throws NoSuchAlgorithmException {
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		String string = password;

		byte[] bytes = sha256.digest(string.getBytes());
		String hash = String.format(
				"%040x",
				new BigInteger(1, bytes));

		return hash;
	}

	public void add(String name, String email, String password, boolean isAdmin)
			throws NoSuchAlgorithmException, SQLException {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String hash = createHash(password);

		String sql = "INSERT INTO users (name, email, password, "
				+ "is_admin, created_at, updated_at) "
				+ "values (?, ?, ?, ?, ?, ?)";

		PreparedStatement statement = this.connection.prepareStatement(sql);

		statement.setString(1, name);
		statement.setString(2, email);
		statement.setString(3, hash);
		statement.setBoolean(4, isAdmin);
		statement.setTimestamp(5, now);
		statement.setTimestamp(6, now);

		statement.executeUpdate();
		statement.close();
	}

	public void update(User user) throws SQLException {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		String sql = "UPDATE users SET name = ?, email = ?, is_admin = ?, "
				+ "updated_at = ? WHERE id = ?";

		PreparedStatement statement = this.connection.prepareStatement(sql);

		statement.setString(1, user.getName());
		statement.setString(2, user.getEmail());
		statement.setBoolean(3, user.isAdmin());
		statement.setTimestamp(4, now);
		statement.setInt(5, user.getId());

		statement.executeUpdate();
		statement.close();
	}

	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM users WHERE id = ?";

		PreparedStatement statement = this.connection.prepareStatement(sql);

		statement.setInt(1, id);

		statement.executeUpdate();
		statement.close();
	}

	public List<User> findAll() throws SQLException {
		List<User> list = new ArrayList<User>();

		String sql = "SELECT * FROM users";
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			User user = new User();
			user.setId(resultSet.getInt("id"));
			user.setName(resultSet.getString("name"));
			user.setEmail(resultSet.getString("email"));
			user.setPassword(resultSet.getString("password"));
			user.setAdmin(resultSet.getBoolean("is_admin"));
			user.setCreatedAt(resultSet.getTimestamp("created_at"));
			user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
			list.add(user);
		}
		resultSet.close();
		statement.close();

		return list;
	}
}
