import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
    selector: 'app-dialog-message',
    standalone: true,
    imports: [MatButtonModule],
    templateUrl: './dialog-message.component.html',
    styleUrl: './dialog-message.component.scss',
})
export class DialogMessageComponent {
    title!: string;
    description!: string;

    constructor(
        public dialogRef: MatDialogRef<DialogMessageComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {}

    ngOnInit(): void {
        if (this.data.title) {
            this.title = this.data.title; // Solo sobrescribe si `data.title` está definido
        }
        this.description = this.data.description;
    }

    onClose(value = false) {
        this.dialogRef.close(value);
    }
}