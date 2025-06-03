package com.Lukasz.MojSklep.Service;

import com.Lukasz.MojSklep.Model.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class UserService {
    private final String FILE_PATH = "users.csv";
    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        loadUsers();
    }

    public boolean registerUser(String username, String password) {
        if (username == null || password == null) return false;
        String key = username.toLowerCase();
        if (users.containsKey(key)) return false;

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        users.put(key, user);
        saveUserToFile(user);
        return true;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void saveUserToFile(User user) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(user.getUsername() + "," + user.getPassword() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    User user = new User();
                    user.setUsername(data[0]);
                    user.setPassword(data[1]);
                    users.put(data[0].toLowerCase(), user);  // klucz w małych literach
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUserByUsername(String username) {
        if (username == null) return null;
        return users.get(username.toLowerCase());
    }
}


