package com.rose.controler;

import com.rose.dbopt.mapper.TbSysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class TestController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Inject
    private TbSysUserMapper tbSysUserMapper;

    /**
     * 服务发现，证明内容中心总能找到用户中心
     * @return
     */
    @GetMapping("/test1")
    public List<ServiceInstance> getInstances() {
        // 查询指定服务的所有实例的信息
        // consul/eureka/zookeeper...
        List<ServiceInstance> list = new ArrayList<>();
        List<ServiceInstance> list1 =  this.discoveryClient.getInstances("rose-gateway-server");
        List<ServiceInstance> list2 =  this.discoveryClient.getInstances("rose-content-server");
        List<ServiceInstance> list3 =  this.discoveryClient.getInstances("rose-content-server-aaa");
        list.addAll(list1);
        list.addAll(list2);
        list.addAll(list3);
        return list;
    }
}