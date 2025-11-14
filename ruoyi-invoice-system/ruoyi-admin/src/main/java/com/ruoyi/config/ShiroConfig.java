package com.ruoyi.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shiro配置类
 * 
 * @author ruoyi
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置Realm
     */
    @Bean
    public Realm realm() {
        // 使用简单的IniRealm作为演示，实际项目中应该使用数据库Realm
        IniRealm realm = new IniRealm();
        realm.setResourcePath("classpath:shiro.ini");
        return realm;
    }


    /**
     * 配置Shiro过滤器链
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        
        // 允许匿名访问登录接口
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/logout", "anon");
        
        // 静态资源允许匿名访问
        chainDefinition.addPathDefinition("/css/**", "anon");
        chainDefinition.addPathDefinition("/js/**", "anon");
        chainDefinition.addPathDefinition("/images/**", "anon");
        
        // 其他所有请求都需要认证
        chainDefinition.addPathDefinition("/**", "authc");
        
        return chainDefinition;
    }
}
