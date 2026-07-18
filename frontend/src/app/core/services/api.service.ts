import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { ApiResponse, PagedResponse, Post, Group, Friendship, Comment, LikeStatus, ChatRoom, ChatMessage, Notification, ModeState, User } from "../models";

@Injectable({ providedIn: "root" })
export class ApiService {
  private readonly API = "http://localhost:8080/api";

  constructor(private http: HttpClient) {}

  // ── Users ──
  getMe(): Observable<User> { return this.http.get<ApiResponse<User>>(`${this.API}/users/me`).pipe(map(r=>r.data)); }
  getUserById(id: number): Observable<User> { return this.http.get<ApiResponse<User>>(`${this.API}/users/${id}`).pipe(map(r=>r.data)); }
  updateProfile(data: Partial<User>): Observable<User> { return this.http.put<ApiResponse<User>>(`${this.API}/users/me`, data).pipe(map(r=>r.data)); }
  searchUsers(q: string, page=0, size=10): Observable<PagedResponse<User>> { return this.http.get<ApiResponse<PagedResponse<User>>>(`${this.API}/users/search`, {params:{q,page,size}}).pipe(map(r=>r.data)); }
  getSuggestions(): Observable<User[]> { return this.http.get<ApiResponse<User[]>>(`${this.API}/users/suggestions`).pipe(map(r=>r.data)); }

  // ── Groups ──
  getGroups(): Observable<Group[]> { return this.http.get<ApiResponse<Group[]>>(`${this.API}/groups`).pipe(map(r=>r.data)); }
  createGroup(data: {groupName:string;description?:string}): Observable<Group> { return this.http.post<ApiResponse<Group>>(`${this.API}/groups`, data).pipe(map(r=>r.data)); }
  updateGroup(id: number, data: Partial<Group>): Observable<Group> { return this.http.put<ApiResponse<Group>>(`${this.API}/groups/${id}`, data).pipe(map(r=>r.data)); }
  deleteGroup(id: number): Observable<void> { return this.http.delete<any>(`${this.API}/groups/${id}`); }

  // ── Friends ──
  getFriends(): Observable<Friendship[]> { return this.http.get<ApiResponse<Friendship[]>>(`${this.API}/friends`).pipe(map(r=>r.data)); }
  getPendingRequests(): Observable<Friendship[]> { return this.http.get<ApiResponse<Friendship[]>>(`${this.API}/friends/requests`).pipe(map(r=>r.data)); }
  getSentRequests(): Observable<Friendship[]> { return this.http.get<ApiResponse<Friendship[]>>(`${this.API}/friends/requests/sent`).pipe(map(r=>r.data)); }
  sendFriendRequest(receiverId: number): Observable<Friendship> { return this.http.post<ApiResponse<Friendship>>(`${this.API}/friends/request`, {receiverId}).pipe(map(r=>r.data)); }
  acceptRequest(id: number): Observable<Friendship> { return this.http.post<ApiResponse<Friendship>>(`${this.API}/friends/${id}/accept`, {}).pipe(map(r=>r.data)); }
  rejectRequest(id: number): Observable<Friendship> { return this.http.post<ApiResponse<Friendship>>(`${this.API}/friends/${id}/reject`, {}).pipe(map(r=>r.data)); }
  unfriend(friendId: number): Observable<void> { return this.http.delete<any>(`${this.API}/friends/${friendId}`); }
  blockUser(userId: number): Observable<Friendship> { return this.http.post<ApiResponse<Friendship>>(`${this.API}/friends/${userId}/block`, {}).pipe(map(r=>r.data)); }
  assignGroups(friendId: number, groupIds: number[]): Observable<void> { return this.http.post<any>(`${this.API}/friends/${friendId}/groups`, {groupIds}); }
  getFriendGroups(friendId: number): Observable<{groupId:number;groupName:string}[]> { return this.http.get<ApiResponse<{groupId:number;groupName:string}[]>>(`${this.API}/friends/${friendId}/groups`).pipe(map(r=>r.data)); }

  // ── Followers ──
  follow(userId: number): Observable<void> { return this.http.post<any>(`${this.API}/follow/${userId}`, {}); }
  unfollow(userId: number): Observable<void> { return this.http.delete<any>(`${this.API}/follow/${userId}`); }
  getFollowers(): Observable<any[]> { return this.http.get<ApiResponse<any[]>>(`${this.API}/follow/followers`).pipe(map(r=>r.data)); }
  getFollowing(): Observable<any[]> { return this.http.get<ApiResponse<any[]>>(`${this.API}/follow/following`).pipe(map(r=>r.data)); }

  // ── Posts ──
  createPost(data: {caption?:string;mediaUrl?:string;visibilityType:string;visibleToGroupIds?:number[]}): Observable<Post> { return this.http.post<ApiResponse<Post>>(`${this.API}/posts`, data).pipe(map(r=>r.data)); }
  getPostById(id: number): Observable<Post> { return this.http.get<ApiResponse<Post>>(`${this.API}/posts/${id}`).pipe(map(r=>r.data)); }
  getMyPosts(): Observable<Post[]> { return this.http.get<ApiResponse<Post[]>>(`${this.API}/posts/me`).pipe(map(r=>r.data)); }
  deletePost(id: number): Observable<void> { return this.http.delete<any>(`${this.API}/posts/${id}`); }

  // ── Feed ──
  getFeed(groupId?: number, page=0, size=20): Observable<PagedResponse<Post>> {
    let params: any = {page, size};
    if (groupId) params['groupId'] = groupId;
    return this.http.get<ApiResponse<PagedResponse<Post>>>(`${this.API}/feed`, {params}).pipe(map(r=>r.data));
  }

  // ── Comments ──
  getComments(postId: number): Observable<Comment[]> { return this.http.get<ApiResponse<Comment[]>>(`${this.API}/posts/${postId}/comments`).pipe(map(r=>r.data)); }
  addComment(postId: number, commentText: string): Observable<Comment> { return this.http.post<ApiResponse<Comment>>(`${this.API}/posts/${postId}/comments`, {commentText}).pipe(map(r=>r.data)); }
  deleteComment(postId: number, commentId: number): Observable<void> { return this.http.delete<any>(`${this.API}/posts/${postId}/comments/${commentId}`); }

  // ── Likes ──
  likePost(postId: number): Observable<LikeStatus> { return this.http.post<ApiResponse<LikeStatus>>(`${this.API}/posts/${postId}/likes`, {}).pipe(map(r=>r.data)); }
  unlikePost(postId: number): Observable<LikeStatus> { return this.http.delete<ApiResponse<LikeStatus>>(`${this.API}/posts/${postId}/likes`).pipe(map(r=>r.data)); }
  getLikeStatus(postId: number): Observable<LikeStatus> { return this.http.get<ApiResponse<LikeStatus>>(`${this.API}/posts/${postId}/likes`).pipe(map(r=>r.data)); }

  // ── Mode ──
  getCurrentMode(): Observable<ModeState> { return this.http.get<ApiResponse<ModeState>>(`${this.API}/mode/current`).pipe(map(r=>r.data)); }
  changeMode(groupId: number|null): Observable<ModeState> { return this.http.post<ApiResponse<ModeState>>(`${this.API}/mode/change`, {groupId}).pipe(map(r=>r.data)); }

  // ── Chat ──
  startChat(otherUserId: number, contextGroupId?: number): Observable<ChatRoom> { return this.http.post<ApiResponse<ChatRoom>>(`${this.API}/chats/rooms`, {otherUserId, contextGroupId}).pipe(map(r=>r.data)); }
  getMyRooms(): Observable<ChatRoom[]> { return this.http.get<ApiResponse<ChatRoom[]>>(`${this.API}/chats/rooms`).pipe(map(r=>r.data)); }
  getMessages(roomId: number): Observable<ChatMessage[]> { return this.http.get<ApiResponse<ChatMessage[]>>(`${this.API}/chats/rooms/${roomId}/messages`).pipe(map(r=>r.data)); }
  sendMessage(roomId: number, message: string): Observable<ChatMessage> { return this.http.post<ApiResponse<ChatMessage>>(`${this.API}/chats/rooms/${roomId}/messages`, {message}).pipe(map(r=>r.data)); }

  // ── Notifications ──
  getNotifications(): Observable<Notification[]> { return this.http.get<ApiResponse<Notification[]>>(`${this.API}/notifications`).pipe(map(r=>r.data)); }
  getUnreadCount(): Observable<{unreadCount:number}> { return this.http.get<ApiResponse<{unreadCount:number}>>(`${this.API}/notifications/unread-count`).pipe(map(r=>r.data)); }
  markAllRead(): Observable<void> { return this.http.post<any>(`${this.API}/notifications/mark-all-read`, {}); }
  markOneRead(id: number): Observable<void> { return this.http.post<any>(`${this.API}/notifications/${id}/read`, {}); }
}