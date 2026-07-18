package io.relationship.user;


import io.relationship.common.response.ApiResponse;
import io.relationship.common.response.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> getCurrentUser(
            @AuthenticationPrincipal UserDetails ud) {
        UserEntity user = userService.getByEmail(ud.getUsername());
        return ResponseEntity.ok(ApiResponse.success(userService.toResponse(user)));
    }



    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails ud,
            @Valid @RequestBody UserDto.UpdateProfileRequest request) {
        UserEntity user    = userService.getByEmail(ud.getUsername());
        UserEntity updated = userService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated", userService.toResponse(updated)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.toResponse(userService.getById(id))));
    }

    /**
     * GET /api/users/search?q=pravin
     * GET /api/users/search?q=pravin&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<UserDto.UserResponse>>> searchUsers(
            @AuthenticationPrincipal UserDetails ud,
            @RequestParam("q")                    String query,
            @RequestParam(defaultValue = "0")     int    page,
            @RequestParam(defaultValue = "10")    int    size) {
        Long requesterId = userService.getByEmail(ud.getUsername()).getId();
        PagedResponse<UserDto.UserResponse> result = userService.searchUsers(query, requesterId, page, size);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<ApiResponse<List<UserDto.UserResponse>>> getSuggestions(@AuthenticationPrincipal UserDetails ud) {
        Long requesterId = userService.getByEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(ApiResponse.success(userService.getSuggestions(requesterId)));
    }
}
