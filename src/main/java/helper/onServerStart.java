package helper;

import DAO.ConnectionPool;
import helper.polling.MetricCollector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class onServerStart extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();

        System.out.println("Started");
        ConnectionPool.createConnectionPool(5);
        MetricCollector.startPolling();
    }
}
