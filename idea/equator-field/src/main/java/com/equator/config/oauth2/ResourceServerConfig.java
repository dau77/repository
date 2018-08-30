package com.equator.config.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "travel";

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config
                //这个资源服务的ID，这个属性是可选的，但是推荐设置并在授权服务中进行验证。      !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                .resourceId(RESOURCE_ID) // 用于分配给可授予的clientId
                .stateless(true)  //  标记以指示在这些资源上仅允许基于令牌的身份验证???????
                //ResourceServerTokenServices 类的实例，用来实现令牌服务。
                .tokenServices(tokenServices());

        //        其他的拓展属性例如 tokenExtractor 令牌提取器用来提取请求中的令牌。
        //        请求匹配器，用来设置需要进行保护的资源路径，默认的情况下是受保护资源服务的全部路径。
        //        受保护资源的访问规则，默认的规则是简单的身份验证（plain authenticated）。
        //        其他的自定义权限保护规则通过 HttpSecurity 来进行配置。
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/oauth/*","/static/**").permitAll()
//                .anyRequest().authenticated(); //任何请求,登录后可以访问
        http.authorizeRequests().anyRequest().permitAll();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }
}
