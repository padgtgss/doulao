package common.var.listener;

import common.var.constants.SystemConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * @Description: InitConstListener
 * @anthor: shi_lin
 * @CreateTime: 2015-12-10
 */

public class InitConstListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Properties prop = new Properties();
        try {
            prop.load(InitConstListener.class.getClassLoader().getResourceAsStream("mongodb-config.properties"));
            SystemConfig.MONGO_HOST = prop.getProperty("MONGO_HOST");
            SystemConfig.MONGO_PORT =Integer.parseInt( prop.getProperty("MONGO_PORT"));
            SystemConfig.MONGO_DBNAME = prop.getProperty("MONGO_DBNAME");
            SystemConfig.MONGO_USERNAME = prop.getProperty("MONGO_USERNAME");
            SystemConfig.MONGO_PASSWORD = prop.getProperty("MONGO_PASSWORD");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
