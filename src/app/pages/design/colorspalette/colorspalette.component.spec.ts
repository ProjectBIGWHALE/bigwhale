import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ColorspaletteComponent } from './colorspalette.component';

describe('ColorspaletteComponent', () => {
  let component: ColorspaletteComponent;
  let fixture: ComponentFixture<ColorspaletteComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ColorspaletteComponent]
    });
    fixture = TestBed.createComponent(ColorspaletteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
