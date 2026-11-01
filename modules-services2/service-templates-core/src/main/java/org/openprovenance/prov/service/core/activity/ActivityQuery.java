package org.openprovenance.prov.service.core.activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Reads the {@code activity_log} table for the dashboard: the counts of every kind over
 * the last minute, hour, day and week ({@link #summary}), and a time series over one of
 * those windows ({@link #series}). Every figure is a sum over whole buckets, so a window
 * is aligned on the bucket width (10 s by default), not on the instant of the call.
 */
public class ActivityQuery {

    /** The windows of the dashboard, with the range they cover and the step of their series. */
    public enum Window {
        MINUTE(Duration.ofMinutes(1), Duration.ofSeconds(10)),
        HOUR(Duration.ofHours(1), Duration.ofMinutes(1)),
        DAY(Duration.ofDays(1), Duration.ofMinutes(30)),
        WEEK(Duration.ofDays(7), Duration.ofHours(6));

        public final Duration range;
        public final Duration step;

        Window(Duration range, Duration step) {
            this.range = range;
            this.step = step;
        }

        public String key() { return name().toLowerCase(Locale.ROOT); }

        /** The window named by {@code s} (case-insensitive), or {@code dflt} when null or unknown. */
        public static Window parse(String s, Window dflt) {
            if (s == null) return dflt;
            try {
                return valueOf(s.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                return dflt;
            }
        }
    }

    static final String SUMMARY =
            "SELECT COALESCE(k.kind, a.kind) AS kind, k.description,\n" +
            "       COALESCE(a.minute, 0) AS minute, COALESCE(a.hour, 0) AS hour,\n" +
            "       COALESCE(a.day, 0) AS day, COALESCE(a.week, 0) AS week, a.last_seen\n" +
            "FROM (SELECT kind,\n" +
            "             SUM(count) FILTER (WHERE bucket >= ?) AS minute,\n" +
            "             SUM(count) FILTER (WHERE bucket >= ?) AS hour,\n" +
            "             SUM(count) FILTER (WHERE bucket >= ?) AS day,\n" +
            "             SUM(count) AS week,\n" +
            "             MAX(bucket) AS last_seen\n" +
            "      FROM " + ActivityLogger.TABLE + "\n" +
            "      WHERE bucket >= ?\n" +
            "      GROUP BY kind) a\n" +
            "FULL OUTER JOIN " + ActivityLogger.KIND_TABLE + " k ON k.kind = a.kind\n" +
            "ORDER BY 1";

    static final String SERIES =
            "SELECT kind, floor(extract(epoch FROM bucket) / ?) * ? AS step, SUM(count) AS count\n" +
            "FROM " + ActivityLogger.TABLE + "\n" +
            "WHERE bucket >= ?\n" +
            "GROUP BY kind, step\n" +
            "ORDER BY step, kind";

    /** The connection each query runs on, asked for every time: the service's own, which is reopened when it breaks. */
    private final Supplier<Connection> conn;

    public ActivityQuery(Connection conn) {
        this(() -> conn);
    }

    public ActivityQuery(Supplier<Connection> conn) {
        this.conn = conn;
    }

    /** One line of the summary: a kind and its counts over the four windows. */
    public static final class KindSummary {
        public final String kind;
        public final String description;
        public final long minute;
        public final long hour;
        public final long day;
        public final long week;
        public final Instant lastSeen;

        public KindSummary(String kind, String description, long minute, long hour, long day, long week, Instant lastSeen) {
            this.kind = kind;
            this.description = description;
            this.minute = minute;
            this.hour = hour;
            this.day = day;
            this.week = week;
            this.lastSeen = lastSeen;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("kind", kind);
            m.put("description", description);
            m.put("minute", minute);
            m.put("hour", hour);
            m.put("day", day);
            m.put("week", week);
            m.put("last_seen", lastSeen == null ? null : lastSeen.toString());
            return m;
        }
    }

    /** The counts of every kind over the last minute, hour, day and week, ending at {@code now}. */
    public List<KindSummary> summary(Instant now) throws SQLException {
        List<KindSummary> result = new ArrayList<>();
        try (PreparedStatement ps = conn.get().prepareStatement(SUMMARY)) {
            ps.setTimestamp(1, Timestamp.from(now.minus(Window.MINUTE.range)));
            ps.setTimestamp(2, Timestamp.from(now.minus(Window.HOUR.range)));
            ps.setTimestamp(3, Timestamp.from(now.minus(Window.DAY.range)));
            ps.setTimestamp(4, Timestamp.from(now.minus(Window.WEEK.range)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp last = rs.getTimestamp("last_seen");
                    result.add(new KindSummary(rs.getString("kind"), rs.getString("description"),
                            rs.getLong("minute"), rs.getLong("hour"), rs.getLong("day"), rs.getLong("week"),
                            last == null ? null : last.toInstant()));
                }
            }
        }
        return result;
    }

    /** One point of a series: the count of a kind over one step of the window. */
    public static final class Point {
        public final String kind;
        public final Instant step;
        public final long count;

        public Point(String kind, Instant step, long count) {
            this.kind = kind;
            this.step = step;
            this.count = count;
        }
    }

    /**
     * The counts per kind and per step over {@code window}, ending at {@code now}. Steps with
     * no activity have no point; the caller fills the gaps if it wants a dense series.
     */
    public List<Point> series(Window window, Instant now) throws SQLException {
        long stepSeconds = window.step.getSeconds();
        Instant from = alignDown(now.minus(window.range), stepSeconds);
        List<Point> points = new ArrayList<>();
        try (PreparedStatement ps = conn.get().prepareStatement(SERIES)) {
            ps.setLong(1, stepSeconds);
            ps.setLong(2, stepSeconds);
            ps.setTimestamp(3, Timestamp.from(from));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    points.add(new Point(rs.getString("kind"),
                            Instant.ofEpochSecond((long) rs.getDouble("step")),
                            rs.getLong("count")));
                }
            }
        }
        return points;
    }

    /** {@code t} rounded down to a multiple of {@code stepSeconds} since the epoch. */
    public static Instant alignDown(Instant t, long stepSeconds) {
        long s = t.getEpochSecond();
        return Instant.ofEpochSecond(s - Math.floorMod(s, stepSeconds));
    }
}
