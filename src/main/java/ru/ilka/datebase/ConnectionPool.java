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
 * ConnectionPool class controls connections usage and provides connections to the application.
 * @see ProxyConnection
 * @see DataBaseInitializer
 * @since %G%
 * @version %I%
 */
public class ConnectionPool {
    static Logger logger = LogManager.getLogger(ConnectionPool.class);

    private static AtomicBoolean poolInstanceCreated = new AtomicBoolean(false);
    private static AtomicBoolean poolClosed = new AtomicBoolean(false);
    private static Lock connectionPoolLock = new ReentrantLock(true);
    private static Lock closePoolLock = new ReentrantLock(true);
    private static ConnectionPool poolInstance;
    private static int connectionAmount;

    private BlockingQueue<ProxyConnection> freeConnections;
    private BlockingQueue<ProxyConnection> takenConnections;

    /**
     * Registers database driver and initialize connections.
     */
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
                logger.error("Can not get connection " + e);
            }
        }

        if (connectionAmount < 1) {
            logger.fatal("Can not initialize connections. No connections are available.");
            throw new RuntimeException("Error while initializing connections. No connections are available.");
        }
        logger.info("init " + connectionAmount + " connections where taken.");

        /* check if all connections were initialized */
        if (connectionAmount < dbInitializer.POOL_SIZE) {
            logger.error("Can not initialize expected amount of connections.");
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

    /**
     * Ensures that the class has only one instance and provides a global access point to it.
     * @return connection pool instance
     */
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

    /**
     * Provides connection, wait for the freeConnections queue to become
     * non-empty when retrieving an element, and wait for space to become available
     * in the takenConnections queue when storing an element.
     * @return proxy connection
     */
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

    /**
     * Free proxy connection
     * @param connection proxy connection without work
     */
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

    /**
     * Closes all connections and sets connection pool closed flag.
     * @throws ConnPoolException if other thread has already called closePool().
     */
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
