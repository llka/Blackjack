package ru.ilka.datebase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.exception.ConnPoolException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Here could be your advertisement +375 29 3880490
 */
public class ConnectionPool {
    static Logger logger = LogManager.getLogger(ConnectionPool.class);

    private static AtomicBoolean poolInstanceCreated = new AtomicBoolean(false);
    private static AtomicBoolean poolClosed = new AtomicBoolean(false);
    private static Lock connectionPoolLock = new ReentrantLock(true);
    private static Lock closePoolLock = new ReentrantLock(true);
    private  static ConnectionPool poolInstance;
    private static int connectionAmount;

    private BlockingQueue<ProxyConnection> freeConnections;
    private BlockingQueue<ProxyConnection> takenConnections;

    private ConnectionPool() {
        DataBaseInitializer dbInitializer = new DataBaseInitializer();

        freeConnections = new ArrayBlockingQueue<>(dbInitializer.POOL_SIZE);
        takenConnections = new ArrayBlockingQueue<>(dbInitializer.POOL_SIZE);

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            logger.fatal("Registration database driver error" + e);
            throw new RuntimeException("Error while initializing Mysql Driver " + e);
        }
        for (int i = 0; i < dbInitializer.POOL_SIZE; i++) {
            try {
                Connection connection = DriverManager.getConnection(dbInitializer.URL, dbInitializer.LOGIN, dbInitializer.PASSWORD);
                freeConnections.put(new ProxyConnection(connection));
                ++connectionAmount;
            }catch (SQLException | InterruptedException e) {
                logger.error("Can't get connection " + e);
            }
        }

        if (connectionAmount < 1) {
            logger.fatal("Can't initialize connections. No connections are available.");
            throw new RuntimeException("Error while initializing connections. No connections are available.");
        }
        logger.info("init " + connectionAmount + " connections where taken.");

        /* check if all connections were initialized */
        if (connectionAmount < dbInitializer.POOL_SIZE) {
            logger.error("Can't initialize expected amount of connections.");
            try {
                while (connectionAmount < dbInitializer.POOL_SIZE){
                    Connection connection = DriverManager.getConnection(dbInitializer.URL, dbInitializer.LOGIN, dbInitializer.PASSWORD);
                    freeConnections.put(new ProxyConnection(connection));
                    ++connectionAmount;
                }
            } catch (SQLException | InterruptedException e) {
                logger.error("Can't get connection " + e);
            }
            logger.info("init " + connectionAmount+ " connections where taken additionally.");
        }
    }

    public static ConnectionPool getInstance() {
        if (!poolInstanceCreated.get()) {
            connectionPoolLock.lock();
            try {
                if (poolInstance == null) {
                    poolInstance = new ConnectionPool();
                    poolInstanceCreated.set(true);
                }
            } finally {
                connectionPoolLock.unlock();
            }
        }
        return poolInstance;
    }

    public Connection getConnection(){
        ProxyConnection connection = null;
        if (!poolClosed.get()) {
            try {
                connection = freeConnections.take();
                takenConnections.put(connection);
            } catch (InterruptedException e) {
                logger.error("Can't take connection from available connections." + e);
            }
        }
        return connection;
    }

    void freeConnection(ProxyConnection connection) {
        if (!poolClosed.get()) {
            takenConnections.remove(connection);
            try {
                freeConnections.put(connection);
            } catch (InterruptedException e) {
                logger.error("Can't free connection " + e);
            }
        }
    }

    public void closePool() throws ConnPoolException {
        if (!poolClosed.get()) {
            closePoolLock.lock();
            try {
                poolClosed.set(true);
                for (int i = 0; i < connectionAmount; ++i) {
                    freeConnections.take().finalClose();
                }
            } catch (SQLException | InterruptedException e) {
                logger.error("Can't close connection pool." + e);
            } finally {
                closePoolLock.unlock();
            }
        }
        else {
            throw new ConnPoolException("Other thread has already called closePool()");
        }
    }
}
