import { Component, Input } from '@angular/core';


@Component({
  selector: 'app-buttons-homer-help',
  templateUrl: './buttons-home-help.component.html',
  styleUrls: ['./buttons-home-help.component.css'],
})
export class ButtonsHomeHelpComponent {

  @Input() iconName: string = ''; 
  @Input() idName: string = ''; 
  @Input() routerName: string = ''; 

}
