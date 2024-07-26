import java.util.*;

class User {
    String username;
    Set<User> connections;

    public User(String username) {
        this.username = username;
        this.connections = new HashSet<>();
    }

    public void addConnection(User user) {
        connections.add(user);
        user.connections.add(this);
    }

    public Set<User> getConnections() {
        return connections;
    }
}

public class SocialNetwork {

    private static Map<String, User> userMap = new HashMap<>();

    public static void main(String[] args) {
        // Example usage
        createUser("Alice");
        createUser("Bob");
        createUser("Janice");
        createUser("Eve");

        createConnection("Alice", "Bob");
        createConnection("Bob", "Janice");
        createConnection("Alice", "Eve");
        createConnection("Eve", "Bob");

        System.out.println("Alice's connections: " + getConnections("Alice"));
        System.out.println("Bob's connections: " + getConnections("Bob"));
        System.out.println("Common connections of Alice and Bob: " + getCommonConnections("Alice", "Bob"));
        System.out.println("Connection level between Alice and Janice: " + getConnectionLevel("Alice", "Janice"));
        System.out.println("Connection level between Alice and Bob: " + getConnectionLevel("Alice", "Bob"));
        System.out.println("Connection level between Alice and unknown: " + getConnectionLevel("Alice", "unknown"));
    }

    public static void createUser(String username) { // this function adds the new users to the network if they do not
                                                     // exist prior
        userMap.putIfAbsent(username, new User(username));
    }

    public static void createConnection(String username1, String username2) { // establishing the connections using
                                                                              // hashmap
        User user1 = userMap.get(username1);
        User user2 = userMap.get(username2);
        if (user1 != null && user2 != null) {
            user1.addConnection(user2);
        }
    }

    public static Set<String> getConnections(String username) { // retrieving all the friends of the given user
        User user = userMap.get(username);
        if (user != null) {
            Set<String> connectionNames = new HashSet<>();
            for (User connection : user.getConnections()) {
                connectionNames.add(connection.username);
            }
            return connectionNames;
        }
        return Collections.emptySet();
    }

    public static Set<String> getCommonConnections(String username1, String username2) { // finding mutual friends
                                                                                         // between the users
        User user1 = userMap.get(username1);
        User user2 = userMap.get(username2);
        if (user1 != null && user2 != null) {
            Set<String> commonConnections = new HashSet<>();
            for (User connection : user1.getConnections()) {
                if (user2.getConnections().contains(connection)) {
                    commonConnections.add(connection.username);
                }
            }
            return commonConnections;
        }
        return Collections.emptySet();
    }

    public static int getConnectionLevel(String username1, String username2) { // this function finds the level that is
                                                                               // the distance between the users
        User user1 = userMap.get(username1);
        User user2 = userMap.get(username2);
        if (user1 == null || user2 == null) {
            return -1;
        }
        if (user1 == user2) {
            return 0;
        }

        Queue<User> queue = new LinkedList<>();
        Set<User> visited = new HashSet<>();
        Map<User, Integer> levels = new HashMap<>();

        queue.add(user1);
        visited.add(user1);
        levels.put(user1, 0);

        while (!queue.isEmpty()) {
            User current = queue.poll();
            int currentLevel = levels.get(current);

            for (User connection : current.getConnections()) {
                if (!visited.contains(connection)) {
                    levels.put(connection, currentLevel + 1);
                    if (connection == user2) {
                        return currentLevel + 1;
                    }
                    queue.add(connection);
                    visited.add(connection);
                }
            }
        }
        return -1; // No connection found
    }
}

// Time Complexity : O(n + e) as the BFS operation is performed visiting 'n'
// users and traversing among 'e' connections(in the WORST CASE)
// Space Complexity : O(n) Because of the use of queue, set, map for n users