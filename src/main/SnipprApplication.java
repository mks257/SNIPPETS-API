package src.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// The @SpringBootApplication annotation enables auto-configuration and component scanning.
@SpringBootApplication
public class SnipprApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnipprApplication.class, args);
    }
}

// --- Data Model ---
// This is a simple Plain Old Java Object (POJO) to represent a code snippet.
class Snippet {
    private long id;
    private String language;
    private String code;

    // Constructors
    public Snippet() {}

    public Snippet(long id, String language, String code) {
        this.id = id;
        this.language = language;
        this.code = code;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}

// --- Controller Layer ---
// The @RestController annotation marks this class as a request handler.
// Spring will automatically handle JSON serialization and deserialization.
@RestController
class SnippetController {

    // --- In-Memory Data Store ---
    // Using a thread-safe map to store snippets and an AtomicLong for unique IDs.
    private final ConcurrentHashMap<Long, Snippet> snippets = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    // Constructor to pre-populate the data store with seed data.
    public SnippetController() {
        snippets.put(counter.incrementAndGet(), new Snippet(1, "Python", "print('Hello, World!')"));
        snippets.put(counter.incrementAndGet(), new Snippet(2, "Python", "def add(a, b):\n    return a + b"));
        snippets.put(counter.incrementAndGet(), new Snippet(3, "Python", "class Circle:\n    def __init__(self, radius):\n        self.radius = radius\n\n    def area(self):\n        return 3.14 * self.radius ** 2"));
        snippets.put(counter.incrementAndGet(), new Snippet(4, "JavaScript", "console.log('Hello, World!');"));
        snippets.put(counter.incrementAndGet(), new Snippet(5, "JavaScript", "function multiply(a, b) {\n    return a * b;\n}"));
        snippets.put(counter.incrementAndGet(), new Snippet(6, "JavaScript", "const square = num => num * num;"));
        snippets.put(counter.incrementAndGet(), new Snippet(7, "Java", "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}"));
        snippets.put(counter.incrementAndGet(), new Snippet(8, "Java", "public class Rectangle {\n    private int width;\n    private int height;\n\n    public Rectangle(int width, int height) {\n        this.width = width;\n        this.height = height;\n    }\n\n    public int getArea() {\n        return width * height;\n    }\n}"));
        // Set counter to the highest ID from the seed data
        counter.set(8);
    }


    /**
     * [GET] /snippets
     * Retrieves all code snippets.
     * BONUS: Can be filtered by programming language using a query parameter.
     * Example: /snippets?lang=JavaScript
     */
    @GetMapping("/snippets")
    public List<Snippet> getSnippets(@RequestParam(required = false) String lang) {
        if (lang != null && !lang.isEmpty()) {
            return snippets.values().stream()
                    .filter(snippet -> snippet.getLanguage().equalsIgnoreCase(lang))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(snippets.values());
    }

    /**
     * [GET] /snippets/{id}
     * Retrieves a single code snippet by its unique ID.
     * Returns a 404 Not Found error if the snippet does not exist.
     */
    @GetMapping("/snippets/{id}")
    public ResponseEntity<Snippet> getSnippetById(@PathVariable long id) {
        Snippet snippet = snippets.get(id);
        if (snippet != null) {
            return ResponseEntity.ok(snippet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * [POST] /snippets
     * Creates a new code snippet.
     * Expects a JSON body with 'language' and 'code' properties.
     */
    @PostMapping("/snippets")
    public ResponseEntity<Snippet> createSnippet(@RequestBody Snippet newSnippet) {
        // Basic validation
        if (newSnippet.getLanguage() == null || newSnippet.getCode() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Generate a new unique ID and set it on the object
        long newId = counter.incrementAndGet();
        newSnippet.setId(newId);
        
        // Save the new snippet to our in-memory store
        snippets.put(newId, newSnippet);
        
        // Respond with the newly created snippet and a 201 Created status
        return new ResponseEntity<>(newSnippet, HttpStatus.CREATED);
    }
}
