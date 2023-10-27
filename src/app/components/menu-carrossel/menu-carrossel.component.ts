import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-menu-carrossel',
  templateUrl: './menu-carrossel.component.html',
  styleUrls: ['./menu-carrossel.component.css']
})
export class MenuCarrosselComponent {

  @Input()
  title:string =''

}
