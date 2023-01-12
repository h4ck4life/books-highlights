import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BookService } from '../book.service';
import { Notes } from '../Notes';

@Component({
  selector: 'app-notelist',
  templateUrl: './notelist.component.html',
  styleUrls: ['./notelist.component.css']
})
export class NotelistComponent {

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute) { }

  notes: Notes = {
    data: [],
    success: false
  };

  hasLoaded = false;
  bookId: string = '1';

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.bookId = params['bookId'];
      this.getNotesByBookId(this.bookId);
    });

  }

  getNotesByBookId(bookId: string): void {
    this.bookService.getBook(bookId).subscribe(data => {
      this.notes = data;
      this.hasLoaded = true;
    });
  }

}
