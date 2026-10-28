
import { Component, ChangeDetectorRef } from '@angular/core';
import { Router,RouterLink } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Auth, LoginRequest } from '../../services/auth';
import { JwtTokenStorage } from '../../services/jwt-token-storage';


@Component({
  selector: 'app-login',
  imports: [RouterLink,ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  constructor(private auth: Auth,
    private cdr: ChangeDetectorRef,
    private jwtTokenStorage: JwtTokenStorage,
   private router: Router ){}
  successMessage = ''
  errorMessage = ''
  loginForm = new FormGroup({
    userNameOrEmail: new FormControl('',{
      validators: Validators.required,
      nonNullable: true}),
    password: new FormControl('',{
      validators: Validators.required,
      nonNullable:true})
  })
  onSubmit(){
    if(! this.loginForm.valid){
      return;
    }
    this.successMessage = ''
    this.errorMessage = ''
    const loginRequest: LoginRequest = {
      userNameOrEmail: this.loginForm.controls.userNameOrEmail.value,
      password: this.loginForm.controls.password.value
    }
    this.auth.login(loginRequest).subscribe({
      next: (response) => {
        console.log('1. Login successful', response);
        this.jwtTokenStorage.saveToken(response.accessToken);
        this.successMessage = `User login successful for ${response.userName}`;
        this.errorMessage = '';
        this.cdr.detectChanges();
        this.router.navigate(['/dashboard'])
      },
      error: (error) =>{
        console.log('Login Failed')
        this.errorMessage = error.error?.message??'User Login Failed.'
        this.successMessage = ''
        this.cdr.detectChanges()
      }
    })
  }
  
}
