import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-my-events',
  imports: [RouterModule, CommonModule],
  templateUrl: './my-events.html',
  styleUrl: './my-events.css',
})
export class MyEvents {
  private http = inject(HttpClient);
  eventArray = signal<any[]>([]);
  pages = signal<number[]>([]);
  activePage = signal<number>(0);
  loading = signal<boolean>(false);

  ngOnInit() {
    this.loadMyEvents(0);
  }

  loadMyEvents(page: number = 0) {
    this.activePage.set(page);
    this.loading.set(true);

    this.http.get<any>(`http://localhost:8090/event/myList?page=${page}`, { withCredentials: true }).subscribe({
      next: (response) => {
        this.eventArray.set(response.content);
        const pagesArray = Array.from({ length: response.totalPages }, (_, i) => i);
        this.pages.set(pagesArray);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  deleteEvent(id: number) {
    const confirmDelete = confirm('Bu etkinliği tamamen silmek istediğinize emin misiniz?');
    if (confirmDelete) {
      this.http.delete(`http://localhost:8090/event/deleteOne/${id}`, { withCredentials: true }).subscribe({
        next: () => {
          alert('Etkinlik başarıyla silindi.');
          this.loadMyEvents(this.activePage());
        },
        error: (err: any) => {
          alert(err.error.message || 'Silme işlemi gerçekleştirilemedi.');
        }
      });
    }
  }

  changeStatus(id: number, newStatus: string) {
    const confirmChange = confirm(`Etkinlik durumunu ${newStatus === 'YAYINDA' ? 'Yayınla' : newStatus === 'DURDURULDU' ? 'Yayını Durdur' : 'Arşivle'} olarak değiştirmek istediğinize emin misiniz?`);
    if (confirmChange) {
      this.http.put(`http://localhost:8090/event/changeStatus/${id}/${newStatus}`, {}, { withCredentials: true }).subscribe({
        next: () => {
          alert('Etkinlik durumu güncellendi.');
          this.loadMyEvents(this.activePage());
        },
        error: (err: any) => {
          alert(err.error.message || 'Durum değiştirilemedi.');
        }
      });
    }
  }
}
