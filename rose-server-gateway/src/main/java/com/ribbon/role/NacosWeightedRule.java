package com.ribbon.role;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // 读取配置文件，并初始化NacosWeightedRule
    }

    @Override
    public Server choose(Object key) {
        try {
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
//        log.info("lb = {}", loadBalancer);

            // 想要请求的微服务的名称
            String name = loadBalancer.getName();

            // 拿到服务发现的相关API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            // nacos client自动通过基于权重的负载均衡算法，给我们选择一个实例。
            //Instance instance = namingService.selectOneHealthyInstance(name);

            // 基于权重的负载均衡算法，返回1个实例
            List<Instance> instanceList = namingService.selectInstances(name, true);
            if (instanceList.size() > 0) {
                Instance instance = ExtendBalancer.getHostByRandomWeight2(instanceList);
                log.info("请求服务名称={}，选择到的实例：serviceName={}，ip={}", name, instance.getServiceName(), instance.getIp());
                return new NacosServer(instance);
            } else {
                log.info("请求服务名称={}，未找到可用实例！", name);
                return null;
            }
        } catch (Exception e) {
            log.info("实例匹配错误：{}", e);
            return null;
        }
    }
}

class ExtendBalancer extends Balancer {
    public static Instance getHostByRandomWeight2(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}