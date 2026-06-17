package com.example.Spring_boot_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.Spring_boot_demo.dao.Student;
import com.example.Spring_boot_demo.dao.StudentRepository;
import java.util.Random;

@SpringBootApplication(scanBasePackages = { "com.example" })
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}

    @Bean
    public CommandLineRunner loadData(StudentRepository repository) {
        return (args) -> {
            if (repository.count() == 0) {
                System.out.println("No students found. Injecting 20 mock records...");
                String[] firstNames = {"James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen"};
                String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"};
                String[] hobbies = {"Reading", "Gaming", "Swimming", "Music", "Photography", "Traveling", "Cooking", "Dancing", "Painting", "Sports"};
                String[] grades = {"Freshman", "Sophomore", "Junior", "Senior", "Graduate"};
                String[] genders = {"Male", "Female", "Other"};
                
                Random random = new Random();
                for (int i = 0; i < 20; i++) {
                    Student s = new Student();
                    s.setName(firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)]);
                    s.setEmail("student" + i + "@university.edu");
                    s.setAge(18 + random.nextInt(8));
                    s.setGender(genders[random.nextInt(genders.length)]);
                    s.setHobby(hobbies[random.nextInt(hobbies.length)]);
                    s.setGrade(grades[random.nextInt(grades.length)]);
                    repository.save(s);
                }
                System.out.println("20 records injected successfully.");
            }
        };
    }
}
