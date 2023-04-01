import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'certificateHierarchyLevel', standalone: true})
export class CertificateHierarchyLevelPipe implements PipeTransform {
  private transformations = ['Root', 'First Intermediate', 'Second Intermediate'];
  transform(value: number | undefined): string {
    if (value) return this.transformations[value - 1];
    else return 'Second Intermediate';
  }
}
