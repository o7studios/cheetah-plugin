package studio.o7.cheetah.plugin.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Labels extends Map<String, String> {

    static Labels fromMap(Map<String, String> map) {
        return new Labels() {
            @Override
            public int size() {
                return map.size();
            }

            @Override
            public boolean isEmpty() {
                return map.isEmpty();
            }

            @Override
            public boolean containsKey(Object key) {
                return map.containsKey(key);
            }

            @Override
            public boolean containsValue(Object value) {
                return map.containsValue(value);
            }

            @Override
            public String get(Object key) {
                return map.get(key);
            }

            @Override
            public @Nullable String put(String key, String value) {
                return map.put(key, value);
            }

            @Override
            public String remove(Object key) {
                return map.remove(key);
            }

            @Override
            public void putAll(@NotNull Map<? extends String, ? extends String> m) {
                map.putAll(m);
            }

            @Override
            public void clear() {
                map.clear();
            }

            @Override
            public @NotNull Set<String> keySet() {
                return map.keySet();
            }

            @Override
            public @NotNull Collection<String> values() {
                return map.values();
            }

            @Override
            public @NotNull Set<Entry<String, String>> entrySet() {
                return map.entrySet();
            }
        };
    }
}
