import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  faRotate,
  faMoon,
  faSun,
  faArrowDownWideShort,
  faBars,
  faSearch,
} from '@fortawesome/free-solid-svg-icons';
import { faGooglePlay } from '@fortawesome/free-brands-svg-icons';
import { BookService } from './book.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'PlayBooks Notes';
  faRotate = faRotate;
  faGooglePlay = faGooglePlay;
  faMoon = faMoon;
  faSun = faSun;
  faArrowDownWideShort = faArrowDownWideShort;
  faBars = faBars;
  faSearch = faSearch;

  isSyncing = false;
  isDarkMode = false;
  isMenuOpen = true;

  constructor(
    private bookService: BookService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    // save localstorage dark mode
    this.isDarkMode = localStorage.getItem('darkMode') === 'true';
    if (this.isDarkMode) {
      document.body.parentElement!.classList.add('dark');
    }
  }

  private getBookId(): string {
    try {
      return this.router.parseUrl(this.router.url).root.children['primary'].segments[2].path;
    } catch (error) {
      return '';
    }
  }

  private getPin(): string {
    let pin = prompt('Sync PIN:');
    return pin != null ? pin : '';
  }

  syncBooks(): void {
    const pin = this.getPin();
    if (pin != '') {
      let bookId = this.getBookId();
      if (bookId) {
        console.log('syncing book: ' + bookId);
        this.isSyncing = true;
        this.bookService.syncBook(bookId, pin).subscribe(data => {
          this.isSyncing = false;
          if (data.success == false && data.redirect == true) {
            location.assign(data.redirectUrl!);
          } else {
            location.reload();
            //this.router.navigate(['/page/book/' + bookId], { replaceUrl: true });
          }
        }, error => {
          this.isSyncing = false;
          alert(error.message);
        });

      } else {
        console.log('syncing all books');
        this.isSyncing = true;
        this.bookService.syncBooks(pin).subscribe(data => {
          this.isSyncing = false;
          if (data.success == false && data.redirect == true) {
            location.assign(data.redirectUrl!);
          } else {
            location.assign('/');
          }
        }, error => {
          this.isSyncing = false;
          alert(error.message);
        });
      }
    }
  }

  toggleDarkMode(): void {
    this.isDarkMode = !this.isDarkMode;
    localStorage.setItem('darkMode', this.isDarkMode.toString());
    if (this.isDarkMode) {
      document.body.parentElement!.classList.add('dark');
    } else {
      document.body.parentElement!.classList.remove('dark');
    }
  }

  toggleSort(): void {
    if (this.router.url === '/') {
      this.router.navigate(['/'], { replaceUrl: true, queryParams: { sortby: 'count' } });
    } else {
      this.router.navigate(['/'], { replaceUrl: true });
    }
  }

  toggleMenu(): void {
    document.getElementById('dropdownMenu')!.classList.toggle('hidden');

  }

  search(): void {
    let query = prompt('Search:');
    if (query != null) {
      this.router.navigate(['/page/note/search/' + query]);
    }
  }
}
