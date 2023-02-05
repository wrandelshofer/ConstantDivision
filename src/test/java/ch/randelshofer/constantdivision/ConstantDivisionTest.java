package ch.randelshofer.constantdivision;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ConstantDivisionTest {
    @TestFactory
    public Stream<DynamicTest> dynamicTests_signed() {
        Random rng=new Random(0);
        return Stream.generate(()->Map.entry(rng.nextInt(),(rng.nextInt()&0xffff)*(rng.nextBoolean()?-1:1)))
                .filter(e->e.getValue()!=1&&e.getValue()!=0)
                .distinct()
                .limit(10000)
                .map(e -> dynamicTest(e.getKey()+"/"+e.getValue(),
                        () -> testSigned(e.getKey(),e.getValue()))
                        );

    }

    private void testSigned(int dividend,int divisor) {

        int expectedDivision = dividend/divisor;
        int expectedRemainder = dividend%divisor;
        long M = ConstantDivision.computeM_s32(divisor);
        int actualDivision = ConstantDivision.fastdiv_s32(dividend, M,(divisor));
        int actualRemainder = ConstantDivision.fastmod_s32(dividend, M,Math.abs( divisor));

        assertEquals(expectedDivision,actualDivision,"div");
        assertEquals(expectedRemainder,actualRemainder,"mod");
    }
    @TestFactory
    public Stream<DynamicTest> dynamicTests_unsigned() {
        Random rng=new Random(0);
        return Stream.generate(()->Map.entry(Math.abs(rng.nextInt()),(rng.nextInt()&0xffff)))
                .filter(e->e.getValue()!=1&&e.getValue()!=0)
                .distinct()
                .limit(10000)
                .map(e -> dynamicTest(e.getKey()+"/"+e.getValue(),
                        () -> testUnsigned(e.getKey(),e.getValue()))
                        );

    }

    private void testUnsigned(int dividend,int divisor) {

        int expectedDivision = dividend/divisor;
        int expectedRemainder = dividend%divisor;
        long M = ConstantDivision.computeM_u32(divisor);
        int actualDivision = ConstantDivision.fastdiv_u32(dividend, M);
        int actualRemainder = ConstantDivision.fastmod_u32(dividend, M, divisor);

        assertEquals(expectedDivision,actualDivision,"div");
        assertEquals(expectedRemainder,actualRemainder,"mod");
    }

}