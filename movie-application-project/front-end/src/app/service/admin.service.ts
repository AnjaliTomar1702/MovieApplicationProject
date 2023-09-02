import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private baseUrl: string = 'http://34.83.1.21';

  constructor(private http: HttpClient) {
  }

  public adminLogin(): Observable<Message>{
    let httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + sessionStorage.getItem('token')
      })
    }
    return this.http.post<Message>(this.baseUrl + '/api/v1/admin/login', {}, httpOptions);
  }

  public getUsers(): Observable<DatabaseUser[]> {
    let httpOptions = {
      withCredentials:true
    }
    return this.http.get<DatabaseUser[]>(this.baseUrl + '/api/v1/admin/users'
    , httpOptions
    );
  }

  public getUsersByEmailContainingString(email: string): Observable<DatabaseUser[]> {
    let httpOptions = {
      params: new HttpParams().set('email', email)
      , withCredentials:true
    }
    return this.http.get<DatabaseUser[]>(this.baseUrl + '/api/v1/admin/users'
    , httpOptions
    );
  }

  public blockUser(email: string): Observable<Message> {
    let httpOptions = {
      withCredentials:true
    }
    return this.http.patch<Message>(this.baseUrl + '/api/v1/admin/users/block'
      , {
        email: email
      }
      , httpOptions
      );
  }

  public unblockUser(email: string): Observable<Message> {
    let httpOptions = {
     withCredentials:true
    }
    return this.http.patch<Message>(this.baseUrl + '/api/v1/admin/users/unblock'
      , {
        email: email
      }
      , httpOptions
      );
  }

  public addRole(email: string, role: string): Observable<Message> {
    let httpOptions = {
      withCredentials:true
    }
    return this.http.patch<Message>(this.baseUrl + '/api/v1/admin/users/addrole'
      , {
        email: email
        , role: role
      }
      , httpOptions
      );
  }

  public removeRole(email: string, role: string): Observable<Message> {
    let httpOptions = {
      withCredentials:true
    }
    return this.http.patch<Message>(this.baseUrl + '/api/v1/admin/users/removerole'
      , {
        email: email
        , role: role
      }
      , httpOptions
      );
  }

  public getRoles(): Observable<Role[]> {
    let httpOptions = {
      withCredentials:true
    }
    return this.http.get<Role[]>(this.baseUrl + '/api/v1/admin/roles'
    , httpOptions
    );
  }

  public updateUser(databaseUser:DatabaseUser): Observable<DatabaseUser> {
    let httpOptions = {
      withCredentials:true
    }
    return this.http.patch<DatabaseUser>(this.baseUrl + '/api/v1/admin/users', databaseUser
    , httpOptions
    );
  }

}


export type DatabaseUser = {
  id: number;
  email: string;
  enabled: boolean;
  // accountExpiryDate: string;
  // accountNonLocked: boolean;
  // credentialsExpiryDate: string;
  roles: Role[];
}

export type Message = {
  message: string;
}

export type Role = {
  id: number;
  name: string;
}