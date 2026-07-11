import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./auth/register/register.component').then(m => m.RegisterComponent) },
  { 
    path: 'app', 
    loadComponent: () => import('./layout/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
    children: [
      { path: 'feed', loadComponent: () => import('./feed/feed/feed.component').then(m => m.FeedComponent) },
      { 
        path: 'network', 
        loadComponent: () => import('./network/network-layout/network-layout.component').then(m => m.NetworkLayoutComponent),
        children: [
          { path: 'friends', loadComponent: () => import('./network/friends/friends.component').then(m => m.FriendsComponent) },
          { path: 'groups', loadComponent: () => import('./network/groups/groups.component').then(m => m.GroupsComponent) },
          { path: '', redirectTo: 'friends', pathMatch: 'full' }
        ]
      },
      { path: 'profile', loadComponent: () => import('./profile/profile/profile.component').then(m => m.ProfileComponent) },
      { path: '', redirectTo: 'feed', pathMatch: 'full' }
    ]
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
