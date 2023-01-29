import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BooklistComponent } from './booklist/booklist.component';
import { NotelistComponent } from './notelist/notelist.component';
import { NotesearchComponent } from './notesearch/notesearch.component';

const routes: Routes = [
  { path: '', data: { title: 'Home' }, component: BooklistComponent },
  { path: 'page/book/:bookId', data: { title: 'Notes' }, component: NotelistComponent },
  { path: 'page/note/search/:query', data: { title: 'Search' }, component: NotesearchComponent},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
