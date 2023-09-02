import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { Router } from '@angular/router';
import { Message } from './admin.service';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
  private usersUrl1: string;
  private usersUrl2: string;
  private usersUrl3: string;
  private userUrl4:string;
  private userUrl5:string;


  constructor(private http: HttpClient, private router: Router) {
    this.usersUrl2 = 'http://34.83.1.21/api/v1/login';
    this.usersUrl1 = 'http://34.83.1.21/api/v1/register';
    this.usersUrl3 = 'http://34.83.1.21/api/v1/image';
    this.userUrl4 = 'http://34.83.1.21/api/v1/forgotpassword';
    this.userUrl5 = 'http://34.83.1.21/api/v1/resetpassword';


  }

  public registerUser(user: FormData): Observable<any> {
    return this.http.post<User>(this.usersUrl1, user);
  }
 
  public loginUser(user: any): Observable<any> {
    return this.http.post<User>(this.usersUrl2, user);
  }
  
  isLoggingIn(){
   return  !!sessionStorage.getItem('token');
  }
 
sendPasswordResetEmail(email:string):Observable<Message>{
  console.log(email);
  let param = new HttpParams();
  param.set('email',email);
 return this.http.post<Message>(this.userUrl4 + '?email=' + email,null);
}

resetPassword(sessionId:string,password:any):Observable<Message>{
 
 return this.http.post<Message>(this.userUrl5 + '?session=' +sessionId,password);
}


  public uploadfile(file: File) {
    let httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + sessionStorage.getItem('token')
      })
    }
    let formParams = new FormData();
    formParams.append('file', file)
    return this.http.post(this.usersUrl3, formParams, httpOptions);
  }


  loggingOut() {
    sessionStorage.clear();
    this.router.navigateByUrl("");
  }

}
