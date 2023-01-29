import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BooklistComponent } from './booklist/booklist.component';
import { NotelistComponent } from './notelist/notelist.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NotesearchComponent } from './notesearch/notesearch.component';

@NgModule({
  declarations: [
    AppComponent,
    BooklistComponent,
    NotelistComponent,
    NotesearchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FontAwesomeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
