import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faRotate, faMoon, faSun, faArrowDownWideShort } from '@fortawesome/free-solid-svg-icons';
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

  isSyncing = false;
  isDarkMode = false;

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

  syncBooks(): void {
    let bookId = this.getBookId();
    if (bookId) {
      console.log('syncing book: ' + bookId);
      this.isSyncing = true;
      this.bookService.syncBook(bookId).subscribe(data => {
        this.isSyncing = false;
        this.router.navigate(['/page/book/' + bookId], { replaceUrl: true }).then((result) => {
          console.log(result);
        });
      });
    } else {
      console.log('syncing all books');
      this.isSyncing = true;
      this.bookService.syncBooks().subscribe(data => {
        this.isSyncing = false;
        this.router.navigate(['/'], { replaceUrl: true }).then((result) => {
          console.log(result);
        });
      });
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
}
