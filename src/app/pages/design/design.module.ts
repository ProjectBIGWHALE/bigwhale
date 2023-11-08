import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from 'src/app/app-routing.module';

import { AltercolorComponent } from './altercolor/altercolor.component';
import { ColorspaletteComponent } from './colorspalette/colorspalette.component';
import { UtilsModule } from 'src/app/components/utils/utils.module';


const importedComponents = [
  ColorspaletteComponent,
  AltercolorComponent,
]

@NgModule({
  declarations: importedComponents,
  imports: [
    CommonModule,
    AppRoutingModule,
    UtilsModule,
    ReactiveFormsModule
  ],
  exports: importedComponents
})
export class DesignModule { }
