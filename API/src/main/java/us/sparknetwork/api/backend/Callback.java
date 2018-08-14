package us.sparknetwork.api.backend;

public interface Callback<T> {
    void call(T object);
}
