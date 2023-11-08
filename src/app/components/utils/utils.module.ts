import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from 'src/app/app-routing.module';

import { ButtonDownloadComponent } from './button-download/button-download.component';
import { ButtonsHomeHelpComponent } from './buttons-home-help/buttons-home-help.component';
import { LinesComponent } from './lines/lines.component';


const importedComponents = [
  ButtonDownloadComponent,
  ButtonsHomeHelpComponent,
  LinesComponent,
]


@NgModule({
  declarations: importedComponents,
  imports: [
    CommonModule,
    AppRoutingModule
  ],
  exports: importedComponents
})
export class UtilsModule { }
