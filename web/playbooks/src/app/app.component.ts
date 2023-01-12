import { Component } from '@angular/core';
import { faBookBookmark } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Playbooks';
  faBookBookmark = faBookBookmark;
}
