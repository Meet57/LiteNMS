package helper;

import DAO.ConnectionPool;
import helper.polling.MetricCollector;
import services.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Init extends HttpServlet {



    @Override
    public void init() throws ServletException {

        super.init();

        ConnectionPool.createConnectionPool(Constants.DB_POOL_SIZE);

        MetricCollector.startPolling();

        CacheData.loadMonitorDevices();

    }
}
