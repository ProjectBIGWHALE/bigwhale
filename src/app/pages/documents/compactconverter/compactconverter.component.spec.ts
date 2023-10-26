import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompactconverterComponent } from './compactconverter.component';

describe('CompactconverterComponent', () => {
  let component: CompactconverterComponent;
  let fixture: ComponentFixture<CompactconverterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompactconverterComponent]
    });
    fixture = TestBed.createComponent(CompactconverterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
