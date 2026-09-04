                    # cohort-9-java-7944-taha
Cohort 9 — JAVA Fullstack (JAVA+ReactJS) assignment for Taha Shafiq


# Contact Management System

This Contact Management System was developed by me as part of my Java Backend Development Internship at 10Pearl.

The purpose of this project was to create an application where users can register, sign in, manage their profile, and manage their contacts. I also implemented an admin section where the admin can view information about users.

During the development of this project, I worked on the backend as well as the frontend. I implemented the database structure, DTOs, repositories, services, REST APIs, authentication and authorization, exception handling, Google OAuth, Swagger, logging, testing, and SonarQube analysis.

## 1. Creating Entities

The first thing I worked on was creating the entities required for the application.

I created two main entities:

* UserEntity
* ContactEntity

                     UserEntity

I created "UserEntity" to store information about the user in the database.

It contains fields related to the user's account and profile, such as:

* User ID
* Username
* First Name
* Last Name
* Email
* Password
* Created At
* Deleted At

The `UserEntity` is mapped to the user table in the database and is used whenever I need to store or retrieve user information.

        ContactEntity

After creating the user entity, I created `ContactEntity`.

I used `ContactEntity` to store information about contacts in the database.

It contains fields such as:

* Contact ID
* User ID
* Username
* Phone Number
* Phone Label
* Created At
* Updated At

Each contact is associated with a particular user. This allows the application to retrieve the contacts belonging to the currently logged-in user.


Like there is one to many mapping between user and contact like with respect to each user there are multiple contact entities
Moreever i have added jpa validations annotations using unique , notnull , updatable =false etc
and my database connection is with postgresql 
## 2. Creating DTOs

After creating the entities, I created DTOs for handling data between the frontend and backend.

I created the following DTOs:

* SignupDto
* SigninDto
* AdminDashboardDto
* PostContactDto
* GetUserDto

I used DTOs instead of directly exposing the entities through the APIs. This gives me better control over which fields are received from the client and which fields are returned in API responses.

  SignupDto

I created `SignupDto` for the signup functionality.

It contains the fields required when a new user creates an account.

The signup data is received through this DTO and then processed by the service layer before creating a `UserEntity`.

I have also add validations like restrictions like password must follow that specific format 
username should be at least of that character like minimum three characters and maximum 20 characters


                              SigninDto

I created `SigninDto` for the signin functionality.

It contains the fields required when an existing user wants to log in, such as username and password.

The signin information is passed to the authentication process to verify the user.

  - >   AdminDashboardDto

I created `AdminDashboardDto` for the admin dashboard.

This DTO contains the fields that I want to show about each user on the admin page.

Instead of returning the complete `UserEntity`, I use this DTO to control the information displayed on the admin dashboard.


->    PostContactDto

I created `PostContactDto` for creating a new contact.

It contains the fields that are required when a user adds a contact.

The data from this DTO is processed by the service and then used to create a `ContactEntity`.

- >  GetUserDto

I created `GetUserDto` for returning user profile information.

When a user opens their profile, the application returns the required profile information through this DTO.

It contains fields such as:

* User ID
* First Name
* Last Name
* Email
* Created At
* Deleted At

This allows me to return only the information required by the frontend.

--- > 3. Creating Repositories

After creating the entities and DTOs, I created repositories for database operations.

I created repositories for the main entities, including:

* UserRepository
* ContactRepository

I used Spring Data JPA repositories to communicate with the database.

The repository layer is responsible for operations such as finding users, saving users, finding contacts, saving contacts, updating contacts, and deleting contacts.

- > UserRepository is used to deal with data that is coming from the database and sent to the database regarding the User Information containing table
  I have used almost default methods as provided by Springboot But I have Created Three query method by myself 
  first for finding User by UserName 
  2nd for finding User by Email
  3rd for distinguishing whether the request is commit from Google Auth or Sigin or normal Email or Username Signin person
findByAuthProviderAndProviderId  - > to get info about user signin using google 

- > Contact Repository is used to deal with data that is coming from the database and sent to the database regarding the Contact Information containing table
  >
  > Addition Method that is created in this repository is findAllByUserName - > such that to get all contact Entities corresponding to a userName
  > 
## 4. Creating Services

After creating the repositories, I created service interfaces.

I created services for the user and contact functionality.

The service layer contains the main business logic of the application.

Some of the operations handled by the service layer include:

* Creating users
* Finding users
* Authenticating users
* Creating contacts
* Getting contacts
* Getting individual contacts
* Updating contacts
* Deleting contacts

I kept the business logic inside the service layer instead of putting it directly inside the controllers.

- > Moreever Service Layer or Package does not contain methods that is specifically used inside there implementations
It only contains or declare that methods that are called from the controllers
All the mappings that are done in Implementaion Classes are not declared there 
## 5. Service Implementations

After creating the service interfaces, I created their implementations.

For example:

* UserServiceImpl
* ContactServiceImpl
* JwtServiceImpl
The implementation classes contain the actual logic for the operations defined in the service interfaces.

The controllers call the service methods, and the service implementations communicate with the repositories.

This keeps the application separated into different layers and makes the code easier to maintain and test.

The Implementaion classes also contains mapping methods like
"mapToAdminContactDto" and "mapToContactDto" are used Inside the "ContactServiceImpl" like to map the Entities with the Dtos
like first mapping method maps the contactEntity to the fields used inside the AdminDashboard and the later one for the fields that are shown in the Contact Info of the person

while "UserServiceImpl" has three mapping like "maptoDto" "maptoEntity" and "maptoLoginDto" 

third class is used to implement or handle jwt related config like as the bridge between jwtutils where token generation validation related stuff is done and to the controller


## 6. Creating Controllers

Beside implementing the services, I created the controllers for exposing the REST APIs.

The controllers receive requests from the frontend and call the appropriate service methods.

Like I Divide My Controllers In Four Category

 -  >  UserController

> this is for the Apis that a person can used to Interact with it profile like update it profile get it profile

This Basically has three end points 
. GetById
. GetByUserName -> and UserName is taken form Spring Boot Authentication Class
. ChangeUser

-  > ContactController

> this is for the Apis that a person can used to Interact with its contacts like update its contact, Delete any of the Contact, Get or Add Any of the Contact

This Basically has five end points 
. GetContactById -> to get a particular contact by its Id
. GetContactOfUserName -> and UserName is taken form Spring Boot Authentication Class and this is used to get all the contact of the User
. ChangeContact - > to update the contact by its Id
. DeleteContact - > to delete The contact By its Id


 -  > PublicController

> this is for the Apis that any one can access like
. Signup - > to register  a  user in the application to create contacts
. SignIn - > to login a user or simply authenticate the user to see or access its profile or contacts like by calling that controller with correct details like username password or email password combo result in generating Jwt Token to access the authenticated endpoints
-  > Admin Controller

> this is for Apis that only admin can see like
. GetAllUser - > Information of all the User login or registered inside the App
. GetAllContactOfUser - > All the contacts corresponding to a particular user inside the App
. DeleteUser - > Delete any particular user


The controllers are mainly responsible for handling HTTP requests and responses, while the actual business logic is handled by the service layer.

## 7. Global Exception Handling

After implementing the basic functionality, I worked on exception handling.

I created a global exception handler so that exceptions can be handled centrally instead of writing separate exception handling code in every controller.

I also created a custom `ResourceNotFoundException`.

I use this exception when a requested resource does not exist.

For example, if a user tries to access a contact that does not exist, the application throws the appropriate exception and the global exception handler returns a proper response.

This helped me keep the controllers cleaner and provide consistent error responses.

## 8. Validation

I also implemented validation for incoming requests.

Validation is used to make sure that the data received from the frontend follows the required rules.

For example, required fields should not be empty and input should follow the expected format.

If invalid data is received, the application returns an appropriate validation error instead of processing incorrect data.

## 9. Spring Security

After implementing the basic APIs, I worked on securing the application using Spring Security.

I used Spring Security to protect the APIs that should only be accessible to authenticated users.

I worked with components such as:

* Security Configuration
* AuthenticationManager
* BCryptPasswordEncoder
* JwtFilter
* JwtUtils

## 10. Password Encryption

I used `BCryptPasswordEncoder` to encrypt user passwords before storing them in the database.

I did not store passwords as plain text.

When a user signs up, the password is encoded before being saved.

When the user signs in, the provided password is checked against the stored encoded password.

## 11. JWT Authentication

I implemented JWT-based authentication for securing the application.

When a user successfully signs in, the backend generates a JWT token.

The frontend then uses this token when making requests to protected APIs.

The JWT is sent with the request, and the backend validates the token before allowing access to the protected endpoint.

## 12. JWT Filter

I created a JWT filter to process JWT tokens from incoming requests.

The filter checks the authorization header, extracts the token, validates it, and sets the authentication information when the token is valid.

This allows Spring Security to identify the authenticated user when accessing protected APIs.


I did not hard-code the JWT secret key directly into the source code.

I stored the JWT key as an environment variable and accessed it through the application configuration.

This prevents sensitive information from being exposed in the source code or committed to GitHub.

## 14. Google OAuth

I also implemented Google OAuth authentication.

This allows users to authenticate using their Google account.

For Google OAuth, I used a Google Client ID and Google Client Secret.

I stored these values as environment variables instead of putting them directly into the source code.

This helped me understand how third-party authentication works and how OAuth can be integrated into a Spring Boot application.

I have managed This inside a "serviceImpl" by creating a method "manageGoogleUser" to verify or check whether is request is coming from the user that is sigin using google or using Email or Username like if request is coming from the Google then I build the UserEntity Corresponding to that user Such that there is a class

-  >  GoogleOauthSuccessHandler

Such that this class got executed when there is successful login from the google and this manages how data is retrived from the google response and send that data like "email" "givenname" "familyname" and also manages the json that is returned or shows on the page that google redirect us after successful login



## 15. Swagger Integration

I integrated Swagger/OpenAPI into the backend for API documentation and testing.

Swagger allows me to view all available APIs, their request parameters, request bodies, responses, and other API information.

It also allows me to test the APIs directly from the Swagger UI.

This made API development and testing easier during the project.

For this I have created "SwaggerConfig" Class 
In this class I have added Tags Corresponding to my Controllers to visualize it on broswer
For that I have added @Operation and @ApiResponse annotation before each Api to get usefull information like which Api
does what in SwaggerUi

## 16. Logging Using SLF4J

I implemented logging using SLF4J.

I used logging instead of relying only on `System.out.println()`.

Logging helped me track important operations and understand what was happening inside the application during development and testing.

I used logs for important events, debugging, and identifying problems when something was not working as expected.

                                     Frontend Development

After working on the backend, I also created the frontend pages required for the application.

I created separate pages for different parts of the system.

The main pages include:

* Home Page
* Get Started Page
* Signup Page
* Signin Page
* Admin Login Page
* Admin Dashboard
* User Dashboard
* User Profile
* Contact Management

-  >  18. Home Page

I created the home page as the main starting page of the application.

It provides basic information about the application and gives users options to get started or move to the appropriate section.

-  >  19. Get Started Page

I created a Get Started page where users can choose whether they want to create a new account or sign in to an existing account or If it is Logined using google so there is also signIn with google option .


-  > 20. Signup Page

I created the signup page for new users.

The user enters the required information and submits the form.

The frontend sends this information to the signup API, where it is received through `SignupDto` and processed by the backend.

-  >   Signin Page

I created the signin page for existing users.

The user provides their username and password.

The frontend sends the credentials to the signin API.

After successful authentication, the backend generates the JWT that is used for accessing protected APIs.

There is also the Admin Login Button such that if user is Admin It can Directed to Admin Login Page

-  >   Admin Login Page

I created a separate admin login page.

The purpose of this page is to provide access to the administrative section of the application.

After successful authentication, the administrator can access the admin dashboard.

-  >   Admin Dashboard

I created an admin dashboard where information about users can be displayed.

The data for the dashboard is handled using `AdminDashboardDto`.

The admin dashboard provides a separate interface from the normal user dashboard.

Like there are two options one simple list all the User login in the App such that after clicking on individual User
all the contacts Created by that user is shown

And there is All contact OPtion to get to know about all existing contacts inside the application

There is also button to delete User if Admin Desire 

-  >   User Dashboard

I created a separate dashboard for users.

The dashboard displays information and functionality related to the currently logged-in user.

Users can access their profile and manage their contacts from their dashboard.

User can delete it contact update its profile add contact operations 
Like there is two Options
. First option is profile Button such that user can see its profile
    user can edit or modify its profile there
. Second Option is Contacts like User can See its Contacts in that Screen like User can delete or Update it contact or also      add the contacts

- >   Contact Management Frontend

I created the frontend functionality for managing contacts.

Users can:

* Add contacts
* View contacts
* View individual contacts
* Update contacts
* Delete contacts

The frontend communicates with the Spring Boot REST APIs to perform these operations.

-  >  26. CSS Files

I created CSS files for the different frontend pages.

I used CSS to design and style the:

* Home page
* Get Started page
* Signup page
* Signin page
* Admin login
* Admin dashboard
* User dashboard
* Contact sections
* Forms
* Buttons
* Navigation
* Other UI components

I kept the CSS separated from the HTML so that the frontend code is easier to maintain.

## 27. Unit Testing

After implementing the main functionality, I worked on unit testing.

I used JUnit and Mockito for testing different parts of the application.

I wrote tests for successful scenarios as well as error scenarios.

I tested things such as:

* Service methods
* Controller methods

-  >  28. Mockito

I used Mockito to mock dependencies during unit testing.

For example, when testing a service, I can mock the repository instead of connecting to the actual database.

This allows me to test the service logic independently.

Mockito helped me create isolated unit tests where I could control the behavior of dependencies and verify the expected results.

-  >  29. Service Testing

I have Tested almost all the service implementation tests 
For that i have taken help of MockitoBean to create dummy classes repository etc
I have also created classes to provide me data like regarding user contact such that i can test it or perform operation like store that data fakely or update it or delete it like using when()->ThenReturn mechanism is my best friend in this regard

for providing me with dummy data i have created more classes like "UserServiceMethodSource" "UserDetailsProvider"
and also classes for providing me data regarding contactsDetials

>> UserServiceTestClass - > In that class I have Tested "UserServiceImpl" Methods
>> UserServiceMethodSourceClass - > In This Class I have created method to provide us with dummy data for testing update user funcationality
>> UserDetailsProvider - > In this Class I have created method to provide us with dummy UserEntity to check like createUser method testing

>> ContactServiceTestClass - > In that class I have Tested "ContactServiceImpl" Methods
>> ContactServiceMethodSourceClass - > In This Class I have created method to provide us with dummy data for testing update Contact funcationality
>> ContactDetailsProvider - > In this Class I have created method to provide us with dummy ContactEntity to check like createContact method testing

-  > Controller Testing 

I also worked on controller testing.
But It is a bit difficult or new experience for me Like I have done or write  all the service methods test Because I have knowledge of it so I started but cannot understand it and because of shortage of Time I cannot Implement Any of the ControllerTesting Method

--  >  30. SonarQube

After working on the application and tests, I worked on code-quality analysis using SonarQube.

I used SonarQube to analyze my project and identify problems in the code.

SonarQube helped me find things such as:

 Bugs
 Code smells
 Security issues
 Duplicated code
 Maintainability issues
 Test-related issues

-  >  Running SonarQube Using Docker

I started SonarQube using Docker.

I created and ran a SonarQube Docker container and accessed the SonarQube server locally.

After setting up SonarQube, I configured my project so that the project could be analyzed by SonarQube.

-  >  SonarQube Key and Project Analysis

I generated the required authentication key/token from SonarQube and used it for analyzing the project.

I then ran the SonarQube analysis using Maven.

The analysis generated a report showing different issues and code-quality information about my project.

I reviewed the issues reported by SonarQube and worked on fixing them.

After making changes, I ran the tests again and performed another analysis to check the improvements.

Like Most Common Mistakes are

- > @Autowired annotation using like it is then replaced by using constructor overloading
- > RemoveUnused Imports
- > Remove CommentedOutCode
- > Remove Unneccessary Exception Throwing
- > Using Standard naming conventions for variables method name and classes
-  >   Overall Development Process

I developed the project in multiple stages.

First, I created the `UserEntity` and `ContactEntity` for storing the required information in the database.

Then I created the DTOs required for signup, signin, admin dashboard, creating contacts, and returning user information.

After that, I created repositories for database operations, service interfaces for business operations, service implementations for the actual logic, and controllers for exposing REST APIs.

Once the main functionality was working, I implemented validation and global exception handling.

After that, I worked on Spring Security, password encryption, JWT authentication, and the JWT filter.

I then integrated Google OAuth and stored the Google Client ID, Google Client Secret, and JWT secret as environment variables instead of hard-coding them.

I integrated Swagger/OpenAPI for API documentation and testing and added logging using SLF4J.

On the frontend side, I created the Home, Get Started, Signup, Signin, Admin Login, Admin Dashboard, User Dashboard, Profile, and Contact Management pages along with the required CSS files.

After implementing the application, I wrote unit tests and controller tests using JUnit and Mockito.

Finally, I set up SonarQube using Docker, generated the required key, analyzed the project, reviewed the reported issues, fixed the problems, and ran the analysis again.

-  >  34. Technologies Used

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* JWT
* Google OAuth
* PostgreSQL
* Maven
* Swagger/OpenAPI
* JUnit
* Mockito
* SLF4J
* SonarQube
* Docker
* Git
* GitHub
* HTML
* CSS
* JavaScript

-  >  35. What I Learned

This project gave me practical experience in developing a complete application instead of only working with individual technologies.

Through this project, I learned how to:

* Design entities and database structures
* Use DTOs
* Build REST APIs
* Implement layered architecture
* Work with repositories and services
* Handle exceptions globally
* Validate API requests
* Secure APIs using Spring Security
* Implement JWT authentication
* Encrypt passwords using BCrypt
* Integrate Google OAuth
* Store sensitive configuration using environment variables
* Document APIs using Swagger
* Add application logging using SLF4J
* Write unit tests using JUnit and Mockito
* Test controllers
* Build frontend pages
* Connect frontend with backend APIs
* Analyze code using SonarQube
* Fix code-quality issues
* Work with Docker
* Manage the project using Git and GitHub

                                               Conclusion

This Contact Management System was one of my main practical projects during my internship at 10Pearl.

I worked on the project from the database and backend structure to authentication, frontend development, testing, and code-quality analysis.

The project helped me understand how different parts of a real application work together, especially how entities, DTOs, repositories, services, controllers, security, frontend pages, testing, and code-quality tools fit into one complete application.

It also gave me practical experience with professional backend development practices and helped me improve my understanding of Java and Spring Boot development.
