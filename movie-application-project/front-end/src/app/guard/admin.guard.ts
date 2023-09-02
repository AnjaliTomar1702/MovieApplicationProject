import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AdminService } from '../service/admin.service';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private adminService:AdminService){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return new Observable((observor) => this.adminService.adminLogin().subscribe(res => {
      observor.next(true);
      observor.complete();
    }
    , err => {
      Swal.fire({
        icon: 'error',
        title: 'Access Restricted!!',
        color: '#FA8072',
        showConfirmButton: false,
        timer: 2500
      })
      observor.next(false);
      observor.complete();
    }));
  }
  
}
