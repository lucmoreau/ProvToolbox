package org.openprovenance.prov.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.openprovenance.prov.model.interop.PrincipalManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/** Submissions hashed side by side (a pool, parallel clients) get the hash each would get alone. */
public class ConcurrentHashTest {

    @Test
    public void hashesComputedSideBySideAreTheSequentialOnes() throws InterruptedException {
        TemplateQuery query = new TemplateQuery(new Querier(new Storage(), new FakeDatabase().connection()), FakeCatalogue.create(),
                new PrincipalManager(), Map.of(), new ObjectMapper());
        int records = 200;
        List<String> expected = new ArrayList<>();
        for (int i = 0; i < records; i++) expected.add(query.computeHash(FakeCatalogue.MAKING, i, record(i)).get(TemplateQuery.SHA_3_512));
        assertEquals(records, Set.copyOf(expected).size());

        Map<Integer, String> seen = new ConcurrentHashMap<>();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        CountDownLatch go = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();
        for (int t = 0; t < 8; t++) {
            Thread thread = new Thread(() -> {
                try {
                    go.await();
                    for (int round = 0; round < 20; round++) {
                        for (int i = 0; i < records; i++) {
                            String h = query.computeHash(FakeCatalogue.MAKING, i, record(i)).get(TemplateQuery.SHA_3_512);
                            if (!h.equals(expected.get(i))) seen.put(i, h);
                        }
                    }
                } catch (Throwable e) {
                    failure.compareAndSet(null, e);
                }
            });
            threads.add(thread);
            thread.start();
        }
        go.countDown();
        for (Thread t : threads) t.join();
        assertNull("no failure: " + failure.get(), failure.get());
        assertEquals("no hash differs from the record's own", Map.of(), seen);
    }

    private static Object[] record(int id) {
        return new Object[]{FakeCatalogue.MAKING, id, 100 + id, 200 + id, 300 + id};
    }
}
