package com.jeondoh.core.common.component;

import com.jeondoh.core.common.exception.BaseCoreException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedisLuaScriptHelper {

    public <T> RedisScript<T> getRedisScript(String script, Class<T> clazz) {
        String scriptSrc = "lua/" + script;
        try {
            return new DefaultRedisScript<>(
                    new ResourceScriptSource(
                            new ClassPathResource(scriptSrc)
                    ).getScriptAsString(), clazz
            );
        } catch (IOException e) {
            throw BaseCoreException.luaFileIoException();
        }
    }
}
