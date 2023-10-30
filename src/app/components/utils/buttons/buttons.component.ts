import { Component, Input } from '@angular/core';


@Component({
  selector: 'app-buttons',
  templateUrl: './buttons.component.html',
  styleUrls: ['./buttons.component.css'],
})
export class ButtonsComponent {

  @Input() iconName: string = ''; 
  @Input() idName: string = ''; 
  // @Input() routerName: string = ''; 

}
