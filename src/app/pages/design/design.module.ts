import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from 'src/app/app-routing.module';

import { AltercolorComponent } from './altercolor/altercolor.component';
import { ColorspaletteComponent } from './colorspalette/colorspalette.component';


const importedComponents = [
  ColorspaletteComponent,
  AltercolorComponent
]

@NgModule({
  declarations: importedComponents,
  imports: [
    CommonModule,
    AppRoutingModule,

  ],
  exports: importedComponents
})
export class DesignModule { }
