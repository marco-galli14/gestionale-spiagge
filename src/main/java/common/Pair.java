package common;

public record Pair<K, V>(K key, V value) {
    // I record generano automaticamente costruttore, getter (key() e value()), 
    // equals(), hashCode() e toString().
}
