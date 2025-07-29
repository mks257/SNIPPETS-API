# Snippr API

## Project Overview

Snippr is a simple and efficient RESTful API for storing and retrieving code snippets. This project is built using **Java** and the **Spring Boot** framework, providing a robust backend for managing a collection of code examples.

This MVP (Minimum Viable Product) uses an in-memory data store, meaning the snippets will reset every time the application is restarted.

## Prerequisites

Before you can run this project, you will need the following installed on your system:

* **Java Development Kit (JDK)** - Version 17 or higher is recommended.
* **Apache Maven** - A build automation tool used for Java projects. VS Code's Java extensions can often manage this for you.
* **Visual Studio Code** - The code editor.
* **Extension Pack for Java** - The official Microsoft extension pack for Java development in VS Code.

## How to Run the Application

Follow these steps to get the API server running on your local machine.

### 1. Project Structure

Ensure your project files are arranged correctly. The `pom.xml` file **must** be in the root directory of the project.


snippr-api/
├── .vscode/
├── src/
│   └── SnipprApplication.java
└── pom.xml  

### 2. Open in VS Code

Open the root folder (`snippr-api`) in Visual Studio Code. The Java extensions will automatically detect the `pom.xml` file and begin setting up the project and downloading the required Spring Boot libraries.

### 3. Run the Application

Once the project has been set up (the red error lines in the Java file should disappear), you can start the server:

* Open the `SnipprApplication.java` file.
* Click the **"Run"** button that appears directly above the `public static void main(String[] args)` method.
* The **TERMINAL** panel will open and display the Spring Boot startup logs.
* The application is ready when you see a line similar to: `Tomcat started on port(s): 8080 (http)`

Your API is now running at `http://localhost:8080`.

## API Endpoints

You can interact with the API using a tool like Postman, Insomnia, or `curl`.

---

### Get All Snippets

Retrieves a list of all stored code snippets. Can optionally be filtered by programming language.

* **URL:** `/snippets`
* **Method:** `GET`
* **Query Parameters (Optional):**
    * `lang` - Filters snippets by language (e.g., `?lang=Python`)
* **Success Response:**
    * **Code:** 200 OK
    * **Content:** An array of snippet objects.

**Example with `curl`:**
```bash
# Get all snippets
curl http://localhost:8080/snippets

# Get only Python snippets
curl "http://localhost:8080/snippets?lang=Python"

Get Snippet by ID
Retrieves a single code snippet by its unique ID.

URL: /snippets/{id}

Method: GET

Success Response:

Code: 200 OK

Content: A single snippet object.

Error Response:

Code: 404 Not Found (if the ID does not exist)

Example with curl:

curl http://localhost:8080/snippets/1

Create a New Snippet
Adds a new code snippet to the data store.

URL: /snippets

Method: POST

Headers:

Content-Type: application/json

Body (raw JSON):

{
    "language": "Your Language",
    "code": "Your code snippet here"
}

{
    "id": 1,
    "language": "Python",
    "code": "print('Hello, World!')"
}

Success Response:

Code: 201 Created

Content: The newly created snippet object, including its new ID.

Example with curl:
curl -X POST http://localhost:8080/snippets \
-H "Content-Type: application/json" \
-d '{"language": "SQL", "code": "SELECT * FROM users;"}'
