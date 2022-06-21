package com.umc.thingseye2.iotgw.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisServerConfig {

	@Autowired
	public Environment environment ;

	
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName(environment.getRequiredProperty("spring.redis.host"));
        jedisConFactory.setPort(environment.getRequiredProperty("spring.redis.port", Integer.class));        
        return jedisConFactory;
    }

    @Bean(name="defaultRedisTemplate")
    public RedisTemplate<String, Object> defaultRedisTemplate() {
    
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate r = new StringRedisTemplate();
		r.setConnectionFactory(jedisConnectionFactory());
		return r;
	}

}
