import { ComponentFixture, TestBed } from '@angular/core/testing';

import { buttonsHomeHelpComponent } from './buttons-home-help.component';

describe('ButtonsComponent', () => {
  let component: buttonsHomeHelpComponent;
  let fixture: ComponentFixture<buttonsHomeHelpComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [buttonsHomeHelpComponent]
    });
    fixture = TestBed.createComponent(buttonsHomeHelpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
