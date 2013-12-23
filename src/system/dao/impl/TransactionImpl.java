package system.dao.impl;


import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import system.dao.spec.ResourceHolder;
import system.dao.spec.Transaction;
import system.util.PropertyManager;

@Stateful
@RequestScoped
public class TransactionImpl implements Transaction, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7828715984476316619L;

	private static final String YOUR_STM_ID = "stms130802";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	private ResourceHolder resourceHolder;
	private Connection connection;


	@Override
	public void begin() throws TransactionException {
		if(connection == null){
			try {

				Properties property = PropertyManager.getApplicationProperties();

				if(property.getProperty("db").equals("orcl"))
					// This usage is irregular. Only on the STM. Normaly use
					// 'getConnection()'.
					connection = resourceHolder.getResource().getConnection(
							YOUR_STM_ID, YOUR_STM_ID);
				else if(property.getProperty("db").equals("derby"))
					connection = resourceHolder.getResource().getConnection();
				connection.setAutoCommit(false);
			} catch (IOException e) {
				throw new TransactionException(e);
			} catch (SQLException e) {
				throw new TransactionException(e);
			}
		}
	}

	@Override
	public void commit() throws TransactionException {
		try {
			if(connection != null){
				connection.commit();
			}
		} catch (SQLException e) {
			throw new TransactionException(e);
		}
	}

	@Override
	public void rollback() throws TransactionException {
		try {
			if(connection != null){
				connection.rollback();
			}
		} catch (SQLException e) {
			throw new TransactionException(e);
		}
	}

	@Override
	public boolean isActive() throws TransactionException {
		try {
			if (connection != null && connection.isClosed()) {
				connection = null;
				return false;
			}
			if(connection == null)
				return false;
		} catch (SQLException e) {
			throw new TransactionException(e);
		}

		return true;
	}

	@PreDestroy
	public void preDestory(){
		if(connection != null){
			try {
				logger.warn("transaction destory");
				connection.close();
			} catch (SQLException e) {
				logger.warn(e.getMessage(), e);
			}
			connection = null;
		}
	}

	@Override
	public <T> T getResource(Class<T> klass) {
		if (klass.equals(Connection.class)) {
			@SuppressWarnings("unchecked")
			T con = (T)connection;

			return con;
		}

		throw new IllegalArgumentException();
	}

}
