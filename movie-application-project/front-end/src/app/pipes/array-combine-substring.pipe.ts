import { Pipe, PipeTransform } from '@angular/core';
import { Role } from '../service/admin.service';

@Pipe({
  name: 'arrayCombineSubstring'
})
export class ArrayCombineSubstringPipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    let rolesString = (value as Role[]).map(i => i.name.substring(5)).reduce((t, i) => t = t + i + ',', '');
    return rolesString.substring(0, rolesString.length - 1);
  
  }

}
