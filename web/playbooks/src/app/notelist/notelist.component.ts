import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Location } from '@angular/common';
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
    private titleService: Title,
    private location: Location
  ) { }

  notes: Notes = {
    data: [],
    success: false
  };

  hasLoaded = false;
  hasData = false;
  bookId: string = '1';

  ngOnInit(): void {
    this.route.fragment.subscribe(fragment => {
      if (fragment) {
        setTimeout(() => {
          const element = document.getElementById(fragment);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
        }, 1000);
      }
    });

    this.route.params.subscribe(params => {
      this.bookId = params['bookId'];
      this.getNotesByBookId(this.bookId);
    });
  }

  getFragmentUrl(noteId: number): string {
    return this.location.path() + '#' + noteId;
  }

  private getNotesByBookId(bookId: string): void {
    this.bookService.getBook(bookId).subscribe(data => {
      this.notes = data;
      this.hasLoaded = true;
      this.hasData = this.notes.data!.length < 1;
      this.titleService.setTitle(data.data![0].book!.bookTitle!);
    });
  }

}
