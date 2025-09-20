package com.nubank.challenge;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    private void runTest(String input, String expectedOutput) throws Exception {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        App.main(new String[]{});

        String actualOutput = testOut.toString().trim();
        assertEquals(expectedOutput.trim(), actualOutput);
    }

    @Test
    public void testCase1() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":10.00,"quantity":100},{"operation":"sell","unit-cost":15.00,"quantity":50},{"operation":"sell","unit-cost":15.00,"quantity":50}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":0.0},{"tax":0.0}]
        """;
        runTest(input, expected);
    }

    @Test
    public void testCase2() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":10.00,"quantity":10000},{"operation":"sell","unit-cost":20.00,"quantity":5000},{"operation":"sell","unit-cost":5.00,"quantity":5000}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":10000.0},{"tax":0.0}]
        """;
        runTest(input, expected);
    }

    @Test
    public void testCase3() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":10.00,"quantity":10000},{"operation":"sell","unit-cost":5.00,"quantity":5000},{"operation":"sell","unit-cost":20.00,"quantity":3000}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":0.0},{"tax":1000.0}]
        """;
        runTest(input, expected);
    }

    @Test
    public void testCase4() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":10.00,"quantity":10000},{"operation":"buy","unit-cost":25.00,"quantity":5000},{"operation":"sell","unit-cost":15.00,"quantity":10000}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":0.0},{"tax":0.0}]
        """;
        runTest(input, expected);
    }

    @Test
    public void testCase5() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":10.00,"quantity":10000},{"operation":"buy","unit-cost":25.00,"quantity":5000},{"operation":"sell","unit-cost":15.00,"quantity":10000},{"operation":"sell","unit-cost":25.00,"quantity":5000}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":10000.0}]
        """;
        runTest(input, expected);
    }

    @Test
    public void testCase6() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":10.00,"quantity":10000},{"operation":"sell","unit-cost":2.00,"quantity":5000},{"operation":"sell","unit-cost":20.00,"quantity":2000},{"operation":"sell","unit-cost":20.00,"quantity":2000},{"operation":"sell","unit-cost":25.00,"quantity":1000}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":3000.0}]
        """;
        runTest(input, expected);
    }

    @Test
    public void testCase7() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":10.00,"quantity":10000},{"operation":"sell","unit-cost":2.00,"quantity":5000},{"operation":"sell","unit-cost":20.00,"quantity":2000},{"operation":"sell","unit-cost":20.00,"quantity":2000},{"operation":"sell","unit-cost":25.00,"quantity":1000},{"operation":"buy","unit-cost":20.00,"quantity":10000},{"operation":"sell","unit-cost":15.00,"quantity":5000},{"operation":"sell","unit-cost":30.00,"quantity":4350},{"operation":"sell","unit-cost":30.00,"quantity":650}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":3000.0},{"tax":0.0},{"tax":0.0},{"tax":3700.0},{"tax":0.0}]
        """;
        runTest(input, expected);
    }

    @Test
    public void testCase8() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":10.00,"quantity":10000},{"operation":"sell","unit-cost":50.00,"quantity":10000},{"operation":"buy","unit-cost":20.00,"quantity":10000},{"operation":"sell","unit-cost":50.00,"quantity":10000}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":80000.0},{"tax":0.0},{"tax":60000.0}]
        """;
        runTest(input, expected);
    }

    @Test
    public void testCase9() throws Exception {
        String input = """
          [{"operation":"buy","unit-cost":5000.00,"quantity":10},{"operation":"sell","unit-cost":4000.00,"quantity":5},{"operation":"buy","unit-cost":15000.00,"quantity":5},{"operation":"buy","unit-cost":4000.00,"quantity":2},{"operation":"buy","unit-cost":23000.00,"quantity":2},{"operation":"sell","unit-cost":20000.00,"quantity":1},{"operation":"sell","unit-cost":12000.00,"quantity":10},{"operation":"sell","unit-cost":15000.00,"quantity":3}]
          
        """;
        String expected = """
          [{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":0.0},{"tax":1000.0},{"tax":2400.0}]
        """;
        runTest(input, expected);
    }
}
