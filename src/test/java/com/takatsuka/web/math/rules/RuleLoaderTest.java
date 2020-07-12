package com.takatsuka.web.math.rules;

import com.takatsuka.web.math.MathOps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class RuleLoaderTest {

    private MathOps mathOps;
    private RuleLoader ruleLoader;

    @BeforeEach
    void setup() {
        mathOps = new MathOps();
        ruleLoader = new RuleLoader();
    }

    @Test
    void exampleTest() {
        assertThat(5).isEqualTo(5);
    }
}
