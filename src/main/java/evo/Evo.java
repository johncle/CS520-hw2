package src.main.java.evo;

import java.lang.Math;

public class Evo {
    public static int sum(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        return sum;
    }

    public static int sumOutOfBounds(int[] nums) {
        int sum = 0;
        // i should stop before nums.length
        for (int i = 0; i <= nums.length; ++i) {
            sum += nums[i];
        }
        return sum;
    }

    public static int sumOffByOne(int[] nums) {
        int sum = 0;
        // i should start at 0
        for (int i = 1; i < nums.length; ++i) {
            sum += nums[i];
        }
        return sum;
    }

    // range includes start and excludes end
    public static int[] genRange1(int start, int end, int step) {
        int[] range = new int[(int) Math.ceil((end - start) / step)];
        int x = start - step;
        for (int i = 0; i < range.length; ++i) {
            range[i] = x += step;
        }
        return range;
    }

    public static int[] genRange2(int start, int end, int step) {
        int[] range = new int[(int) Math.ceil((end - start) / step)];
        int x = start - step;
        int i = 0;
        while (x + step < end) {
            range[i++] = x += step;
        }
        return range;
    }

    public static int[] genRangeBadMath1(int start, int end, int step) {
        // forgot to include parentheses
        int[] range = new int[(int) Math.ceil(end - start / step)];
        int x = start - step;
        for (int i = 0; i < range.length; ++i) {
            range[i] = x += step;
        }
        return range;
    }

    public static int[] genRangeBadMath2(int start, int end, int step) {
        int[] range = new int[(int) Math.ceil((end - start) / step)];
        int x = start - step, i = 0;
        // forgot to account for x == end
        while (x < end) {
            range[i++] = x += step;
        }
        return range;
    }
}