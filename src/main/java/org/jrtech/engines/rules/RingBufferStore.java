/*
 * Copyright (c) 2016-2026 Jumin Rubin
 * LinkedIn: https://www.linkedin.com/in/juminrubin/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jrtech.engines.rules;

import java.util.LinkedHashMap;
import java.util.Map;

public class RingBufferStore<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 8594222201635650361L;
    
    private final int maxSize;

    public RingBufferStore() {
        this(200);
    }
    
    public RingBufferStore(int maxSize) {
        super(16, 0.75f, true);
        this.maxSize = maxSize;
    }
    
    @Override
    public synchronized V put(K key, V value) {
        return super.put(key, value);
    }
    
    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
    }
    
    @Override
    public synchronized V putIfAbsent(K key, V value) {
        return super.putIfAbsent(key, value);
    }

    @Override
    public synchronized V remove(Object key) {
        return super.remove(key);
    }
    
    @Override
    public synchronized boolean remove(Object key, Object value) {
        return super.remove(key, value);
    }
    
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        if (super.size() > maxSize) {
            return true;
        }
        
        return super.removeEldestEntry(eldest);
    }
    
}
