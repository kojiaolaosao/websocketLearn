package com.example.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @program: learnwebsokect
 * @description: 开启webSocket支持
 * @author: Mr.qi
 * @create: 2021-08-10 21:50
 **/

@Configuration
public class WebSocket {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
