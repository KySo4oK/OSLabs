package org.example;

public class App {
    public static void main(String[] args) {
        long nanoTimeBefore = System.nanoTime();
        System.out.println(nanoTimeBefore);
        byte[][][] AMD = new byte[100][100][100];
            byte res = 0;

            for (byte i = 99; i >= 0; i--)
            {
                for (byte j = 99; j >= 0; j--)
                {
                    for (byte k = 99; k >= 0; k--)
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
