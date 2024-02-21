package store.mybooks.resource.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * packageName    : store.mybooks.resource.config
 * fileName       : DatabaseConfig
 * author         : Fiat_lux
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        Fiat_lux       최초 생성
 */
@Configuration
public class DatabaseConfig {
    private final KeyConfig keyConfig;
    private final DatabaseProperties databaseProperties;

    @Autowired
    public DatabaseConfig(KeyConfig keyConfig, DatabaseProperties databaseProperties) {
        this.keyConfig = keyConfig;
        this.databaseProperties = databaseProperties;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(keyConfig.keyStore(databaseProperties.getUrl()));
        dataSource.setUsername(keyConfig.keyStore(databaseProperties.getUserName()));
        dataSource.setPassword(keyConfig.keyStore(databaseProperties.getPassword()));

        dataSource.setInitialSize(databaseProperties.getInitialSize());
        dataSource.setMaxTotal(databaseProperties.getMaxTotal());
        dataSource.setMinIdle(databaseProperties.getMinIdle());
        dataSource.setMaxIdle(databaseProperties.getMaxIdle());

        dataSource.setMaxWaitMillis(databaseProperties.getMaxWait());

        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("store.mybooks.resource");
        emf.setJpaVendorAdapter(jpaVendorAdapters());
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    private JpaVendorAdapter jpaVendorAdapters() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }

    private Properties jpaProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.show_sql", "false");
        jpaProperties.setProperty("hibernate.format_sql", "true");
        jpaProperties.setProperty("hibernate.use_sql_comments", "true");
        jpaProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        jpaProperties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

        return jpaProperties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

}