package com.rose.conf;

import com.ribbon.configuration.RibbonRoleConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClients(defaultConfiguration = RibbonRoleConfiguration.class)
public class RibbonConfiguration {

}