import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  private http = inject(HttpClient);
  globalName = 'Misafir';

  constructor() {
    const name = localStorage.getItem('name');
    if (name) {
      this.globalName = name;
    }
  }

  logout() {
    const answer = confirm('Oturumu kapatmak istediğinize emin misiniz?');
    if (answer) {
      this.http.get('http://localhost:8090/user/logout', { withCredentials: true }).subscribe({
        next: () => {
          localStorage.clear();
          this.globalName = 'Misafir';
          window.location.href = '/';
        },
        error: () => {
          alert('Oturum kapatılamadı. Lütfen tekrar deneyin.');
        }
      });
    }
  }
}
