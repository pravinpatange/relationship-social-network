package io.relationship.user;


import io.relationship.common.exception.ResourceNotFoundException;
import io.relationship.common.responce.PagedResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }


    @Transactional
    public UserEntity updateProfile(Long userId, UserDto.UpdateProfileRequest request) {
        UserEntity user = getById(userId);
        if (request.getDisplayName()       != null) user.setDisplayName(request.getDisplayName());
        if (request.getBio()               != null) user.setBio(request.getBio());
        if (request.getLocation()          != null) user.setLocation(request.getLocation());
        if (request.getWebsite()           != null) user.setWebsite(request.getWebsite());
        if (request.getProfilePictureUrl() != null) user.setProfilePictureUrl(request.getProfilePictureUrl());
        if (request.getAccountType()       != null) user.setAccountType(request.getAccountType());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public PagedResponse<UserDto.UserResponse> searchUsers(String query, Long requesterId, int page, int size) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        Pageable pageable = PageRequest.of(page, Math.min(size, 20));
        Page<UserEntity> result = userRepository.searchUsers(query.trim(), requesterId, pageable);
        Page<UserDto.UserResponse> mapped = result.map(this::toResponse);
        return PagedResponse.of(mapped);
    }
    public UserDto.UserResponse toResponse(UserEntity user) {
        return UserDto.UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .location(user.getLocation())
                .website(user.getWebsite())
                .accountType(user.getAccountType())
                .createdAt(user.getCreatedAt())
                .build();
    }





}
