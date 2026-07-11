package io.relationship.mode;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/mode") @RequiredArgsConstructor
public class ModeController {
    private final ModeService modeService; private final UserService userService;
    @PostMapping("/change") public ResponseEntity<ApiResponse<ModeDto.ModeResponse>> change(
            @AuthenticationPrincipal UserDetails ud, @RequestBody ModeDto.ChangeModeRequest req) {
        return ResponseEntity.ok(ApiResponse.success("Mode changed",modeService.toResponse(modeService.changeMode(id(ud),req.getGroupId()))));
    }
    @GetMapping("/current") public ResponseEntity<ApiResponse<ModeDto.ModeResponse>> current(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(modeService.toResponse(modeService.getCurrentMode(id(ud)))));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}