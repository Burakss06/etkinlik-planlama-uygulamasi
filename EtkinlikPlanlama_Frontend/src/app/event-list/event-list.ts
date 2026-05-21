import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-event-list',
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './event-list.html',
  styleUrl: './event-list.css',
})
export class EventList {
  private http = inject(HttpClient);
  eventArray = signal<any[]>([]);
  pages = signal<number[]>([]);
  activePage = signal<number>(0);
  loading = signal<boolean>(false);
  searchQuery = signal<string>('');

  ngOnInit() {
    this.loadEvents(0);
  }

  loadEvents(page: number = 0) {
    this.activePage.set(page);
    this.loading.set(true);

    const query = this.searchQuery();
    let url = `http://localhost:8090/event/list?page=${page}`;
    if (query.trim() !== '') {
      url = `http://localhost:8090/event/search?page=${page}&q=${query}`;
    }

    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (response) => {
        const published = (response.content || []).filter((e: any) => e.status === 'YAYINDA');
        this.eventArray.set(published);
        const pagesArray = Array.from({ length: response.totalPages }, (_, i) => i);
        this.pages.set(pagesArray);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  onSearch() {
    this.loadEvents(0);
  }
}
