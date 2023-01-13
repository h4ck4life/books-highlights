import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Title } from '@angular/platform-browser';
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
    private route: ActivatedRoute,
    private titleService: Title
    ) { }

  notes: Notes = {
    data: [],
    success: false
  };

  hasLoaded = false;
  hasData = false;
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
      this.hasData = this.notes.data!.length < 1;
      this.titleService.setTitle(data.data![0].book!.bookTitle!);
    });
  }

}
