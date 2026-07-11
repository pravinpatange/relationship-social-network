package io.relationship.group;
import io.relationship.common.exception.*;
import io.relationship.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepo;
    private final UserService userService;

    @Transactional
    public UserGroupEntity createGroup(Long userId, GroupDto.CreateGroupRequest req) {
        UserEntity owner = userService.getById(userId);
        if (groupRepo.existsByOwnerIdAndGroupNameIgnoreCase(userId, req.getGroupName()))
            throw new DuplicateResourceException("You already have a group named '" + req.getGroupName() + "'");
        return groupRepo.save(UserGroupEntity.builder().owner(owner).groupName(req.getGroupName()).description(req.getDescription()).build());
    }
    @Transactional(readOnly=true)
    public List<UserGroupEntity> getGroupsByOwner(Long userId) { return groupRepo.findAllByOwnerId(userId); }
    @Transactional(readOnly=true)
    public UserGroupEntity getGroupById(Long id, Long userId) {
        return groupRepo.findByIdAndOwnerId(id,userId).orElseThrow(()->new ResourceNotFoundException("Group",id));
    }
    @Transactional
    public UserGroupEntity updateGroup(Long id, Long userId, GroupDto.UpdateGroupRequest req) {
        UserGroupEntity g = getGroupById(id, userId);
        if (req.getGroupName() != null) {
            boolean changed = !g.getGroupName().equalsIgnoreCase(req.getGroupName());
            if (changed && groupRepo.existsByOwnerIdAndGroupNameIgnoreCase(userId, req.getGroupName()))
                throw new DuplicateResourceException("You already have a group named '" + req.getGroupName() + "'");
            g.setGroupName(req.getGroupName());
        }
        if (req.getDescription() != null) g.setDescription(req.getDescription());
        return groupRepo.save(g);
    }
    @Transactional
    public void deleteGroup(Long id, Long userId) {
        UserGroupEntity g = groupRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Group",id));
        if (!g.getOwner().getId().equals(userId)) throw new ForbiddenException("You are not the owner of this group");
        groupRepo.delete(g);
    }
    public GroupDto.GroupResponse toResponse(UserGroupEntity g) {
        return GroupDto.GroupResponse.builder().id(g.getId()).ownerUserId(g.getOwner().getId())
            .groupName(g.getGroupName()).description(g.getDescription()).createdAt(g.getCreatedAt()).build();
    }
}