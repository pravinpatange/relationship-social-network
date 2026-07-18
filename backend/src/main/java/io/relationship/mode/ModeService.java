package io.relationship.mode;
import io.relationship.common.exception.*;
import io.relationship.group.*;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class ModeService {
    private final UserModeRepository modeRepo;
    private final GroupRepository groupRepo;
    private final UserService userService;

    @Transactional
    public UserModeEntity changeMode(Long userId, Long groupId) {
        UserEntity user = userService.getById(userId);
        UserModeEntity mode = modeRepo.findByUserId(userId).orElseGet(()->UserModeEntity.builder().user(user).activeMode("ALL").build());
        if (groupId==null) { mode.setActiveMode("ALL"); mode.setActiveGroup(null); }
        else {
            UserGroupEntity g = groupRepo.findById(groupId).orElseThrow(()->new ResourceNotFoundException("Group",groupId));
            if (!g.getOwner().getId().equals(userId)) throw new ForbiddenException("This group does not belong to you");
            mode.setActiveMode(g.getGroupName().toUpperCase()); mode.setActiveGroup(g);
        }
        return modeRepo.save(mode);
    }
    @Transactional(readOnly=true)
    public UserModeEntity getCurrentMode(Long userId) {
        return modeRepo.findByUserId(userId).orElseGet(()->{
            UserEntity u = userService.getById(userId);
            return UserModeEntity.builder().userId(userId).user(u).activeMode("ALL").updatedAt(java.time.LocalDateTime.now()).build();
        });
    }
    public ModeDto.ModeResponse toResponse(UserModeEntity m) {
        return ModeDto.ModeResponse.builder().userId(m.getUserId()).activeMode(m.getActiveMode())
            .activeGroupId(m.getActiveGroup()!=null?m.getActiveGroup().getId():null)
            .activeGroupName(m.getActiveGroup()!=null?m.getActiveGroup().getGroupName():null)
            .updatedAt(m.getUpdatedAt()).build();
    }
}