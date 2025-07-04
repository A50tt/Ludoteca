import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from '../../category.service';
import { Category } from '../../model/Category';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AlertService } from '../../../core/alerts';
import { DialogMessageService } from '../../../core/dialog-message/dialog-message-service';

@Component({
  selector: 'app-category-edit',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatDialogModule],
  templateUrl: './category-edit.component.html',
  styleUrl: './category-edit.component.scss',
  providers: []
})
export class CategoryEditComponent implements OnInit {
  category!: Category

  constructor(
    public dialogRef: MatDialogRef<CategoryEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { category: Category },
    private categoryService: CategoryService,
    private alertService: AlertService,
    private errDialogService: DialogMessageService,
  ) { }

  ngOnInit(): void {
    this.category = this.data.category != null ? this.data.category : new Category();
    this.category = this.data.category ? Object.assign({}, this.data.category) : new Category();
  }

  onSave() {
    this.categoryService.saveCategory(this.category).subscribe({
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