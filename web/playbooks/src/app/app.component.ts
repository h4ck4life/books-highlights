import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { faBookBookmark, faRotate } from '@fortawesome/free-solid-svg-icons';
import { BookService } from './book.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'PlayBooks Notes';
  faBookBookmark = faBookBookmark;
  faRotate = faRotate;

  isSyncing = false;

  constructor(private bookService: BookService, private router: Router) { }

  ngOnInit(): void {
  }

  syncBooks(): void {
    this.isSyncing = true;
    this.bookService.syncBooks().subscribe(data => {
      this.isSyncing = false;
      this.router.navigate(['/'], { replaceUrl: true }).then((result) => {
        console.log(result);
      });
    });
  }
}
