import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AltercolorComponent } from './altercolor.component';

describe('AltercolorComponent', () => {
  let component: AltercolorComponent;
  let fixture: ComponentFixture<AltercolorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AltercolorComponent]
    });
    fixture = TestBed.createComponent(AltercolorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
