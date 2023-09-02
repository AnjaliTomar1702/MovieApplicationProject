import { Pipe, PipeTransform } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Observable, map } from 'rxjs';

@Pipe({
    name: 'imageAuth'
})
export class ImageAuthPipe implements PipeTransform {

    constructor(private http: HttpClient, private sanitizer: DomSanitizer) { }

    transform(url:string): Observable<SafeUrl> {
        return this.http
            .get(url, { headers: new HttpHeaders({
                'Authorization': 'Bearer ' + sessionStorage.getItem('token')
            })
            , responseType: 'blob' }).pipe(
            map(val => this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(val)))
            );
    }

}