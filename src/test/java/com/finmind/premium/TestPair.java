package com.finmind.premium;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestPair {
    @Test
    public void testPair() throws Exception {
        Pair<Integer, Long> pair = new Pair<>(20, 3000L);
        assertEquals(pair.toString(), "(20, 3000)");
        assertEquals(pair.first(), Integer.valueOf(20));
        assertEquals(pair.second(), Long.valueOf(3000L));

        Pair<Integer, Long> clone = pair.clone();
        assertEquals(clone.toString(), "(20, 3000)");
        assertEquals(pair, clone);
    }

    @Test
    public void testHashMapwithPair() throws Exception {
        HashMap<Pair<Integer, Long>, Float> map = new HashMap<>();
        map.put(new Pair<Integer, Long>(20, 3000L), Float.valueOf(35.67f));
        map.put(new Pair<Integer, Long>(25, 3500L), Float.valueOf(48.93f));
        assertEquals(map.get(new Pair<Integer, Long>(20, 3000L)), Float.valueOf(35.67f));
    }
}
