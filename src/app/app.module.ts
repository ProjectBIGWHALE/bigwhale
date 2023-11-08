import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
//modulos de importações
import { DesignModule } from './pages/design/design.module';
import { SecurityModule } from './pages/security/security.module';
import { DocumentsModule } from './pages/documents/documents.module';

import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { MenuBarComponent } from './pages/home/menu-bar/menu-bar.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';

import { DesignComponent } from './pages/design/design.component';
import { DocumentsComponent } from './pages/documents/documents.component';
import { SecurityComponent } from './pages/security/security.component';
import { MenuLogoComponent } from './pages/home/menu-logo/menu-logo.component';
import { MenuCarrosselComponent } from './components/menu-carrossel/menu-carrossel.component';
import { MenuAboutComponent } from './pages/home/menu-about/menu-about.component';
import { CollaboratorComponent } from './components/collaborator/collaborator.component';
import { CardComponent } from './components/card/card.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MenuBarComponent,
    NotFoundComponent,
    DesignComponent,
    DocumentsComponent,
    SecurityComponent,
    MenuLogoComponent,
    MenuCarrosselComponent,
    MenuAboutComponent,
    CollaboratorComponent,
    CardComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    DocumentsModule,
    DesignModule,
    SecurityModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
