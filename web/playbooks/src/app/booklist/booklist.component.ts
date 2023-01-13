import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

  hasLoaded = false;
  hasData = false;

  sortBy = false;
  books: Books = {
    data: [],
    success: false
  };

  ngOnInit(): void {

    // Get query params
    this.route.queryParams.subscribe(params => {
       this.sortBy = params['sortby'] === 'count' ? true : false;
       if(this.sortBy) {
         this.books.data!.sort((a, b) => {
           return b.noteCount! - a.noteCount!;
         });
       } else {
          this.books.data!.sort((a, b) => {
            return b.driveModifiedTime! - a.driveModifiedTime!;
          });
       }
    });

    this.bookService.getBooks().subscribe(data => {
      this.books = data;
      this.hasLoaded = true;
      this.hasData = this.books.data!.length < 1;
    });
  }

}
