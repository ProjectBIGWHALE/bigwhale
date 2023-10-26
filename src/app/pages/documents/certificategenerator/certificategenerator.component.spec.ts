import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificategeneratorComponent } from './certificategenerator.component';

describe('CertificategeneratorComponent', () => {
  let component: CertificategeneratorComponent;
  let fixture: ComponentFixture<CertificategeneratorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CertificategeneratorComponent]
    });
    fixture = TestBed.createComponent(CertificategeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
