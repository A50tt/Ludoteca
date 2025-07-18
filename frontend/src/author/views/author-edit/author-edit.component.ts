import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AuthorService } from '../../author.service';
import { Author } from '../../model/Author';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AlertService } from '../../../core/alerts';
import { DialogMessageService } from '../../../core/dialog-message/dialog-message-service';

@Component({
    selector: 'app-author-edit',
    standalone: true,
    imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatDialogModule],
    templateUrl: './author-edit.component.html',
    styleUrl: './author-edit.component.scss',
})
export class AuthorEditComponent implements OnInit {
    author!: Author;

    constructor(
        public dialogRef: MatDialogRef<AuthorEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private authorService: AuthorService,
        private errDialogService: DialogMessageService,
        private alertService: AlertService,
    ) { }

    ngOnInit(): void {
        this.author = this.data.author ? Object.assign({}, this.data.author) : new Author();
    }

    onSave() {
        this.authorService.saveAuthor(this.author).subscribe({
            next: (result) => {
                this.alertService.success(result.extendedMessage);
                this.dialogRef.close();
            },
            error: (err) => {
                this.errDialogService.openMsgErrorDialog(err.error.message, err.error.extendedMessage);
            }
        });
    }

    onClose() {
        this.dialogRef.close();
    }
}