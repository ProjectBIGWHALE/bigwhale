import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CryptographComponent } from './cryptograph.component';

describe('CryptographComponent', () => {
  let component: CryptographComponent;
  let fixture: ComponentFixture<CryptographComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CryptographComponent]
    });
    fixture = TestBed.createComponent(CryptographComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
