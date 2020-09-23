/*
 * package com.config;
 * 
 * 
 * import lombok.extern.slf4j.Slf4j ; import javax.servlet.ServletContextEvent;
 * import javax.servlet.ServletContextListener; import
 * javax.servlet.annotation.WebListener; import
 * java.lang.reflect.InvocationTargetException; import java.lang.reflect.Method;
 * import java.sql.Driver; import java.sql.DriverManager; import
 * java.sql.SQLException; import java.util.Enumeration;
 * 
 * @WebListener
 * 
 * @ Slf4j public class JdbcUnregisterListener implements ServletContextListener
 * {
 * 
 * @Override public void contextDestroyed(ServletContextEvent sce) { try {
 * log.info("Calling MySQL AbandonedConnectionCleanupThread checkedShutdown");
 * // Or com.mysql.jdbc.AbandonedConnectionCleanupThread Class cls =
 * Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread"); Method
 * method = cls.getMethod("checkedShutdown"); method.invoke(null); } catch
 * (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
 * InvocationTargetException e) { log.
 * error("Cannot call MySQL AbandonedConnectionCleanupThread.checkedShutdown!",
 * e); } // Now deregister JDBC drivers in this context's ClassLoader: // Get
 * the webapp's ClassLoader ClassLoader cl =
 * Thread.currentThread().getContextClassLoader(); // Loop through all drivers
 * Enumeration<Driver> drivers = DriverManager.getDrivers(); while
 * (drivers.hasMoreElements()) { Driver driver = drivers.nextElement(); if
 * (driver.getClass().getClassLoader() == cl) { // This driver was registered by
 * the webapp's ClassLoader, so deregister it: try {
 * log.info("Deregistering JDBC driver {}", driver);
 * DriverManager.deregisterDriver(driver); } catch (SQLException ex) {
 * log.error("Error deregistering JDBC driver {}", driver, ex); } } else { //
 * driver was not registered by the webapp's ClassLoader and may be in use
 * elsewhere log.
 * trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader"
 * , driver); } } } }
 */

/*
 * package com.config;
 * 
 * import lombok.SneakyThrows; import lombok.extern.slf4j.Slf4j; import
 * oracle.jdbc.OracleDriver;
 * 
 * import javax.servlet.ServletContextEvent; import
 * javax.servlet.ServletContextListener; import
 * javax.servlet.annotation.WebListener; import
 * java.lang.reflect.InvocationTargetException; import java.lang.reflect.Method;
 * import java.sql.SQLException; import java.util.Enumeration; import
 * java.sql.DriverManager; import java.sql.Driver; import
 * java.sql.DriverManager; import java.sql.SQLException; import
 * java.util.Enumeration;
 * 
 * @WebListener
 * 
 * @Slf4j public class JdbcUnregisterListener implements ServletContextListener
 * {
 * 
 * @SneakyThrows
 * 
 * @Override public void contextDestroyed(ServletContextEvent sce) { try {
 * log.info("Calling MySQL AbandonedConnectionCleanupThread checkedShutdown");
 * // Or com.mysql.jdbc.AbandonedConnectionCleanupThread Class cls =
 * Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread"); Method
 * method = cls.getMethod("checkedShutdown"); method.invoke(null); } catch
 * (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
 * InvocationTargetException e) { log.
 * error("Cannot call MySQL AbandonedConnectionCleanupThread.checkedShutdown!",
 * e); }
 * 
 * // Now deregister JDBC drivers in this context's ClassLoader: // Get the
 * webapp's ClassLoader ClassLoader cl =
 * Thread.currentThread().getContextClassLoader(); // Loop through all drivers
 * Enumeration<OracleDriver> drivers = (Enumeration<OracleDriver>)
 * DriverManager.getDriver(
 * "jdbc:oracle:thin:@ica-uat-subway-rds.c1qagayt83hu.eu-west-1.rds.amazonaws.com:1521:thorevo"
 * ); while (drivers.hasMoreElements()) { OracleDriver driver =
 * drivers.nextElement(); if (driver.getClass().getClassLoader() == cl) { //
 * This driver was registered by the webapp's ClassLoader, so deregister it: try
 * { log.info("Deregistering JDBC driver {}", driver);
 * DriverManager.deregisterDriver(driver); } catch (SQLException ex) {
 * log.error("Error deregistering JDBC driver {}", driver, ex); } } else { //
 * driver was not registered by the webapp's ClassLoader and may be in use
 * elsewhere log.
 * trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader"
 * , driver); } } } }
 */
package com.example.config;
import lombok.SneakyThrows; import lombok.extern.slf4j.Slf4j; import
oracle.jdbc.OracleDriver;
 
import javax.servlet.ServletContextEvent; 
import
javax.servlet.ServletContextListener;
import
 javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import
java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
 import java.sql.SQLException; 
 import java.util.Enumeration;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import
 java.sql.DriverManager;
 import java.sql.Driver;
 import java.sql.DriverManager;
 import java.sql.SQLException; 
 import java.util.Enumeration;
 //import com.mysql.jdbc.AbandonedConnectionCleanupThread;


@WebListener
public class JdbcUnregisterListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUnregisterListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("######### contextInitialized #########");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)  {
        logger.info("######### contextDestroyed #########");
       
        // ... First close any background tasks which may be using the DB ...
        // ... Then close any DB connection pools ...

        // Now deregister JDBC drivers in this context's ClassLoader:
        // Get the webapp's ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        
        
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
            try {
                DriverManager.deregisterDriver(driver);
                logger.info(String.format("deregistering the jdbc driver: %s", driver));
            } catch (SQLException e) {
                logger.info(String.format("Error deregistering driver %s", driver), e);
            }
            }
            
            else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
            	logger.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader", driver);
            }

        }
		
		  try { Thread.sleep(2000L); } catch (Exception e) {}
		  AbandonedConnectionCleanupThread.checkedShutdown();
		 
        
        logger.info("Entered here into the context closed event");
    	
		/*
		 * ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
		 * threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS);
		 * threadPoolExecutor.shutdown();
		 */
    }

}


