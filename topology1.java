import java.util.*;

 class TaskScheduler {

    // Function to perform topological sort using Kahn's Algorithm (BFS)
    public static List<String> scheduleTasks(int numTasks, List<List<String>> dependencies) {
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        
        // Initialize the graph and in-degree map
        for (int i = 0; i < numTasks; i++) {
            String task = "Task" + i; // This will be Task0, Task1, Task2, ...
            graph.put(task, new ArrayList<>());
            inDegree.put(task, 0); // Initially, all tasks have in-degree of 0
        }

        // Build the graph
        for (List<String> dependency : dependencies) {
            String task1 = dependency.get(0);
            String task2 = dependency.get(1);
            if (!graph.containsKey(task1) || !graph.containsKey(task2)) {
                System.out.println("Invalid task names. Please ensure they are in the format 'TaskX TaskY' and within the task list.");
                return new ArrayList<>();
            }
            graph.get(task1).add(task2);
            inDegree.put(task2, inDegree.get(task2) + 1);
        }

        // Initialize queue
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        // Perform topological sort
        List<String> schedule = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            String task = queue.poll();
            schedule.add(task);
            for (String dependentTask : graph.get(task)) {
                inDegree.put(dependentTask, inDegree.get(dependentTask) - 1);
                if (inDegree.get(dependentTask) == 0) {
                    queue.offer(dependentTask);
                }
            }
        }

        if (schedule.size() != numTasks) {
            System.out.println("Cycle detected! Task scheduling is not possible.");
            return new ArrayList<>();
        }

        return schedule;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input number of tasks
        System.out.print("Enter the number of tasks: ");
        int numTasks = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        // List to store task dependencies
        List<List<String>> dependencies = new ArrayList<>();

        // List of valid task names
        List<String> validTasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            validTasks.add("Task" + i);
        }

        // Input task dependencies
        System.out.println("Enter the task dependencies in the format 'TaskX TaskY' where TaskX must be completed before TaskY:");
        System.out.println("Enter 'done' when finished.");

        while (true) {
            System.out.print("Enter a dependency (or 'done' to finish): ");
            String input = scanner.nextLine();
            if (input.equals("done")) {
                break;
            }

            String[] taskPair = input.split(" ");
            if (taskPair.length == 2) {
                String task1 = taskPair[0];
                String task2 = taskPair[1];

                // Validate task names
                if (!validTasks.contains(task1) || !validTasks.contains(task2)) {
                    System.out.println("Invalid task names. Please ensure they are in the format 'TaskX TaskY' and within the task list.");
                } else {
                    dependencies.add(Arrays.asList(task1, task2));
                }
            } else {
                System.out.println("Invalid input. Please enter the dependency in the format 'TaskX TaskY'.");
            }
        }

        // Measure execution time
        long startTime = System.nanoTime();

        // Call the function to schedule tasks
        List<String> schedule = scheduleTasks(numTasks, dependencies);

        long endTime = System.nanoTime();
        long durationInNanoSeconds = (endTime - startTime);  // Duration in nanoseconds
        long durationInMillis = durationInNanoSeconds / 1000000;  // Convert nanoseconds to milliseconds

        // Print the resulting schedule if no cycle detected
        if (!schedule.isEmpty()) {
            System.out.println("Task Schedule (Topological Order): " + schedule);
        }

        // Print execution time in milliseconds
        System.out.println("Execution Time: " + durationInMillis + " milliseconds");

        scanner.close();
    }
}
 
