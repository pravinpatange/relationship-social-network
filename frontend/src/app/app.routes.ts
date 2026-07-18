import { Routes } from "@angular/router";
import { authGuard, guestGuard } from "./core/guards/auth.guard";

export const routes: Routes = [
  { path: "", redirectTo: "/feed", pathMatch: "full" },
  { path: "landing", loadComponent: () => import("./pages/landing/landing.component").then(m => m.LandingComponent), canActivate: [guestGuard] },
  { path: "login", loadComponent: () => import("./pages/auth/login/login.component").then(m => m.LoginComponent), canActivate: [guestGuard] },
  { path: "register", loadComponent: () => import("./pages/auth/register/register.component").then(m => m.RegisterComponent), canActivate: [guestGuard] },
  {
    path: "",
    loadComponent: () => import("./shared/components/layout/layout.component").then(m => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: "feed", loadComponent: () => import("./pages/feed/feed.component").then(m => m.FeedComponent) },
      { path: "explore", loadComponent: () => import("./pages/explore/explore.component").then(m => m.ExploreComponent) },
      { path: "groups", loadComponent: () => import("./pages/groups/groups.component").then(m => m.GroupsComponent) },
      { path: "friends", loadComponent: () => import("./pages/friends/friends.component").then(m => m.FriendsComponent) },
      { path: "chat", loadComponent: () => import("./pages/chat/chat.component").then(m => m.ChatComponent) },
      { path: "notifications", loadComponent: () => import("./pages/notifications/notifications.component").then(m => m.NotificationsComponent) },
      { path: "profile", loadComponent: () => import("./pages/profile/profile.component").then(m => m.ProfileComponent) },
      { path: "profile/:id", loadComponent: () => import("./pages/profile/profile.component").then(m => m.ProfileComponent) },
    ]
  },
  { path: "**", redirectTo: "/feed" }
];