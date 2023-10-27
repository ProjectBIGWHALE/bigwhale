import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

//modulos de importações
import { DocumentsModule } from './pages/documents/documents.module';
import { DesignModule } from './pages/design/design.module';
import { SecurityModule } from './pages/security/security.module';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { MenuBarComponent } from './pages/home/menu-bar/menu-bar.component';
import { UtilsComponent } from './components/utils/utils.component';
import { ButtonsComponent } from './components/utils/buttons/buttons.component';
import { LinesComponent } from './components/utils/lines/lines.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';

import { DesignComponent } from './pages/design/design.component';
import { DocumentsComponent } from './pages/documents/documents.component';
import { SecurityComponent } from './pages/security/security.component';
import { MenuLogoComponent } from './pages/home/menu-logo/menu-logo.component';
import { MenuCarrosselComponent } from './components/menu-carrossel/menu-carrossel.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MenuBarComponent,
    UtilsComponent,
    ButtonsComponent,
    LinesComponent,
    NotFoundComponent,
    DesignComponent,
    DocumentsComponent,
    SecurityComponent,
    MenuLogoComponent,
    MenuCarrosselComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    DocumentsModule,
    DesignModule,
    SecurityModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
