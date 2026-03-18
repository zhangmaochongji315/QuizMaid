package com.kanade.backend.service;

import java.util.List;
import java.util.Map;

public interface ImportTaskRedisService {

    /**
     * 创建任务初始状态
     */
    void createTask(String taskId, Long creatorId);

    /**
     * 更新任务进度（原子操作）
     */
    void updateProgress(String taskId, int totalInc, int successInc, int failInc, List<String> newErrors);

    /**
     * 完成任务（状态置为 success 或 failed）
     */
    void finishTask(String taskId, boolean success);

    /**
     * 获取任务状态
     */
    Map<Object, Object> getTaskStatus(String taskId);
}