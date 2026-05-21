import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-event-detail',
  imports: [CommonModule, RouterModule],
  templateUrl: './event-detail.html',
  styleUrl: './event-detail.css',
})
export class EventDetail {
  private http = inject(HttpClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  eventItem = signal<any | null>(null);
  participants = signal<any[]>([]);
  hasJoined = signal<boolean>(false);
  loading = signal<boolean>(false);

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.loadEventDetails(id);
    });
  }

  loadEventDetails(id: number) {
    this.loading.set(true);
    this.http.get<any>(`http://localhost:8090/event/getOne/${id}`, { withCredentials: true }).subscribe({
      next: (response) => {
        this.eventItem.set(response);
        this.loadParticipants(id);
      },
      error: () => {
        this.router.navigate(['/events']);
      }
    });
  }

  loadParticipants(eventId: number) {
    this.http.get<any[]>(`http://localhost:8090/event/participants/${eventId}`, { withCredentials: true }).subscribe({
      next: (response) => {
        this.participants.set(response);
        const userId = localStorage.getItem('id');
        const joined = response.some((p: any) => p.id == userId);
        this.hasJoined.set(joined);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  toggleParticipation() {
    const event = this.eventItem();
    if (!event) return;

    const isJoining = !this.hasJoined();
    const endpoint = isJoining ? 'join' : 'leave';

    this.http.put(`http://localhost:8090/event/${endpoint}/${event.id}`, {}, { withCredentials: true }).subscribe({
      next: () => {
        this.loadParticipants(event.id);
      },
      error: (err: any) => {
        alert(err.error.message || 'İşlem gerçekleştirilemedi.');
      }
    });
  }
}
