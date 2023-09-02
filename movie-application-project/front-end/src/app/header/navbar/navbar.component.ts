import { Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuTrigger } from '@angular/material/menu';
import { Router } from '@angular/router';
import { MyProfileComponent } from 'src/app/my-profile/my-profile.component';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})

export class NavbarComponent{
  @Output() sideNavToggled = new EventEmitter<boolean>();
  menuStatus:boolean = true;

  constructor(private auth:AuthServiceService,public dialog: MatDialog,private router:Router){
    this.window = window;

  }
sessionStorage = sessionStorage;

  @Input()
  userDisplayName:any='';
  
  @Input()
  userRole:any;
  ngOnInit() {
     this.userDisplayName = sessionStorage.getItem('userName');
     this.userRole = sessionStorage.getItem('role');
  }



  window:Window;

  logout() {
    this.auth.loggingOut();
    Swal.fire({
      icon: 'success',
      title: 'Logged Out Successfully!!!',
      color: '#dd3675',
      showConfirmButton: false,
      timer: 1500
    })
}

openDialog() {
  const dialogRef = this.dialog.open(MyProfileComponent);

  dialogRef.afterClosed().subscribe(result => {
  });
}
}



// navbg:any;
//   @HostListener('document:scroll') scrollover(){
// if(document.body.scrollTop > 0 || document.documentElement.scrollTop > 0){
//   this.navbg = {
//     'background-color': '#000000'
         
//   }
// }else{
//   this.navbg = {
//     'background-color': '#000000'

//   }
// }
//   }
//   @ViewChild(MatMenuTrigger) trigger!: MatMenuTrigger;

//   someMethod() {
//     this.trigger.openMenu();
//   }
// }

