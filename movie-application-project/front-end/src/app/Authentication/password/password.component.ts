import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.css']
})
export class PasswordComponent {
email:FormControl=new FormControl('', [Validators.email, Validators.required]);
constructor(private authService:AuthServiceService){}

resetPassword(){
this.authService.sendPasswordResetEmail(this.email.getRawValue()).subscribe(data=> 
  Swal.fire({
    title: 'We sent a reset password email to your Email Address. Please click the reset password link to set your new password.',
    icon: 'info',
    showCloseButton: true,
    showCancelButton: true,
    focusConfirm: false,
    confirmButtonText:
      '<i class="fa fa-thumbs-up"></i> Great!',
    confirmButtonAriaLabel: 'Thumbs up, great!',
    cancelButtonText:
      '<i class="fa fa-thumbs-down"></i>',
    cancelButtonAriaLabel: 'Thumbs down'
  })
  
  );
}
}
