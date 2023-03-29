import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CsrComponent } from './csr.component';

describe('CsrComponent', () => {
  let component: CsrComponent;
  let fixture: ComponentFixture<CsrComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CsrComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CsrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
