package com.ldg.baoli.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;


@Slf4j
@Configuration
public class RestTemplateConfig {

    //自创建RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectTimeout(10*1000);
        httpComponentsClientHttpRequestFactory.setReadTimeout(10*1000);
        httpComponentsClientHttpRequestFactory.setHttpClient(httpClientBuilder().build());

        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getInterceptors()
                // 默认使用短连接
                .add(new ClientHttpRequestConnectionCloseInterceptor());

        return restTemplate;
    }
    /**
     * 设置HTTP连接管理器,连接池相关配置管理
     *
     * @return 客户端链接管理器
     */
    @Bean
    public HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingConnectionManager());
        return httpClientBuilder;
    }

    /**
     * 链接线程池管理,可以keep-alive不断开链接请求,这样速度会更快 MaxTotal 连接池最大连接数 DefaultMaxPerRoute
     * 每个主机的并发 ValidateAfterInactivity
     * 可用空闲连接过期时间,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
     *
     * @return
     */
    @Bean
    public HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(5000);
        poolingConnectionManager.setDefaultMaxPerRoute(5000);
        poolingConnectionManager.setValidateAfterInactivity(30000);
        return poolingConnectionManager;
    }
}
