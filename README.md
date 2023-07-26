## **Authors**
* Name: Ehab Qeisi Email:ehabqe@edu.hac.ac.il 
* Name: lammah jaber Email: lammahja@edu.hac.ac.il



## **User Registration System**
This project is a web application that implements a user registration system with an admin backend using Spring Boot MVC,
Thymeleaf, JPA, and MySQL as the database. It provides a complete solution for user management, including user registration,
authentication, profile management, and administrative tasks such as enabling, disabling, and deleting user accounts.

## **Features**
* User Registration: Users can create an account by providing their details, including username, email, and password. The passwords are securely hashed and stored in the database.

* User Authentication: Users can log in to their accounts using their credentials. The system verifies the provided information and grants access to authenticated users.

* User Profile Management: Authenticated users can update their profile information, such as username, email, and password. They can also view their profile details.

* Admin Backend: Administrators have access to a separate admin backend where they can manage user accounts. They can enable or disable user accounts, delete accounts, and perform other administrative tasks.

* Role-based Authorization: The system implements role-based authorization to restrict access to certain functionalities. Only administrators have access to the admin backend, while regular users can access their own profile and perform limited actions.

* Security: The project uses Spring Security to handle user authentication and authorization. Passwords are securely encoded and stored using a cryptographic algorithm.

* Data Persistence: User and role information is stored in a MySQL database using JPA. The project uses Spring Data JPA for database operations, including querying and manipulating user data.

## **Technologies Used**
1. Java
2. Spring Boot MVC
3. Thymeleaf
4. Spring Data JPA
5. MySQL
6. Spring Security
7. Maven

## **Useful information**
* Admin UserName is : admin
* Admin Password is : password