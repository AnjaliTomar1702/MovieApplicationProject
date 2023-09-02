import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import { MovieFavouritesService } from 'src/app/service/movie-favourites.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {
  login = new FormGroup({
    email: new FormControl(''),
    password: new FormControl('')
  })
  msg: string = "";
  constructor(private router: Router, private authService: AuthServiceService, private favService: MovieFavouritesService) {
    // this.router.routeReuseStrategy.shouldReuseRoute = function () {
    //   return false;
    // };
   }

  ngOnInit(): void {
  }
  signin() {
    this.authService.loginUser(this.login.value).subscribe((result => {
      let token: string = result.message.substring(6);
      sessionStorage.setItem('token', token);
      let start = token.indexOf('.');
      let end = token.indexOf('.', start + 1);

      let claim = JSON.parse(atob(token.substring(start + 1, end)));
      console.log(claim?.sub);
      console.log(claim?.roles);

      sessionStorage.setItem('userName', claim?.sub.substring(0, claim?.sub.indexOf('@')));
      sessionStorage.setItem('role', claim?.roles.substring(claim?.roles.indexOf('_') + 1, claim?.roles.indexOf(',')));

      this.favService.getFavouriteMovies().subscribe(data =>
        this.favService.favourites = data
      )

      this.router.navigateByUrl('/movie');
      Swal.fire({
        icon: 'success',
        title: 'Logged In Successfully!!!',
        color: '#dd3675',
        showConfirmButton: false,
        timer: 1500
      })
    }),
      error => {
        console.log("user does not exist");
        this.msg = "Bad credentials,please enter valid email and password";

      })
  }


}
