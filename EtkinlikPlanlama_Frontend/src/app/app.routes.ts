import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { MainLayout } from './layout/main-layout/main-layout';
import { EventList } from './event-list/event-list';
import { EventDetail } from './event-detail/event-detail';
import { EventCreate } from './event-create/event-create';
import { EventEdit } from './event-edit/event-edit';
import { MyEvents } from './my-events/my-events';
import { authGuard } from './auth-guard';
import { notAuthGuard } from './not-auth-guard';

export const routes: Routes = [
  { path: '', component: Login, canActivate: [notAuthGuard] },
  { path: 'register', component: Register, canActivate: [notAuthGuard] },
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      { path: 'events', component: EventList },
      { path: 'events/create', component: EventCreate },
      { path: 'events/edit/:id', component: EventEdit },
      { path: 'events/:id', component: EventDetail },
      { path: 'my-events', component: MyEvents }
    ]
  },
  { path: '**', redirectTo: '' }
];
