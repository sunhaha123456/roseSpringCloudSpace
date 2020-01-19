package com.rose.controler;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.rose.common.data.response.ResponseResult;
import com.rose.common.exception.SentinelCaputeException;
import com.rose.common.util.JsonUtil;
import com.rose.data.base.BaseDto;
import com.rose.data.to.dto.UserLoginDto;
import com.rose.service.feign.FeignLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
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

    @Inject
    private FeignLoginService feignLoginService;

    /**
     * 功能：证明可以找到服务
     * @return
     */
    @GetMapping("/test1")
    public List<ServiceInstance> test1() {
        // 查询指定服务的所有实例的信息
        // consul/eureka/zookeeper...
        List<ServiceInstance> list = new ArrayList<>();
        List<ServiceInstance> list1 =  this.discoveryClient.getInstances("rose-login-server");
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
        targetUrl = targetUrl + "/rose-content-server/login/verify";
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
        String targetUrl = "http://rose-login-server/rose-login-server/login/verify";
        Map map = new HashMap<>();
        map.put("uname", "aaa");
        map.put("upwd", "bbb");
        map.put("key", "ccc");
        map.put("code", "ddd");
        Map res = loadBalance.postForObject(targetUrl, map, Map.class);
        return res;
    }

    /**
     * 功能：使用feign调用
     * @return
     */
    @GetMapping("/test4")
    public void test4() {
        UserLoginDto dto = new UserLoginDto();
        dto.setUname("111");
        dto.setUpwd("222");
        dto.setCode("333");
        dto.setKey("444");
        ResponseResult resp = feignLoginService.verify(dto);
        System.out.println(JsonUtil.objectToJson(resp));
    }

    // -------------- sentinel 限流、降级

    // -------------- sentinel 限流、降级 第一种方式

    @PostMapping("/user/sentinelTest1")
    public Object sentinelTest1(@RequestBody BaseDto dto) throws BlockException {
        Entry entry = null;
        try {
            // 定义sentinel 资源标志名称
            // 即：sentinel控制台中，需要对簇点链路中的 user-sentinelTest1 进行相应设置，而非 /user/sentinelTest1
            //     并且 user-sentinelTest1 与 /user/sentinelTest1 不要重名，否则会有问题
            String sentinelFlagName = "user-sentinelTest1";
            ContextUtil.enter(sentinelFlagName);
            entry = SphU.entry(sentinelFlagName);
            // 被保护的业务逻辑
            Map<String, Object> map = new HashMap<>();
            map.put("msg", "success");
            return map;
            //需要降级处理时，抛此异常
//            throw new SentinelCaputeException(ResponseResultCode.OPERT_ERROR);
        } catch (BlockException e) {
            // 如果被保护的资源触发了 限流 或 降级，都会抛 BlockException
            log.error("接口-/user/sentinelTest1，发生了限流，或者降级了，原因：{}", e);
            throw e;
        } catch (SentinelCaputeException e) {
            // 统计SentinelCaputeException【发生的次数、发生占比...】
            Tracer.trace(e);
            throw e;
        } finally {
            // 退出 sentinel
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    // -------------- sentinel 限流、降级 第二种方式

//    @PostMapping("/user/sentinelTest2")
//    @SentinelResource(value = "user-sentinelTest2", blockHandler = "blockHandle")
//    public Object sentinelTest2(@RequestBody BaseDto dto) throws FlowException {
//        try {
//            Map<String, Object> map = new HashMap<>();
//            map.put("msg", "success");
//            throw new SentinelCaptureException();
//        } catch (SentinelCaptureException e) {
//            Tracer.trace(e);
//            throw new SentinelCaptureException();
//        }
//    }

//    public Object blockHandle(BaseDto dto, BlockException e) throws BlockException {
//        log.error("接口-/user/sentinelTest2，被限流或降级了，blockException：{}", e);
//
//        throw new RuntimeException("/user/sentinelTest2，被限流或降级了");
//    }
//
//    @GetMapping("/sentinelTest")
//    @SentinelResource(value = "/sentinelTest",
//            blockHandler = "sentinelTestBlockHandler",  // 限流
//            fallback = "sentinelTestFallback")          // 降级
//    public Object sentinelTest(@RequestParam(required = false) String param1, @RequestParam(required = false) String param2) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("msg", "success");
//        return map;
//    }
//
//    public Object sentinelTestBlockHandler(String param1, String param2, BlockException e) {
//        // 处理一些具体也的业务
//        // 比如发送报警短信
//        throw new BusinessException(ResponseResultCode.SERVER_BUSY_ERROE);
//    }
//
//    public Object sentinelTestFallback(String param1, String param2) {
//        // 处理一些具体也的业务
//        // 比如发送报警短信
//        throw new BusinessException(ResponseResultCode.SERVER_BUSY_ERROE);
//    }

    //package com.rose.conf;
//
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//import com.rose.data.base.BaseDto;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class SentinelBlockHandler {
//
//    /**
//     * 功能：处理限流
//     * @param dto
//     * @param e
//     * @return
//     */
//    public static Object blockHandle(BaseDto dto, BlockException e) throws BlockException {
//        String url = dto.getRequestUrl();
//        log.error("requestUrl：{}，被限流了，blockException：{}", url, e);
//
//        throw new RuntimeException(url + " 被限流了");
//    }
//}
}