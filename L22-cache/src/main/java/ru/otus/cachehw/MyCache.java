package ru.otus.cachehw;


import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notify(key, value, "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notify(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(ref -> {
            var l = ref.get();
            if (l != null) {
                return l.equals(listener);
            } else {
                return true;
            }
        });
    }

    private void notify(K  key, V value, String action) {
        for (var ref : listeners) {
            var l = ref.get();
            if (l != null) {
                try {
                    l.notify(key, value, action);
                } catch (Exception e) {
                    // ignore just to let rest of the listeners to be notified
                }
            } else {
                removeListener(null);
            }
        }
    }
}
