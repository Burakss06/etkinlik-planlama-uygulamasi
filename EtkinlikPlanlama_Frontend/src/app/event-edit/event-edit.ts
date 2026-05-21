import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-event-edit',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './event-edit.html',
  styleUrl: './event-edit.css',
})
export class EventEdit {
  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private formBuilder = inject(FormBuilder);

  eventForm: FormGroup;
  eventId = signal<number | null>(null);
  loading = signal<boolean>(false);

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

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.eventId.set(Number(id));
        this.loadEvent(Number(id));
      }
    });
  }

  loadEvent(id: number) {
    this.loading.set(true);
    this.http.get<any>(`http://localhost:8090/event/getOne/${id}`, { withCredentials: true }).subscribe({
      next: (response) => {
        this.eventForm.patchValue({
          title: response.title,
          description: response.description,
          eventDate: response.eventDate,
          eventTime: response.eventTime,
          location: response.location,
          category: response.category,
        });
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.router.navigate(['/my-events']);
      }
    });
  }

  onSubmit() {
    if (this.eventForm.valid && this.eventId() !== null) {
      const eventData = {
        id: this.eventId(),
        ...this.eventForm.value
      };
      this.http.put('http://localhost:8090/event/update', eventData, { withCredentials: true }).subscribe({
        next: () => {
          alert('Etkinlik başarıyla güncellendi.');
          this.router.navigate(['/my-events']);
        },
        error: (error) => {
          let errorMessage = 'Lütfen bilgilerinizi kontrol edin.';
          if (Array.isArray(error.error)) {
            errorMessage = error.error.map((err: any) => `${err.field}: ${err.message}`).join('\n');
          } else if (error.error && error.error.message) {
            errorMessage = error.error.message;
          }
          alert('Güncelleme başarısız:\n' + errorMessage);
        }
      });
    }
  }
}
