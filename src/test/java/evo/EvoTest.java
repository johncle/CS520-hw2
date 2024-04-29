package src.test.java.evo;

import src.main.java.evo.Evo;

import org.junit.Test;
import org.junit.Assert;

public class EvoTest {
    @Test
    public void testSum() {
        int[] nums = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int expected = (10 * 11) / 2;

        Assert.assertEquals(expected, Evo.sum(nums));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSumThrows() {
        int[] nums = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        Evo.sumOutOfBounds(nums);
    }
}
