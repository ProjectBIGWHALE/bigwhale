import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageconverterComponent } from './imageconverter.component';

describe('ImageconverterComponent', () => {
  let component: ImageconverterComponent;
  let fixture: ComponentFixture<ImageconverterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImageconverterComponent]
    });
    fixture = TestBed.createComponent(ImageconverterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
