package com.fenoreste.util;



import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class SpringConfiguration {
	/*
	FicheroConexion fichero= new FicheroConexion();
	

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSourceHikari;
	
	@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://"+fichero.getHost()+":5432/"+fichero.getDatabase());
        dataSource.setUsername("saicoop");
        dataSource.setPassword("slufpana?");  
        return dataSource;*/
		
		//Propiedades de conexione
	    /*config.setJdbcUrl("jdbc:postgresql://"+fichero.getHost()+":5432/"+fichero.getDatabase());
	    config.setUsername("saicoop");
	    config.setPassword("slufpana?");
	    config.addDataSourceProperty("cachePrepStmts", "true");
	    config.addDataSourceProperty("prepStmtCacheSize", "250");
	    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");	    
	    dataSourceHikari = new HikariDataSource(config);	        
	    //pool de conexion
		dataSourceHikari = new HikariDataSource();
	    dataSourceHikari.setJdbcUrl("jdbc:postgresql://"+fichero.getHost().trim()+":5432/"+fichero.getDatabase().trim());
	    dataSourceHikari.setUsername("saicoop");
	    dataSourceHikari.setPassword("slufpana?");
	    dataSourceHikari.setMaximumPoolSize(2);
	    dataSourceHikari.setMinimumIdle(0);
	    dataSourceHikari.setIdleTimeout(10000);
	    dataSourceHikari.setMaxLifetime(10000);
	    return dataSourceHikari;
	    }
	    
	    public static Connection getConnection() throws SQLException {
	        return dataSourceHikari.getConnection();
	    }
	   
		return null;
	} */
}
