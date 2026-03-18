package com.kanade.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.kanade.backend.service.ImportTaskRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportTaskRedisServiceImpl implements ImportTaskRedisService {

    private static final String TASK_PREFIX = "import:task:";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StringRedisTemplate redisTemplate;

    @Override
    public void createTask(String taskId, Long creatorId) {
        String key = TASK_PREFIX + taskId;
        Map<String, String> map = new HashMap<>();
        map.put("status", "processing");
        map.put("total", "0");
        map.put("success", "0");
        map.put("fail", "0");
        map.put("errors", "[]");
        map.put("createTime", LocalDateTime.now().format(DTF));
        map.put("updateTime", LocalDateTime.now().format(DTF));
        redisTemplate.opsForHash().putAll(key, map);
        // 使用 Duration 设置过期时间，避免 TimeUnit
        redisTemplate.expire(key, Duration.ofHours(24));
    }

    @Override
    public void updateProgress(String taskId, int totalInc, int successInc, int failInc, List<String> newErrors) {
        String key = TASK_PREFIX + taskId;
        redisTemplate.opsForHash().increment(key, "total", totalInc);
        redisTemplate.opsForHash().increment(key, "success", successInc);
        redisTemplate.opsForHash().increment(key, "fail", failInc);
        if (newErrors != null && !newErrors.isEmpty()) {
            String existingErrors = (String) redisTemplate.opsForHash().get(key, "errors");
            List<String> errorList = JSON.parseArray(existingErrors, String.class);
            errorList.addAll(newErrors);
            redisTemplate.opsForHash().put(key, "errors", JSON.toJSONString(errorList));
        }
        redisTemplate.opsForHash().put(key, "updateTime", LocalDateTime.now().format(DTF));
    }

    @Override
    public void finishTask(String taskId, boolean success) {
        String key = TASK_PREFIX + taskId;
        redisTemplate.opsForHash().put(key, "status", success ? "success" : "failed");
        redisTemplate.opsForHash().put(key, "updateTime", LocalDateTime.now().format(DTF));
    }

    @Override
    public Map<Object, Object> getTaskStatus(String taskId) {
        String key = TASK_PREFIX + taskId;
        return redisTemplate.opsForHash().entries(key);
    }
}