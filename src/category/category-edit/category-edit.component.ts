import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from '../category.service';
import { Category } from '../model/Category';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { NgIf } from '@angular/common';
import { AlertService } from '../../core/alerts';
import { DialogMessageComponent } from '../../core/dialog-message/dialog-message.component';

@Component({
    selector: 'app-category-edit',
    standalone: true,
    imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
    templateUrl: './category-edit.component.html',
    styleUrl: './category-edit.component.scss'
})
export class CategoryEditComponent implements OnInit {
    category!: Category

    constructor(
        public dialogRef: MatDialogRef<CategoryEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: {category : Category},
        private categoryService: CategoryService,
            private alertService: AlertService,
            private errDialog: MatDialog
    ) {}

    ngOnInit(): void {
        this.category = this.data.category != null ? this.data.category : new Category();
        this.category = this.data.category ? Object.assign({}, this.data.category) : new Category();
    }

    onSave() {
        this.categoryService.saveCategory(this.category).subscribe({
              next: (result) => {
                this.alertService.success('Categoría registrada correctamente.');
                this.dialogRef.close();
              },
              error: (err) => {
                console.log(err);
                this.errDialog.open(DialogMessageComponent, {
                  data: { description: err.error.extendedMessage }
                });
              }
            });
    }

    onClose() {
        this.dialogRef.close();
    }
}