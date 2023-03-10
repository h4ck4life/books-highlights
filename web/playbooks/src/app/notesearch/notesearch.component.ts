import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BookService } from '../book.service';
import { Search } from '../Search';
import { faCircleNotch } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-notesearch',
  templateUrl: './notesearch.component.html',
  styleUrls: ['./notesearch.component.css']
})
export class NotesearchComponent {

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute,
  ) { }

  hasLoaded = false;
  hasData = false;

  faCircleNotch = faCircleNotch;

  query: string = '';
  searchResults: Search = {
    data: [],
    success: false
  };

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.query = params['query'];
      this.searchNotes(this.query);
    });
  }

  private searchNotes(query: string): void {
    this.bookService.searchNotes(query).subscribe(data => {
      this.searchResults = data;
      this.hasLoaded = true;
      this.hasData = this.searchResults.data!.length < 1;
    });
  }

}
