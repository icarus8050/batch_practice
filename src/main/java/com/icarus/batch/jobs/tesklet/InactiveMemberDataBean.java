package com.icarus.batch.jobs.tesklet;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@JobScope
public class InactiveMemberDataBean<T> {

    private Map<String, T> shareDataMap;

    public InactiveMemberDataBean() {
        this.shareDataMap = new ConcurrentHashMap<>();
    }

    public void put(String key, T data) {
        shareDataMap.put(key, data);
    }

    public T get(String key) {
        return shareDataMap.get(key);
    }
}
