import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthServiceService } from '../service/auth-service.service';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class DeactivateGuardGuard implements CanActivate {
  constructor(private authService: AuthServiceService) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.authService.isLoggingIn()) {
      Swal.fire({
        position:'top-end',
        icon: 'info',
        title: 'You Have Already Logged In!!',
        color: '#ADD8E6',
        showConfirmButton: false,
        timer: 2500
      })
      return false;
    } 
    else {
      return true;
    }
  }

}
