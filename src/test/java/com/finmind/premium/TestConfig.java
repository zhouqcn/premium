package com.finmind.premium;

import org.junit.*;

public class TestConfig {
    @Test
    public void testConfig() throws Exception {
        Config config = Config.readConfig("test_products.json");
    }
}
