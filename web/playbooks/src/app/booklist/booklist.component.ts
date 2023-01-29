import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { faCircleNotch } from '@fortawesome/free-solid-svg-icons';
import { BookService } from '../book.service';
import { Books } from '../Books';

@Component({
  selector: 'app-booklist',
  templateUrl: './booklist.component.html',
  styleUrls: ['./booklist.component.css']
})
export class BooklistComponent {

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute,
  ) { }

  faCircleNotch = faCircleNotch;

  hasLoaded = false;
  hasData = false;

  sortBy = false;
  books: Books = {
    data: [],
    success: false
  };

  ngOnInit(): void {

    this.sortBooks();

    this.bookService.getBooks().subscribe(data => {
      this.books = data;
      this.sortBooks();
      this.hasLoaded = true;
      this.hasData = this.books.data!.length < 1;
    });
  }

  sortBooks(): void {
    // Get query params
    this.route.queryParams.subscribe(params => {
      this.sortBy = params['sortby'] === 'count' ? true : false;
      if (this.sortBy) {
        this.books.data!.sort((a, b) => {
          return b.noteCount! - a.noteCount!;
        });
      } else {
        this.books.data!.sort((a, b) => {
          return b.driveModifiedTime! - a.driveModifiedTime!;
        });
      }
    });
  }

}
