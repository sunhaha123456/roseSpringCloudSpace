package com.ribbon.configuration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.ribbon.role.NacosWeightedRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonRoleConfiguration {

//    @Bean
//    public IRule randomRule() {
//        return new RandomRule();
//    }

    @Bean
    public IRule nacosWeightedRule() {
        return new NacosWeightedRule();
    }
}