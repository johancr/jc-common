package se.cronsioe.johan.base.transaction;

public interface Transaction {

    void add(TransactionListener listener);

    void commit();

    boolean isActive();

    <T> T lookup(Class<T> klass);

    <T> void bind(Class<T> klass, T object);
}
