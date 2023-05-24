package hom.cluster.auth.config;

import hom.cluster.auth.component.AuthFailureHandler;
import hom.cluster.auth.component.AuthSuccessHandler;
import hom.cluster.auth.component.LogoutHandler;
import hom.cluster.auth.component.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author visy.wang
 * @description: Spring Security 核心配置
 * @date 2023/5/23 17:51
 */
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthSuccessHandler successHandler;
    @Autowired
    private AuthFailureHandler failureHandler;
    @Autowired
    private LogoutHandler logoutHandler;
    @Autowired
    private SecurityUserDetailsService userDetailsService;

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //禁用跨域限制
                .formLogin() //表单登录配置
                .loginProcessingUrl("/login").permitAll()
                .successHandler(successHandler).permitAll()
                .failureHandler(failureHandler).permitAll()
                .and()
                .logout() //退出配置
                .logoutSuccessHandler(logoutHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll();
    }
}
