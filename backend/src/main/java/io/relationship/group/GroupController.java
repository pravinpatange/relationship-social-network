package io.relationship.group;
import io.relationship.common.response.ApiResponse;
import io.relationship.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/groups") @RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;
    @PostMapping
    public ResponseEntity<ApiResponse<GroupDto.GroupResponse>> create(@AuthenticationPrincipal UserDetails ud, @Valid @RequestBody GroupDto.CreateGroupRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Group created", groupService.toResponse(groupService.createGroup(id(ud),req))));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupDto.GroupResponse>>> list(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(ApiResponse.success(groupService.getGroupsByOwner(id(ud)).stream().map(groupService::toResponse).toList()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupDto.GroupResponse>> get(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(groupService.toResponse(groupService.getGroupById(id,id(ud)))));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupDto.GroupResponse>> update(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id, @Valid @RequestBody GroupDto.UpdateGroupRequest req) {
        return ResponseEntity.ok(ApiResponse.success("Group updated", groupService.toResponse(groupService.updateGroup(id,id(ud),req))));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id) {
        groupService.deleteGroup(id,id(ud)); return ResponseEntity.ok(ApiResponse.success("Group deleted",null));
    }
    private Long id(UserDetails ud) { return userService.getByEmail(ud.getUsername()).getId(); }
}