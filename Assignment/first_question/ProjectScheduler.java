import java.util.*;

class Task { // class definition
    String name;
    int duration;
    List<Task> dependencies = new ArrayList<>();
    int EST = 0;
    int EFT = 0;
    int LST = Integer.MAX_VALUE;
    int LFT = Integer.MAX_VALUE;

    Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }
}

public class ProjectScheduler {

    private static void calculateForwardPass(Task task) { // calculates the earliest start and finish time recursively
        for (Task dependency : task.dependencies) {
            if (dependency.EFT == 0) { // If EFT is not yet calculated
                calculateForwardPass(dependency);
            }
            task.EST = Math.max(task.EST, dependency.EFT);
        }
        task.EFT = task.EST + task.duration;
    }

    private static void calculateBackwardPass(Task task, int projectDuration) { // Recursive calculation of latest start
                                                                                // and end time
        if (task.LFT == Integer.MAX_VALUE) {
            task.LFT = projectDuration;
        }
        task.LST = task.LFT - task.duration;
        for (Task dependency : task.dependencies) {
            dependency.LFT = Math.min(dependency.LFT, task.LST);
            if (dependency.LFT == Integer.MAX_VALUE) { // If LFT is not yet calculated
                calculateBackwardPass(dependency, projectDuration);
            }
        }
    }

    private static void calculateTimes(Map<String, Task> tasks, Task startTask) { // this function calculates the
                                                                                  // overall process
        // Forward Pass
        calculateForwardPass(startTask);

        // Get the project duration
        int projectDuration = 0;
        for (Task task : tasks.values()) {
            projectDuration = Math.max(projectDuration, task.EFT);
        }

        // Backward Pass
        for (Task task : tasks.values()) {
            if (task.dependencies.isEmpty()) {
                calculateBackwardPass(task, projectDuration);
            }
        }

        // Print the earliest and latest completion times
        System.out.println("Earliest completion time: " + projectDuration);
        System.out.println("Latest completion time: " + projectDuration);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Task> tasks = new HashMap<>();

        System.out.println("Enter the number of tasks:");
        int numTasks = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numTasks; i++) {
            System.out.println("Enter task name and duration:");
            String[] taskInput = scanner.nextLine().split(" ");
            String taskName = taskInput[0];
            int duration = Integer.parseInt(taskInput[1]);

            Task task = new Task(taskName, duration);
            tasks.put(taskName, task);
        }

        System.out.println("Enter the number of dependencies:");
        int numDependencies = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numDependencies; i++) {
            System.out.println("Enter dependency (format: task dependency):");
            String[] dependencyInput = scanner.nextLine().split(" ");
            String taskName = dependencyInput[0];
            String dependencyName = dependencyInput[1];

            Task task = tasks.get(taskName);
            Task dependency = tasks.get(dependencyName);

            if (task != null && dependency != null) {
                task.dependencies.add(dependency);
            }
        }

        Task startTask = tasks.get("T_START");
        if (startTask == null) {
            startTask = new Task("T_START", 0);
            tasks.put("T_START", startTask);
        }

        for (Task task : tasks.values()) {
            if (task.dependencies.isEmpty() && !task.name.equals("T_START")) {
                startTask.dependencies.add(task);
            }
        }

        calculateTimes(tasks, startTask);
    }
}

// Time Complexity : By considering the time complexity of Task creation O(n) +
// dependency O(d) + forward pass O(n^2) + Backward pass O(n^2) + Completion
// Time O(n) = O(n^2)
// Space complexity : O(n + d) for 'n' tasks and 'd' dependencies