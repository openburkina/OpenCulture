import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    animations: [
        trigger('fadeIn', [
            state('in', style({ opacity: 0.4 })),
            transition(':enter', [
                style({ opacity: 0 }),
                animate(300),
            ]),
            transition(':leave',
            animate(200, style({ opacity: 0 }))),
        ]),
    ],

})
export class SpinnerComponent implements OnInit {

  constructor(
  ) { }

  ngOnInit(): void {
  }

}
