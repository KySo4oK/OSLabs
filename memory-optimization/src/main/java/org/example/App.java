package org.example;

public class App {
    public static void main(String[] args) {
        long nanoTimeBefore = System.nanoTime();
        System.out.println(nanoTimeBefore);
        int[][][] AMD = new int[100][100][100];
            int res = 0;

            for (int i = 0; i < 100; i++)
            {
                for (int j = 0; j < 100; j++)
                {
                    for (int k = 0; k < 100; k++)
                    {
                        AMD [k] [j] [i]++;
                    }
                }
            }
        long nanoTimeAfter = System.nanoTime();
        System.out.println(nanoTimeAfter);
        System.out.println(nanoTimeAfter-nanoTimeBefore);
    }
}
