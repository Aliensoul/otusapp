package arch.homework.orders.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@Primary
public class PgConfig {


    public static final String DATA_SOURCE_PROPERTIES = "DataSourceProperties";
    public static final String DATA_SOURCE = "DataSource";
    public static final String JDBC_TEMPLATE = "JdbcTemplate";
    public static final String TX_MANAGER = "TxManager";
    public static final String NAMED_PARAMETER_JDBC_TEMPLATE = "NamedParameterJdbcTemplate";

    private static final String PREFIX = "pg";

    private static final String PROPERTY_PREFIX = "datasource." + PREFIX;
    private static final String PROPERTY_HIKARI_PREFIX = PROPERTY_PREFIX + ".hikari";

    public static final String DATA_SOURCE_PROPERTIES_BEAN = PREFIX + DATA_SOURCE_PROPERTIES;
    public static final String DATA_SOURCE_BEAN = PREFIX + DATA_SOURCE;
    public static final String JDBC_TEMPLATE_BEAN = PREFIX + JDBC_TEMPLATE;
    public static final String TX_MANAGER_BEAN = PREFIX + TX_MANAGER;
    public static final String NAMED_PARAMETER_BEAN = PREFIX + NAMED_PARAMETER_JDBC_TEMPLATE;

    @Configuration(DATA_SOURCE_PROPERTIES_BEAN)
    @ConfigurationProperties(PROPERTY_PREFIX)
    @Primary
    public static class PgDataSourceProperties extends DataSourceProperties {
    }

    @Bean(DATA_SOURCE_BEAN)
    @ConfigurationProperties(PROPERTY_HIKARI_PREFIX)
    public DataSource pgDataSource(@Qualifier(DATA_SOURCE_PROPERTIES_BEAN) DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(JDBC_TEMPLATE_BEAN)
    public JdbcTemplate pgJdbcTemplate(@Qualifier(DATA_SOURCE_BEAN) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(NAMED_PARAMETER_BEAN)
    public NamedParameterJdbcTemplate pgNamedParameterJdbcTemplate(@Qualifier(DATA_SOURCE_BEAN) DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(TX_MANAGER_BEAN)
    public DataSourceTransactionManager pgTransactionManager(@Qualifier(DATA_SOURCE_BEAN) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
