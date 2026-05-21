import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private http = inject(HttpClient);
  private router = inject(Router);
  registerForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.registerForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      surname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      enabled: [true],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const registerData = this.registerForm.value;
      this.http.post('http://localhost:8090/user/register', registerData).subscribe({
        next: () => {
          alert('Kayıt başarıyla oluşturuldu! Giriş yapabilirsiniz.');
          this.router.navigate(['/']);
        },
        error: (error) => {
          let errorMessage = 'Lütfen bilgilerinizi kontrol edin.';
          if (Array.isArray(error.error)) {
            errorMessage = error.error.map((err: any) => `${err.field}: ${err.message}`).join('\n');
          } else if (error.error && error.error.message) {
            errorMessage = error.error.message;
          }
          alert('Kayıt başarısız:\n' + errorMessage);
        }
      });
    }
  }
}
