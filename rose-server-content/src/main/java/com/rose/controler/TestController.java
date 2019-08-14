package com.rose.controler;

import com.rose.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @LoadBalanced
    private RestTemplate loadBalance;

    /**
     * 功能：证明可以找到服务
     * @return
     */
    @GetMapping("/test1")
    public List<ServiceInstance> test1() {
        // 查询指定服务的所有实例的信息
        // consul/eureka/zookeeper...
        List<ServiceInstance> list = new ArrayList<>();
        List<ServiceInstance> list1 =  this.discoveryClient.getInstances("rose-gateway-server");
        List<ServiceInstance> list2 =  this.discoveryClient.getInstances("rose-content-server");
        List<ServiceInstance> list3 =  this.discoveryClient.getInstances("rose-content-server-aaa");
        list.addAll(list1);
        list.addAll(list2);
        list.addAll(list3);
        System.out.println("获取到的实例列表：");
        System.out.println(JsonUtil.objectToJson(list));
        System.out.println("获取到的实例地址列表：");
        List<String> targetUrlList = list.stream().map(o -> o.getUri().toString()).collect(Collectors.toList());
        System.out.println(JsonUtil.objectToJson(targetUrlList));
        return list;
    }

    /**
     * 功能：原始的服务调用
     *       HttpClient 调用 与 restTemplate 调用
     * @return
     */
    @GetMapping("/test2")
    public Map test2() {
        List<ServiceInstance> list =  this.discoveryClient.getInstances("rose-gateway-server");
        System.out.println("获取到的实例地址列表：");
        List<String> targetUrlList = list.stream().map(o -> o.getUri().toString()).collect(Collectors.toList());
        System.out.println(JsonUtil.objectToJson(targetUrlList));
        String targetUrl = targetUrlList.get(0);
        targetUrl = targetUrl + "/rose-gateway-server/login/verify";
        Map map = new HashMap<>();
        map.put("uname", "aaa");
        map.put("upwd", "bbb");
        map.put("key", "ccc");
        map.put("code", "ddd");
        //HttpClientUtil.postJson(targetUrl, map, Map.class, true);
//        Map res = restTemplate.postForObject(targetUrl, Map.class, map);
        Map res = restTemplate.postForObject(targetUrl, map, Map.class);
        return res;
    }

    /**
     * 功能：使用robbin调用
     * @return
     */
    @GetMapping("/test3")
    public Map test3() {
        String targetUrl = "http://rose-gateway-server/rose-gateway-server/login/verify";
        Map map = new HashMap<>();
        map.put("uname", "aaa");
        map.put("upwd", "bbb");
        map.put("key", "ccc");
        map.put("code", "ddd");
        Map res = loadBalance.postForObject(targetUrl, map, Map.class);
        return res;
    }
}