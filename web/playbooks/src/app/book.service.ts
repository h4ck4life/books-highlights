import { Injectable, isDevMode } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { Notes } from './Notes';
import { Books } from './Books';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  constructor(private http: HttpClient) { }

  getBooks(): Observable<Books> {
    return this.http.get<Books>(this.getHostname() + '/api/books');
  }

  getBook(id: string): Observable<Notes> {
    return this.http.get<Notes>(this.getHostname() + '/api/books/' + id);
  }

  syncBooks(): Observable<Sync> {
    return this.http.get<Sync>(this.getHostname() + '/api/books/sync');
  }

  syncBook(id: string): Observable<Sync> {
    return this.http.get<Sync>(this.getHostname() + '/api/books/' + id + '/sync');
  }

  private getHostname(): string {
    return isDevMode() ? 'http://localhost:8080' : '';
  }

}

export interface Sync {
  isSucceed?: boolean;
}
