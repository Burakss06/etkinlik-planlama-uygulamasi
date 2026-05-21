import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private http = inject(HttpClient);
  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const loginData = this.loginForm.value;
      this.http.post('http://localhost:8090/user/login', loginData, { withCredentials: true }).subscribe({
        next: (response: any) => {
          localStorage.setItem('id', response.id);
          localStorage.setItem('name', response.name + ' ' + response.surname);
          localStorage.setItem('email', response.email);
          window.location.href = '/events';
        },
        error: (error) => {
          let errorMessage = 'Lütfen bilgilerinizi kontrol edin.';
          if (Array.isArray(error.error)) {
            errorMessage = error.error.map((err: any) => `${err.field}: ${err.message}`).join('\n');
          } else if (error.error && error.error.message) {
            errorMessage = error.error.message;
          }
          alert('Giriş başarısız:\n' + errorMessage);
        }
      });
    }
  }
}
