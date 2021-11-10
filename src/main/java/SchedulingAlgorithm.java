

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    public static Results Run(int runtime, Vector processVector) {
        int i;
        int comptime = 0;
        int currentProcess = 0;
        int previousProcess;
        int size = processVector.size();
        int completed = 0;
        String resultsFile = "Summary-Processes";
        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            sProcess process = (sProcess) processVector.elementAt(currentProcess);
            out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
            while (comptime < runtime) {
                if (process.cpudone == process.cputime) {
                    completed++;
                    out.println("Process: " + currentProcess + " completed "+ process.cpudone+ "... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                    if (completed == size) {
                        out.close();
                        return new Results("Batch (Non preemptive)","Shortest Job First",comptime);
                    }
                    currentProcess = findMin(processVector,currentProcess);
                    process = (sProcess) processVector.elementAt(currentProcess);
                    out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                }
                if (process.ioblocking == process.ionext) {
                    out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                    process.numblocked++;
                    process.ionext = 0;
                    previousProcess = currentProcess;
                    currentProcess = findMin(processVector,previousProcess);

                    process = (sProcess) processVector.elementAt(currentProcess);
                    out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                }
                process.cpudone++;
                if (process.ioblocking > 0) {
                    process.ionext++;
                }
                comptime++;
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }

        return new Results("Preemptive","Shortest Job First",comptime);

    }
    public static int findMin(Vector processVector, int currentProcess){
        int size = processVector.size();
        int min = 1000, nextProcess = currentProcess;
        for (int i = 0; i < size ; i++) {
            sProcess process = (sProcess) processVector.elementAt(i);
            if(process.cpudone < process.cputime  && i != currentProcess){
                if(process.ioblocking < min) {
                    min = process.ioblocking;
                    nextProcess = i;
                }
            }
        }
        return nextProcess;
    }
}

