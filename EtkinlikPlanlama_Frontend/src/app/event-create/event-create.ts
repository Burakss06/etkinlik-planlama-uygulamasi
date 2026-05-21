import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-event-create',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './event-create.html',
  styleUrl: './event-create.css',
})
export class EventCreate {
  private http = inject(HttpClient);
  private router = inject(Router);
  private formBuilder = inject(FormBuilder);

  eventForm: FormGroup;

  constructor() {
    this.eventForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(500)]],
      eventDate: ['', [Validators.required]],
      eventTime: ['', [Validators.required]],
      location: ['', [Validators.required]],
      category: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
    });
  }

  onSubmit() {
    if (this.eventForm.valid) {
      const eventData = this.eventForm.value;
      this.http.post('http://localhost:8090/event/save', eventData, { withCredentials: true }).subscribe({
        next: () => {
          alert('Etkinlik başarıyla oluşturuldu.');
          this.router.navigate(['/my-events']);
        },
        error: (error) => {
          let errorMessage = 'Lütfen bilgilerinizi kontrol edin.';
          if (Array.isArray(error.error)) {
            errorMessage = error.error.map((err: any) => `${err.field}: ${err.message}`).join('\n');
          } else if (error.error && error.error.message) {
            errorMessage = error.error.message;
          }
          alert('Etkinlik oluşturulamadı:\n' + errorMessage);
        }
      });
    }
  }
}
