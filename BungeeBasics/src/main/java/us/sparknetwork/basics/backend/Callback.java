package us.sparknetwork.basics.backend;

public interface Callback<V extends Object> {
	public void call(V result);
}
