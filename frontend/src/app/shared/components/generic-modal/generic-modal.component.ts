import { Component, Input } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-generic-modal',
  templateUrl: './generic-modal.component.html',
  styleUrls: ['./generic-modal.component.scss']
})
export class GenericModalComponent {

  @Input()
  contentMessage!: string;

  @Input()
  redirectLink?: string;

  @Input()
  showCloseButton: boolean = false;

  constructor(
    private dialogRef: MatDialogRef<GenericModalComponent>,
    private router: Router
  ){}

  protected redirect(): void{
    this.router.navigate([this.redirectLink])
      .then(() => this.dialogRef.close());
  }

}
