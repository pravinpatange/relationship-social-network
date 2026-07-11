package io.relationship.notification;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/notifications") @RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notifService; private final UserService userService;
    @GetMapping public ResponseEntity<ApiResponse<List<NotificationDto.NotificationResponse>>> get(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(notifService.getMyNotifications(id(ud))));
    }
    @GetMapping("/unread-count") public ResponseEntity<ApiResponse<Map<String,Long>>> unreadCount(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(Map.of("unreadCount",notifService.getUnreadCount(id(ud)))));
    }
    @PostMapping("/mark-all-read") public ResponseEntity<ApiResponse<Void>> markAll(@AuthenticationPrincipal UserDetails ud) {
        notifService.markAllRead(id(ud)); return ResponseEntity.ok(ApiResponse.success("All read",null));
    }
    @PostMapping("/{id}/read") public ResponseEntity<ApiResponse<Void>> markOne(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id) {
        notifService.markOneRead(id,id(ud)); return ResponseEntity.ok(ApiResponse.success("Marked read",null));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}