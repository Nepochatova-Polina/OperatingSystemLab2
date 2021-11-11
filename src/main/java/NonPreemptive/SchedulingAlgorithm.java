// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
package  NonPreemptive;
import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    public static void Run(int runtime, Vector processVector, Results result) {
        int i;
        int comptime = 0;
        int currentProcess = 0;
        int size = processVector.size();
        int completed = 0;
        String resultsFile = "Summary-Processes";

        result.schedulingType = "preemptive";
        result.schedulingName = "Shortest Job First";
        try {

            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            sProcess process;
            int min = 1000;
            for (i = size - 1; i >= 0; i--) {
                process = (sProcess) processVector.elementAt(i);
                if (process.cpudone < process.cputime && process.ioblocking < min) {
                    min = process.ioblocking;
                    currentProcess = i;
                }
            }
             process = (sProcess) processVector.elementAt(currentProcess);
            out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
            while (comptime < runtime) {
                if (process.cpudone == process.cputime) {
                    completed++;
                    out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                    if (completed == size) {
                        result.compuTime = comptime;
                        out.close();
                        return;
                    }
                     min = 1000;
                    for (i = size - 1; i >= 0; i--) {
                        process = (sProcess) processVector.elementAt(i);
                        if (process.cpudone < process.cputime && process.ioblocking < min) {
                            min = process.ioblocking;
                            currentProcess = i;
                        }
                    }
                    process = (sProcess) processVector.elementAt(currentProcess);
                    out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                }
                if (process.ioblocking == process.ionext) {
                    out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                    process.numblocked++;
                    process.ionext = 0;
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
        result.compuTime = comptime;
    }
}
