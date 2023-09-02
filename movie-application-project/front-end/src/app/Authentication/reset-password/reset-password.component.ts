import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {

  sessionId:string="";
  password:FormControl=new FormControl('', [Validators.required,
    Validators.minLength(6),
    Validators.maxLength(40)]);
  constructor(private authService:AuthServiceService ,private activatedRouter:ActivatedRoute){
    this.activatedRouter.queryParams.subscribe(data=>
      this.sessionId = data?.['session']
      // console.log(data)
      );

  }

  setPassword(){
  this.authService.resetPassword(this.sessionId,{'password':this.password.getRawValue()}).subscribe(data=>
    Swal.fire({
      icon: 'success',
      title: 'Password Changed Successfully',
      showConfirmButton: false,
      timer: 2500
    }) ); }
}
