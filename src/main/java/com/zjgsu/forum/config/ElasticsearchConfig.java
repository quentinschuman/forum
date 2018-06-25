package com.zjgsu.forum.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/25
 * Time: 22:25
 */
@Configuration
public class ElasticsearchConfig {

    private String host = "127.0.0.1";
    private int port = 9200;

//    @Bean
//    public Client client() throws Exception {
//        return TransportClient..
//        build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
//    }
//
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
//        return new ElasticsearchTemplate(client());
//    }
}
