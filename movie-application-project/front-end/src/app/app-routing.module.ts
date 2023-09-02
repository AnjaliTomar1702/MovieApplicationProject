import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './pages/search/search.component';
import { MovieDetailsComponent } from './pages/movie-details/movie-details.component';
import { RecommendedComponent } from './pages/recommended/recommended.component';
import { NotificationComponent } from './pages/notification/notification.component';
import { FavouriteMoviesComponent } from './pages/favourite-movies/favourite-movies.component';
import { AdminGuard } from './guard/admin.guard';
import { AdminUsersComponent } from './pages/admin-users/admin-users.component';
import { SignUpComponent } from './Authentication/sign-up/sign-up.component';
import { SignInComponent } from './Authentication/sign-in/sign-in.component';
import { ErrorComponent } from './error/error.component';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { AuthGuardGuard } from './guard/auth-guard.guard';
import { DeactivateGuardGuard } from './guard/deactivate-guard.guard';
import { PasswordComponent } from './Authentication/password/password.component';
import { ResetPasswordComponent } from './Authentication/reset-password/reset-password.component';

const routes: Routes = [
  {path:'home',component:HomeComponent},
  {path:'search',component:SearchComponent},
  {path:'movie',component:RecommendedComponent},
  {path:'notification',component:NotificationComponent,canActivate:[AuthGuardGuard]},
  {path:'my-profile', component:MyProfileComponent,canActivate:[AuthGuardGuard]},
  {path:'admin-user',component:AdminUsersComponent,canActivate:[AdminGuard]},
  {path:'movie/:id',component:MovieDetailsComponent,canActivate:[AuthGuardGuard]},
  {path:'user-favourite' , component:FavouriteMoviesComponent,canActivate:[AuthGuardGuard]},
  {path:'signup', component:SignUpComponent,canActivate:[DeactivateGuardGuard]},
  {path:'signin', component:SignInComponent,canActivate:[DeactivateGuardGuard]},
  {path:'forgot-password',component:PasswordComponent},
  {path:'reset-password',component:ResetPasswordComponent},
  {
    path: '',
    redirectTo: 'movie',
    pathMatch: 'full',
  },

  { path: '**', pathMatch: 'full', 
  component: ErrorComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes,{useHash:true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
