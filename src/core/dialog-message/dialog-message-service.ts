import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DialogMessageComponent } from './dialog-message.component';

@Injectable({ providedIn: 'root' })
export class DialogMessageService {
    constructor(private dialog: MatDialog) {}

    openMsgErrorDialog(title: string, description: string): void {
        this.dialog.open(DialogMessageComponent, {
            data: {
                title: ("ERROR: " + title),
                description: description },
        });
    }
}