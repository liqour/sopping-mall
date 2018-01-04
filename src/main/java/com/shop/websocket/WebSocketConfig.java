package com.shop.websocket;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 
 * @Description: WebSocket注册类 
 * @ClassName: WebSocketConfig 
 * @author liquor
 * @date 2017年9月6日 下午5:00:11 
 *
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	private final Logger LOGGER = Logger.getLogger(WebSocketConfig.class);
	
	/**
	 * 注册WebSocket
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		LOGGER.info("开始注册webSocket服务");
		registry.addHandler(systemWebSocketHandler(), "/webSocketServer").addInterceptors(new WebSocketHandshakeInterceptor());
		LOGGER.info("webSocket服务注册完成");
	}

	/**
	 * 获取webSocket处理
	 */
	@Bean
	public WebSocketHandler systemWebSocketHandler(){
		return new SystemWebSocktHandler();
	}
	
}
