import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from 'src/app/app-routing.module';

import { CertificategeneratorComponent } from './certificategenerator/certificategenerator.component';
import { CompactconverterComponent } from './compactconverter/compactconverter.component';
import { ImageconverterComponent } from './imageconverter/imageconverter.component';

const importedComponents = [
  CertificategeneratorComponent,
  CompactconverterComponent,
  ImageconverterComponent
]

@NgModule({
  declarations: importedComponents,
  imports: [
    CommonModule,
    AppRoutingModule,

  ],
  exports: importedComponents
})
export class DocumentsModule { }
