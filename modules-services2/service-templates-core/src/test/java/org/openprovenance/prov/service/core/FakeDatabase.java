package org.openprovenance.prov.service.core;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A stand-in database for tests: connections that record every statement and query sent to them, answer each query
 * with the rows {@link #answers} gives for its SQL (none by default), and fail the statements {@link #failing} accepts.
 */
final class FakeDatabase {
    final List<String> statements = Collections.synchronizedList(new ArrayList<>());
    final List<String> queries = Collections.synchronizedList(new ArrayList<>());
    final AtomicInteger commits = new AtomicInteger();
    final AtomicInteger rollbacks = new AtomicInteger();
    volatile Function<String, List<Map<String, Object>>> answers = sql -> List.of();
    volatile Predicate<String> failing = sql -> false;
    /** Runs before each statement (a test may block a writer on it). */
    volatile Runnable beforeStatement = () -> { };

    Connection connection() {
        boolean[] autoCommit = {true};
        return (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Connection.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isValid" -> true;
                    case "isClosed" -> false;
                    case "getAutoCommit" -> autoCommit[0];
                    case "setAutoCommit" -> { autoCommit[0] = (Boolean) args[0]; yield null; }
                    case "commit" -> { commits.incrementAndGet(); yield null; }
                    case "rollback" -> { rollbacks.incrementAndGet(); yield null; }
                    case "close" -> null;
                    case "createStatement" -> statement();
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    private Statement statement() {
        return (Statement) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Statement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "execute" -> {
                        String sql = (String) args[0];
                        beforeStatement.run();
                        if (sql.startsWith("DISCARD")) yield false;
                        if (failing.test(sql)) throw new SQLException("refused by the test: " + sql);
                        statements.add(sql);
                        yield false;
                    }
                    case "executeQuery" -> {
                        String sql = (String) args[0];
                        queries.add(sql);
                        yield resultSet(answers.apply(sql), (Statement) proxy);
                    }
                    case "close" -> null;
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    private ResultSet resultSet(List<Map<String, Object>> rows, Statement statement) {
        Iterator<Map<String, Object>> it = rows.iterator();
        Object[] current = {null};
        return (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ResultSet.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "next":
                            if (it.hasNext()) {
                                current[0] = it.next();
                                return true;
                            }
                            return false;
                        case "getObject":
                        case "getString":
                        case "getInt": {
                            Object v = ((Map<?, ?>) current[0]).get(args[0]);
                            if (method.getName().equals("getString")) return v == null ? null : v.toString();
                            if (method.getName().equals("getInt")) return v == null ? 0 : ((Number) v).intValue();
                            return v;
                        }
                        case "close":
                            return null;
                        case "getStatement":
                            return statement;
                        case "hashCode":
                            return System.identityHashCode(proxy);
                        case "equals":
                            return proxy == args[0];
                        default:
                            throw new UnsupportedOperationException(method.getName());
                    }
                });
    }
}
