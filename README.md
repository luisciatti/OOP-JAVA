# 📚 Java OOP Project

A Java application developed using Object-Oriented Programming (OOP) principles as part of coursework for a Bachelor’s Degree in Computer Science (BCC).

The project follows a modular architecture inspired by the MVC (Model–View–Controller) pattern, promoting separation of concerns, maintainability, and scalability.

All classes and methods are documented using JavaDoc, ensuring clear and standardized documentation for developers.

🧩 Project Architecture

The project structure is organized to separate responsibilities across different layers of the application.

src
 ├── controller
 │    └── Main.java
 │
 ├── model
 │    └── (Domain classes and business logic)
 │
 ├── view
 │    └── (User interface and interaction)
 │
 └── test
      └── (Testing and validation classes)
Controller

Responsible for controlling the application flow.

Main responsibilities:

Application initialization

Communication between Model and View

Managing program execution logic

Example:

controller/Main.java
Model

Contains the core business logic and domain entities of the application.

Responsibilities may include:

Entity classes

Business rules

Data structures

Core application state

Examples:

model/User.java
model/Product.java
model/Service.java
View

Handles user interaction and data presentation.

Responsibilities include:

Displaying information to the user

Receiving user input

Formatting outputs

Examples:

view/MenuView.java
view/ConsoleView.java
Test

Contains testing classes used to validate system behavior.

Tests may include:

Functional testing

Method validation

Simulation of system scenarios

Examples:

test/UserTest.java
test/ApplicationTest.java
🧠 Object-Oriented Concepts Applied

This project demonstrates the use of core Object-Oriented Programming principles:

Encapsulation – protecting object state through controlled access

Inheritance – reuse of common logic between classes

Polymorphism – flexible behavior through method overriding

Abstraction – simplified interfaces for complex logic

Additional software design practices:

Separation of concerns

Modular architecture

Documentation-driven development

📖 Documentation (JavaDoc)

All classes and public methods include JavaDoc documentation.

To generate the documentation locally:

javadoc -d docs -sourcepath src -subpackages controller model view

This command generates the documentation in the docs/ folder.

Open the following file in your browser:

docs/index.html

This will display the full interactive documentation.

▶️ Running the Project
1. Clone the repository
git clone https://github.com/your-username/your-repository.git
2. Navigate to the project folder
cd your-repository
3. Compile the project
javac src/controller/Main.java
4. Run the application
java src.controller.Main
🧪 Running Tests

Test classes are located in:

src/test

You can compile and run tests individually.

Example:

javac src/test/TestExample.java
java src.test.TestExample
🛠 Technologies

Java

Object-Oriented Programming

JavaDoc

MVC-inspired architecture

📊 Possible Future Improvements

Add JUnit automated tests

Implement GUI interface (JavaFX / Swing)

Add data persistence (file or database)

Improve test coverage

🎓 Academic Context

This project was developed as part of coursework in a Bachelor's Degree in Computer Science (BCC).
The objective is to apply object-oriented design principles, structured project organization, and software documentation practices using Java.

👨‍💻 Author

Student: Luis Ciatti
Program: Bachelor’s Degree in Computer Science (BCC) - FURB
