package org.example;

public class App {
    public static void main(String[] args) {
        long nanoTimeBefore = System.nanoTime();
        System.out.println(nanoTimeBefore);
        byte[][][] AMD = new byte[100][100][100];
            byte res = 0;

            for (byte i = 0; i < 100; i++)
            {
                for (byte j = 0; j < 100; j++)
                {
                    for (byte k = 0; k < 100; k++)
                    {
                        AMD [i] [j] [k]++;
                    }
                }
            }
        long nanoTimeAfter = System.nanoTime();
        System.out.println(nanoTimeAfter);
        System.out.println(nanoTimeAfter-nanoTimeBefore);
    }
}
