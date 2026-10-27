
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, ValidatorFn } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Auth, SignupRequest } from '../../services/auth';

const passwordMatchValidator: ValidatorFn = (control) => {
  const passwd = control.get('password')?.value;
  const confirmPasswd = control.get('confirmPassword')?.value;
  if((passwd != '' && confirmPasswd != '') &&(passwd != null && confirmPasswd != null)){
    if(passwd != confirmPasswd){
      return {passwordMismatch: true}
    }else{
      return null
    }
  }   
  return null
}



@Component({
  selector: 'app-register',
  imports: [RouterLink,ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  constructor(private auth:Auth,
    private cdr: ChangeDetectorRef
  ){}
  successMessage = ''
  errorMessage = ''
  registerForm = new FormGroup({
    username: new FormControl('',{
      validators: Validators.required,
      nonNullable: true}),
    email: new FormControl('',{
      validators:[Validators.required,Validators.email],
      nonNullable: true}),
    password: new FormControl('',{
      validators: [Validators.required,
      Validators.minLength(8),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d]).+$/)],
      nonNullable: true}),
    confirmPassword: new FormControl('',Validators.required)
  },
  {validators: passwordMatchValidator})
  onSubmit(){
    if(! this.registerForm.valid){
      return;
    }
    this.successMessage = ''
    this.errorMessage = ''
    const signupRequest: SignupRequest = {
      userName: this.registerForm.controls.username.value,
      email: this.registerForm.controls.email.value,
      password: this.registerForm.controls.password.value
    };
    this.auth.registerUser(signupRequest).subscribe({
      next: (response) =>{
        console.log('Success CallBack executed', response)
        this.successMessage = `Registration successfull for ${response.userName}`;
        this.errorMessage = ''
        this.cdr.detectChanges();
      },
      error:(error) =>{
        console.log('Error callBack executed', error)
        this.errorMessage = error.error?.message??'User Registration Failed.'
        this.successMessage = ''
        this.cdr.detectChanges();
      }
    })  
  }
}
