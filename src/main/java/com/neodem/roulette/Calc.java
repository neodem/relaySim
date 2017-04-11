package com.neodem.roulette;

import java.util.ArrayList;
import java.util.List;

import static com.neodem.roulette.Generator.DOUBLE_ZERO;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 5/26/16
 */
public class Calc {
    private Generator g = new Generator();

    public static void main(String[] args) {
        new Calc().start(100000);
    }

    public void start(int size) {
        List<Integer> tally = initList(38);
        List<Integer> runSizesZero = initList(25);
        List<Integer> runSizesOdd = initList(25);
        List<Integer> runSizesEven = initList(25);

        short lastValue = -1;
        int runCount = 0;

        for (int i = 0; i < size; i++) {
            short value = g.getNext();
            int count = tally.get(value);
            tally.set(value, ++count);

            if(lastValue == -1) {
                runCount = 1;
                lastValue = value;
            } else {
                if (isZero(value)) {
                    if (isZero(lastValue)) runCount++;
                    else {
                        int runCountTally = runSizesZero.get(runCount);
                        runSizesZero.set(runCount, ++runCountTally);
                        runCount = 0;
                        lastValue = -1;
                    }
                } else {

                    if (isOdd(value)) {
                        if (isOdd(lastValue)) runCount++;
                        else {
                            int runCountTally = runSizesOdd.get(runCount);
                            runSizesOdd.set(runCount, ++runCountTally);
                            runCount = 0;
                            lastValue = -1;
                        }
                    } else {
                        if (!isOdd(lastValue)) runCount++;
                        else {
                            int runCountTally = runSizesEven.get(runCount);
                            runSizesEven.set(runCount, ++runCountTally);
                            runCount = 0;
                            lastValue = -1;
                        }
                    }
                }
            }
        }
        print(tally, runSizesEven, runSizesOdd, runSizesZero);
    }

    private boolean isOdd(short value) {
        return value % 2 != 0;
    }

    public boolean isZero(short value) {
        return (value == DOUBLE_ZERO || value == 0);
    }

    private void print(List<Integer> tally, List<Integer> runSizesEven, List<Integer> runSizesOdd, List<Integer> runSizesZero) {
        printList(tally, "tally");
        printList(runSizesEven, "even");
        printList(runSizesOdd, "odd");
        printList(runSizesZero, "zero");
    }

    private void printList(List<Integer> tally, String name) {
        System.out.println(name);
        System.out.println("=================");
        int total = 0;
        for (int i = 0; i < tally.size(); i++) {
            int count = tally.get(i);
            System.out.println("" + i + "  | " + count);
            total = total + count;
        }
        System.out.println("---| " + total);
    }

    private List<Integer> initList(int size) {
        List<Integer> tally = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tally.add(0);
        }
        return tally;
    }
}
