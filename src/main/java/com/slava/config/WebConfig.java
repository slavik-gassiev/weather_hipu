package com.slava.config;

import com.slava.model.crossing_weather.VCWeather;
import com.slava.model.open_weather.Weather;
import com.slava.model.weatherApi.WAWeather;
import com.slava.repositories.IWeatherRepository;
import com.slava.repositories.VCWeatherRepository;
import com.slava.repositories.WeatherAPIRepository;
import com.slava.repositories.WeatherRepository;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@EnableWebMvc
@Configuration
@ComponentScan("com.slava")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.slava")
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    private final Environment env;

    @Autowired
    public WebConfig(ApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        this.env = env;
    }

    @Bean
    public Map<String, IWeatherRepository<Weather>> weatherRepositories(WeatherRepository weatherRepository) {
        return Map.of(
                "WeatherRepository", weatherRepository
        );
    }

    @Bean
    public Map<String, IWeatherRepository<WAWeather>> weatherAPIRepository(WeatherAPIRepository weatherAPIRepository) {
        return Map.of(
                "WeatherAPIRepository", weatherAPIRepository
        );
    }

    @Bean
    public Map<String, IWeatherRepository<VCWeather>> weatherVCWeatherRepository(VCWeatherRepository vcWeatherRepository) {
        return Map.of(
                "VCWeatherRepository", vcWeatherRepository
        );
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 секунд
        factory.setReadTimeout(5000);    // 5 секунд
        return new RestTemplate(factory);
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();

        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");

        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        registry.viewResolver(resolver);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("hibernate.driver"));
        dataSource.setUrl(env.getProperty("hibernate.url"));
        dataSource.setUsername(env.getProperty("hibernate.username"));
        dataSource.setPassword(env.getProperty("hibernate.password"));
        return dataSource;
    }

    @Configuration
    public class FlywayConfig {

        @Bean(initMethod = "migrate")
        public Flyway flyway(DataSource dataSource) {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .validateOnMigrate(false)
                    .load();
            return flyway;
        }
    }

    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.slava.entities");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(hibernateProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
}
