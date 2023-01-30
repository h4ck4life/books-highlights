import { Injectable, isDevMode } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { Notes } from './Notes';
import { Books } from './Books';
import { Search } from './Search';
import { GoogleBookInfo } from './GoogleBookInfo';

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

  syncBooks(pin: string): Observable<Sync> {
    const body = { 'pin': pin };
    return this.http.post<Sync>(this.getHostname() + '/api/books/sync', body);
  }

  syncBook(id: string, pin: string): Observable<Sync> {
    const body = { 'pin': pin };
    return this.http.post<Sync>(this.getHostname() + '/api/books/' + id + '/sync', body);
  }

  searchNotes(query: string): Observable<Search> {
    return this.http.get<Search>(this.getHostname() + '/api/notes/search/' + query);
  }

  getBookCover(booktitle: string): Observable<GoogleBookInfo> {
    return this.http.get('https://www.googleapis.com/books/v1/volumes?q=' + booktitle + '&orderBy=relevance&printType=BOOKS');
  }

  private getHostname(): string {
    return isDevMode() ? 'http://localhost:8080' : '';
  }

}

export interface Sync {
  success?: boolean;
  redirect?: boolean;
  redirectUrl?: string;
}
