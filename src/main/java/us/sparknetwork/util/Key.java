package us.sparknetwork.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Key<K, V> {

    public K key;
    public V value;
}
