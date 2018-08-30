package com.equator.config.oauth2;

import com.equator.config.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * 对象和方法的意思可参考
 * OAuth 2 Developers Guide：http://projects.spring.io/spring-security-oauth/docs/oauth2.html
 * 译：https://www.cnblogs.com/xingxueliao/p/5911292.html
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

//            @Autowired
//            RedisConnectionFactory redisConnectionFactory; //如果要关机后还可以使用，应保存

    /**
     * Configure the security of the Authorization Server, which means in practical terms the /oauth/token endpoint.
     * The /oauth/authorize endpoint also needs to be secure,
     * but that is a normal user-facing endpoint and should be secured the same way as the rest of your UI,
     * so is not covered here. The default settings cover the most common requirements,
     * following recommendations from the OAuth2 spec, so you don't need to do anything here to get a basic server up and running.
     * 配置授权服务器的安全性，这意味着实际的/oauth/token端点。
     * /oauth/authorize端点也需要是安全的，但是这是一个普通的面向用户的端点，应该与UI的其他部分以同样的方式进行保护，因此这里不进行介绍。
     * 默认设置包含最常见的需求，遵循OAuth2规范的建议，因此您不需要在这里做任何事情来启动和运行一个基本的服务器。
     * @param oauthServer 用来配置令牌端点(Token Endpoint)的安全约束.
     * @throws Exception
     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
////        //允许表单认证
////        oauthServer.allowFormAuthenticationForClients();

//            //使用授权服务的 /oauth/check_token 端点你需要将这个端点暴露出去，以便资源服务可以进行访问，这在咱们授权服务配置中已经提到了，下面是一个例子：
//            //在这个例子中，我们配置了 /oauth/check_token 和 /oauth/token_key 这两个端点（受信任的资源服务能够获取到公有密匙，这是为了验证JWT令牌）。
//            //这两个端点使用了HTTP Basic Authentication 即HTTP基本身份验证，使用 client_credentials 授权模式可以做到这一点。
//            oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
//                    .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
//    }

    /**
     *Configure the ClientDetailsService, e.g. declaring individual clients and their properties.
     * Note that password grant is not enabled (even if some clients are allowed it) unless an AuthenticationManager is supplied to the configure(AuthorizationServerEndpointsConfigurer).
     * At least one client, or a fully formed custom ClientDetailsService must be declared or the server will not start.
     * 配置ClientDetailsService，例如声明单个客户端及其属性。
     * 注意，除非向configure(AuthorizationServerEndpointsConfigurer)提供了AuthenticationManager，否则不会启用密码授予(即使某些客户端允许这样做)。
     * 必须声明至少一个客户端或一个完整的定制ClientDetailsService，否则服务器将不会启动。
     * @param clients 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
     *                你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.
                //这里使用内存，也可以改用JDBC
                inMemory()
                    //（必须的）用来标识客户的Id
                    .withClient("client_2")
                    //（需要值得信任的客户端）客户端安全码，如果有的话。
                    .secret("{bcrypt}$2a$10$MsQgI3quhPRF02OrhP8hbO9cbl1rNDI86QCWWw44/ezMeWYAyUWTW") //123456
                    //用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
                    .scopes("ALL")
                    //此客户端可以使用的授权类型，默认为空
                    .authorizedGrantTypes("password", "refresh_token")
                    //此客户端可以使用的权限（基于Spring Security authorities）
                    //[应该和用户的权限不同，应如何应用？在AuthorizationServerSecurityConfigurer配置中？???????????????]
                    .authorities("client")
                    //
                    .resourceIds("travel");
    }

    /**
     * Configure the non-security features of the Authorization Server endpoints, like token store,
     * token customizations, user approvals and grant types.
     * You shouldn't need to do anything by default, unless you need password grants, in which case you need to provide an AuthenticationManager.
     * 配置授权服务器端点的非安全性特性，如令牌存储、令牌定制、用户批准和授予类型。
     * 您不应该在默认情况下执行任何操作，除非您需要密码授予，在这种情况下，您需要提供一个AuthenticationManager。
     * @param endpoints 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//            endpoints.tokenStore(tokenStore())
//                    .accessTokenConverter(accessTokenConverter())
////                        .tokenStore(new RedisTokenStore(redisConnectionFactory))
//                    .authenticationManager(authenticationManager);


        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), accessTokenConverter()));

        endpoints.tokenStore(tokenStore()) //TODO 如何撤消JWT？ 1.通常被授予短期有效期限，然后在更新令牌中进行撤销 2.标识已经被撤消？
                .tokenEnhancer(tokenEnhancerChain)
                //如果您注入一个UserDetailsService，或者如果一个是全局配置的（例如在GlobalAuthenticationManagerConfigurer中），
                // 那么refresh token grant将包含对用户详细信息的检查，以确保账户仍然处于活动状态。
                .userDetailsService(userDetailsService) //不配這個刷新令牌會報錯
                //认证管理器，当你选择了资源所有者密码（password）授权类型的时候，请设置这个属性注入一个 AuthenticationManager 对象。
                .authenticationManager(authenticationManager);

                //.tokenGranter() //设置了，那么授权将会交由你来完全掌。控标准的四种授权模式已经满足不了你的需求的时候，才会考虑使用这个

        //自定义URL。默认如下：
        //        /oauth/authorize：授权端点。
        //        /oauth/token：令牌端点。
        //        /oauth/confirm_access：用户确认授权提交端点。
        //        /oauth/error：授权服务错误信息端点。
        //        /oauth/check_token：用于资源服务访问的令牌解析端点。
        //        /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
        //endpoints.pathMapping(defaultPath, customPath)
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123"); //TODO 待优化
        return converter;
    }




}
