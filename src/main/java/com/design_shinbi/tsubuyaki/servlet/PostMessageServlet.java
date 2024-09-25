package com.design_shinbi.tsubuyaki.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.design_shinbi.tsubuyaki.model.Const;
import com.design_shinbi.tsubuyaki.model.PostInfo;
import com.design_shinbi.tsubuyaki.model.dao.MessageDAO;
import com.design_shinbi.tsubuyaki.model.entity.Image;
import com.design_shinbi.tsubuyaki.model.entity.Message;
import com.design_shinbi.tsubuyaki.model.entity.User;
import com.design_shinbi.tsubuyaki.util.DbUtil;

@WebServlet("/postMessage")
@MultipartConfig
public class PostMessageServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute(Const.LOGIN_USER_KEY);

		String jsp = null;

		if (loginUser == null) {
			jsp = "/WEB-INF/jsp/login.jsp";
		} else {
			try {
				Connection connection = DbUtil.connect();
				MessageDAO dao = new MessageDAO(connection);
				String error = "";

				int messageId = 0;
				try {
					messageId = Integer.parseInt(request.getParameter("id"));
				} catch (Exception e) {
				}

				String text = request.getParameter("text");
				if (text == null || text.isEmpty()) {
					error = "テキストを入力してください。";
				}

				if (error.isEmpty()) {
					if (messageId > 0) {
						Message message = dao.find(messageId);
						message.setText(text);
						dao.update(message);
					} else {
						dao.add(loginUser.getId(), text);
						messageId = dao.getMaxId();
					}

					this.setImage(request, dao, messageId);
				}

				List<PostInfo> posts = dao.getPosts();

				request.setAttribute("posts", posts);
				request.setAttribute("error", error);

				jsp = "/WEB-INF/jsp/top.jsp";

				connection.close();
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}

	private void setImage(
			HttpServletRequest request,
			MessageDAO dao,
			int messageId) throws Exception {
		Part part = request.getPart("image_file");
		String fileName = part.getSubmittedFileName();
		boolean deleteFlag = Boolean.parseBoolean(request.getParameter("delete_image_flag"));

		if (fileName == null || fileName.isEmpty()) {
			if (deleteFlag) {
				dao.deleteImage(messageId);
			}
		} else {
			Image image = new Image(fileName, part.getInputStream());
			dao.setImage(messageId, image);
		}
	}
}
