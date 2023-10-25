import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { MenuBarComponent } from './components/menu-bar/menu-bar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DesignComponent } from './pages/design/design.component';
import { DocumentosComponent } from './pages/documentos/documentos.component';
import { SegurancaComponent } from './pages/seguranca/seguranca.component';
import { UtilsComponent } from './components/utils/utils.component';
import { ColorspaletteComponent } from './pages/design/colorspalette/colorspalette.component';
import { AltercolorComponent } from './pages/design/altercolor/altercolor.component';
import { ButtonsComponent } from './components/utils/buttons/buttons.component';
import { LinesComponent } from './components/utils/lines/lines.component';
import { MatIcon } from '@angular/material/icon';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MenuBarComponent,
    DesignComponent,
    DocumentosComponent,
    SegurancaComponent,
    UtilsComponent,
    ColorspaletteComponent,
    AltercolorComponent,
    ButtonsComponent,
    LinesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
