package com.demo.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yangjie.Chen
 * @description 认证服务器 继承 WebSecurityConfigurerAdapter
 * @date 2020/3/13
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig  extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    // 注入认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    // 注入用户认证信息查询接口
    @Autowired
    public UserDetailsService userService;

    // 注入 redisTokenStore 管理 token
//    @Autowired
//    private TokenStore redisTokenStore;

    // 注入 jwtToken 转换器
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    // 注入 jwtTokenStore 用于管理 token
    @Autowired
    private TokenStore jwtTokenStore;

    @Autowired
    private TokenEnhancer jwtTokenEnhancer;

    // 密码加密、校验器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    // 客户端认证配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
//        clients.inMemory()
//                .withClient("test")
//                .secret(passwordEncoder().encode("test_123456"))
//                .accessTokenValiditySeconds(3600)
//                .refreshTokenValiditySeconds(864000)
//                .scopes("all")
//                .authorizedGrantTypes("password");
    }

    // 配置认证端点
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(jwtTokenEnhancer);
        enhancers.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancers);

        endpoints
                // 配置 password 方式获取 token 令牌，则需要添加认证管理器以及用户查询接口
                .authenticationManager(authenticationManager)
                .userDetailsService(userService)
                // 使用 redis 来存放token
//                .tokenStore(redisTokenStore);
                // 使用 jwtToken 方式管理 token
                .tokenStore(jwtTokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenEnhancer(enhancerChain);;
    }
}
