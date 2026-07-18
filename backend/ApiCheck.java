import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiCheck {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        // 1. Login as testpravin99
        String loginJson = "{\"email\":\"pravin.test99@example.com\",\"password\":\"password\"}"; // assuming password is password or similar
        // actually I don't know the password.
    }
}
