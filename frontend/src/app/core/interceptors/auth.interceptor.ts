import { HttpInterceptorFn, HttpErrorResponse } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthService } from "../services/auth.service";
import { catchError, throwError, switchMap, BehaviorSubject, filter, take } from "rxjs";

let isRefreshing = false;
let refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.token();
  
  if (token) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 || error.status === 403) {
        if (req.url.includes('/auth/login') || req.url.includes('/auth/register') || req.url.includes('/auth/refresh')) {
          return throwError(() => error);
        }

        if (!isRefreshing) {
          isRefreshing = true;
          refreshTokenSubject.next(null);
          
          const rt = localStorage.getItem("refreshToken");
          if (rt) {
            return auth.refreshToken(rt).pipe(
              switchMap((tokenResponse) => {
                isRefreshing = false;
                const newToken = tokenResponse.data.accessToken;
                refreshTokenSubject.next(newToken);
                return next(req.clone({ setHeaders: { Authorization: `Bearer ${newToken}` } }));
              }),
              catchError((err) => {
                isRefreshing = false;
                auth.logout();
                return throwError(() => err);
              })
            );
          } else {
            isRefreshing = false;
            auth.logout();
            return throwError(() => error);
          }
        } else {
          return refreshTokenSubject.pipe(
            filter(t => t !== null),
            take(1),
            switchMap(t => next(req.clone({ setHeaders: { Authorization: `Bearer ${t}` } })))
          );
        }
      }
      return throwError(() => error);
    })
  );
};