export interface User {
  id: number; username: string; email: string; displayName: string;
  profilePictureUrl?: string; bio?: string; location?: string;
  website?: string; accountType: 'PUBLIC'|'PRIVATE'; createdAt: string;
}
export interface Group {
  id: number; ownerUserId: number; groupName: string;
  description?: string; createdAt: string;
}
export interface Friendship {
  id: number; requesterId: number; requesterUsername: string;
  receiverId: number; receiverUsername: string;
  status: 'PENDING'|'ACCEPTED'|'REJECTED'|'BLOCKED'; createdAt: string;
}
export interface Post {
  id: number; authorId: number; authorUsername: string;
  caption?: string; mediaUrl?: string;
  visibilityType: 'PUBLIC'|'ALL_CONNECTIONS'|'SELECTED_GROUPS'|'PRIVATE';
  visibleToGroupIds?: number[]; likeCount: number; commentCount: number; createdAt: string;
}
export interface Comment {
  id: number; postId: number; authorId: number;
  authorUsername: string; commentText: string; createdAt: string;
}
export interface LikeStatus { postId: number; likeCount: number; likedByMe: boolean; }
export interface ChatRoom {
  id: number; user1Id: number; user1Username: string;
  user2Id: number; user2Username: string;
  contextGroupId?: number; contextGroupName?: string; createdAt: string;
}
export interface ChatMessage {
  id: number; roomId: number; senderId: number;
  senderUsername: string; message: string; createdAt: string;
}
export interface Notification {
  id: number; recipientId: number; actorId?: number; actorUsername?: string;
  type: 'FRIEND_REQUEST'|'FRIEND_ACCEPTED'|'POST_LIKE'|'POST_COMMENT'|'FOLLOW';
  referenceId?: number; read: boolean; createdAt: string;
}
export interface ModeState { userId: number; activeMode: string; activeGroupId?: number; activeGroupName?: string; }
export interface PagedResponse<T> { content: T[]; page: number; size: number; totalElements: number; totalPages: number; last: boolean; }
export interface ApiResponse<T> { success: boolean; message?: string; data: T; }
export interface AuthResponse { accessToken: string; refreshToken: string; tokenType: string; userId: number; username: string; email: string; }