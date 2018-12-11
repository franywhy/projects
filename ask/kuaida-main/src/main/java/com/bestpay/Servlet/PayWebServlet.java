package com.bestpay.Servlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bestpay.util.WebCheckUtil;
public class PayWebServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //String ipRemote=WebCheckUtil.getIpRemote(request);
        String ipRemote="116.228.55.237";
        request.setAttribute("ipRemote",ipRemote);
        String index=request.getParameter("index");
        if(index.equals("1"))
        {
           request.getRequestDispatcher("test2.jsp").forward(request, response);
        }
        else if(index.equals("2"))
        {
		   request.getRequestDispatcher("testdirect2.jsp").forward(request, response);
        }
        else if(index.equals("3"))
        {
        	request.getRequestDispatcher("telerefundtest2.jsp").forward(request, response);
        }
	}
}
