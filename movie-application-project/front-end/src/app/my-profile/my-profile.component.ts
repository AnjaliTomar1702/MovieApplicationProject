import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService } from '../service/auth-service.service';
import { FileHandle } from '../Authentication/sign-up/helpers/file-handle.model';
import { DomSanitizer } from '@angular/platform-browser';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent {


  file!: File;
  constructor(private activeRouter: ActivatedRoute, private authService: AuthServiceService, private sanitizer: DomSanitizer,
    private router: Router) {
  }
  url: string = 'http://34.83.1.21/api/v1/image';

  onFilechange(event: any) {
    if (event.target.files) {
      this.file = event.target.files[0];
      const fileHandle: FileHandle = {
        file: this.file,
        url: this.sanitizer.bypassSecurityTrustUrl(
          window.URL.createObjectURL(this.file))

      }
    }
    var reader = new FileReader();
    reader.onload = (event: any) => {
      this.url = event.target.result;
    }
    reader.readAsDataURL(this.file);
  }

  upload() {
    if (this.file) {
      this.authService.uploadfile(this.file).subscribe(resp => {
        Swal.fire({
          icon: 'success',
          title: 'Updated!!!',
          color: '#dd3675',
          showConfirmButton: false,
          timer: 1500

        })
      })
    } else {
      Swal.fire({
        title: 'Please select a file first',
        color: '#dd3675',
        showConfirmButton: false,
        timer: 1500

      })
    }
  }

}