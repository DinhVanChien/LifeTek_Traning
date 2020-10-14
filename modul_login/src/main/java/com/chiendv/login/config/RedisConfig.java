package com.chiendv.login.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * @author ChienDV
 *
 */
@Configuration
public class RedisConfig {
	
	@Value("${redis.host}")
	private String redisHost;
	
	@Value("${redis.port}")
	private int redisPort;
	/*
	 * Chúng ta cần sử dụng lettuce để kết nối tới Redis,tạo ra bean LettuceConnectionFactory và 
	 *  Spring Data sẽ tự động nhận vào cấu hình.
	 */
	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		// Tạo Standalone Connection tới Redis 
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
	}
	/*
	 * Cấu RedisTemplate nhận key là Object và value cũng là Object luôn. 
	 * Để chúng ta có thể lưu bất kỳ key-value nào xuống Redis.
	 */
	@Bean
	@Primary // nếu 2 bean trùng nhau nó sẽ ưu tiên bean đánh dấu @Primary
	public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // RedisTemplate giúp chúng ta thao tác với Redis
		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setConnectionFactory(lettuceConnectionFactory);
		return template;
	}
}
