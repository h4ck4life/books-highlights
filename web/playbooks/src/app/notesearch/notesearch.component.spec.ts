import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotesearchComponent } from './notesearch.component';

describe('NotesearchComponent', () => {
  let component: NotesearchComponent;
  let fixture: ComponentFixture<NotesearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotesearchComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotesearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
