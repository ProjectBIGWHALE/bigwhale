import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from 'src/app/app-routing.module';

import { CryptographComponent } from './cryptograph/cryptograph.component';
import { QrcodegeneratorComponent } from './qrcodegenerator/qrcodegenerator.component';


const importedComponents = [
  CryptographComponent,
  QrcodegeneratorComponent
]

@NgModule({
  declarations: importedComponents,
  imports: [
    CommonModule,
    AppRoutingModule,

  ],
  exports: importedComponents
})
export class SecurityModule { }
