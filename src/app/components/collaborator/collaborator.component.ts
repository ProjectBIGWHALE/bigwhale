import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-collaborator',
  templateUrl: './collaborator.component.html',
  styleUrls: ['./collaborator.component.css']
})
export class CollaboratorComponent {

 
  
  @Input()
  name: string =''
  @Input()
  photo:string=''
  @Input()
  linkLinkedin:string=''
  @Input()
  altText:string =''



 

}
