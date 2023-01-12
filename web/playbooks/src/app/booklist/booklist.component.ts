import { Component } from '@angular/core';
import { BookService } from '../book.service';
import { Books } from '../Books';

@Component({
  selector: 'app-booklist',
  templateUrl: './booklist.component.html',
  styleUrls: ['./booklist.component.css']
})
export class BooklistComponent {

  constructor(private bookService: BookService) { }

  books: Books = {
    data: [],
    success: false
  };

  ngOnInit(): void {
    this.bookService.getBooks().subscribe(data => {
      this.books = data;
      console.log(data);
    });
  }

}
