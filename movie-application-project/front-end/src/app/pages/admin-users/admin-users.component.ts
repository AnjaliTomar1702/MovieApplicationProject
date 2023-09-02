import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AdminService, DatabaseUser, Role } from '../../service/admin.service';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { DomSanitizer } from '@angular/platform-browser';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { MatOption } from '@angular/material/core';
import { MatSelect } from '@angular/material/select';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class AdminUsersComponent implements OnInit, AfterViewInit {

  user: FormControl = new FormControl('');

  allRoles: Role[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource: MatTableDataSource<DatabaseUser> = new MatTableDataSource(new Array<DatabaseUser>());

  expandedElement!: DatabaseUser;

  changedDatabaseUser: any;

  displayedColumns: string[] = ['id', 'email', 'enabled', 'roles'];

  columnsToDisplayWithExpand: string[] = [...this.displayedColumns, 'expand'];

  constructor(private adminService: AdminService, private http: HttpClient) { }

  ngOnInit(): void {
    this.adminService.getRoles().subscribe(res => this.allRoles = res);
    this.changedDatabaseUser = new FormGroup({
      id: new FormControl({ value: '', disabled: true, }),
      email: new FormControl({ value: '', disabled: true }),
      enabled: new FormControl(false),
      // accountExpiryDate: new FormControl({ value: '', disabled: true }),
      // accountNonLocked: new FormControl({ value: false, disabled: true }),
      // credentialsExpiryDate: new FormControl({ value: '', disabled: true }),
      roles: new FormControl(new Array<Role>())
    }
    )
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  loadRowDetails(databaseUser: DatabaseUser) {
    if (this.expandedElement) {
      this.changedDatabaseUser?.controls['enabled'].setValue(this.expandedElement.enabled);
      this.changedDatabaseUser?.controls['roles'].setValue(this.expandedElement.roles);
      //      this.changedDatabaseUser?.controls['accountNonLocked'].setValue(this.expandedElement.accountNonLocked);
      // if (databaseUser.email === 'test@test.com')
      //   this.changedDatabaseUser?.controls['roles'].disable();
      this.changedDatabaseUser?.patchValue(this.expandedElement);
    }
    this.changedDatabaseUser?.markAsPristine();
  }

  search() {
    this.adminService.getUsersByEmailContainingString(this.user.value).subscribe(res => {
      this.dataSource.data = res;
    });
  }

  submit() {
    this.adminService.updateUser(this.changedDatabaseUser?.getRawValue()).subscribe(res => this.search());
  }

  compareWith(o1: any, o2: any): boolean {
    return (o1?.name as string) === (o2?.name as string);
  }

  hijackIframe(ev: Event) {
    let iframe: HTMLIFrameElement = (ev.target as HTMLIFrameElement);
    if (iframe?.contentDocument) {
      var atags = iframe?.contentDocument?.getElementsByTagName('a');
      if (atags) {
        for (let i = 0; i < atags.length; i++) {
          atags[i].setAttribute('target', '_self');
        }
      }
      console.log(document.body.scrollHeight);
      let height = iframe?.contentWindow?.document.body.scrollHeight+'px';
      // console.log(iframe?.contentWindow?.document.body.scrollHeight);
      // console.log(iframe?.contentWindow?.document.body.offsetHeight);
      // console.log(iframe?.contentWindow?.document.documentElement.scrollHeight);
      // console.log(iframe?.contentWindow?.document.documentElement.offsetHeight);
      // console.log(height);
      if(height){
        // iframe.height= height;
      }
    }
    
  }

  refreshIframe(){
    let iframe = document.getElementById('eureka') as any;
    if(iframe){
      if(iframe?.src){
        iframe.src = 'http://34.83.1.21/eureka';
    }
  }    
  }
}

// id: number;
// email: string;
// enabled: boolean;
// accountExpiryDate: string;
// accountNonLocked: boolean;
// credentialsExpiryDate: string;
// roles: Role[];

