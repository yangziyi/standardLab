package lab.base.config;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;


@org.springframework.context.annotation.Configuration
public class mybatisConfig {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        //解决mybatis查询结果为null的值不显示问题
        configuration.setCallSettersOnNulls(true);
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

}
