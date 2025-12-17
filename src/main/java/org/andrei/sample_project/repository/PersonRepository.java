package org.andrei.sample_project.repository;

import org.andrei.sample_project.connection.ConnectionFactory;
import org.andrei.sample_project.model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {
    private final Connection connection;

    public PersonRepository() {
        this.connection = ConnectionFactory.getConnection();
    }

    // Create a person and return the updated person object with the generated id
    public Person createPerson(Person person) {
        String sql = "INSERT INTO person (first_name, last_name, age) VALUES (?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set the values for the prepared statement
            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setInt(3, person.getAge());

            // Execute the statement and retrieve the generated id
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Get the auto-generated id and set it in the person object
                    long generatedId = rs.getLong("id");
                    person.setId(generatedId);
                    System.out.println("Person created with ID: " + generatedId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    // Edit an existing person and return the updated person object
    public Person editPerson(Person person) {
        String sql = "UPDATE person SET first_name = ?, last_name = ?, age = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setInt(3, person.getAge());
            stmt.setLong(4, person.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Person edited with ID: " + person.getId());
            } else {
                System.out.println("No person found with ID: " + person.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    // Delete a person and return whether the deletion was successful
    public boolean deletePerson(Person person) {
        String sql = "DELETE FROM person WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, person.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Person deleted with ID: " + person.getId());
                return true; // Successfully deleted
            } else {
                System.out.println("No person found with ID: " + person.getId());
                return false; // No person with the given ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of an error, return false
        }
    }

    // Get all persons from the database and return a list of Person objects
    public List<Person> getAllPersons() {
        List<Person> personList = new ArrayList<>();
        String sql = "SELECT * FROM person";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");

                Person person = new Person(id, firstName, lastName, age);
                personList.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personList;
    }
}
