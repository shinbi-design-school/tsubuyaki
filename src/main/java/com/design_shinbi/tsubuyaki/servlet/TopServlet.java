package com.design_shinbi.tsubuyaki.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.design_shinbi.tsubuyaki.model.Const;
import com.design_shinbi.tsubuyaki.model.entity.User;

@WebServlet("/top")
public class TopServlet extends HttpServlet {
	@Override
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute(Const.LOGIN_USER_KEY);

		String jsp = null;
		if (user == null) {
			jsp = "/WEB-INF/jsp/login.jsp";
		} else {
			jsp = "/WEB-INF/jsp/loginTest.jsp";
		}

		try {
			RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
