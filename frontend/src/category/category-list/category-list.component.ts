import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Category } from '../model/Category';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CategoryService } from '../category.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CategoryEditComponent } from '../category-edit/category-edit.component';
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { DialogMessageComponent } from '../../core/dialog-message/dialog-message.component';
import { AlertService } from '../../core/alerts';
import { DialogMessageService } from '../../core/dialog-message/dialog-message-service';


@Component({
    selector: 'app-category-list',
    standalone: true,
    imports: [
        MatButtonModule,
        MatIconModule,
        MatTableModule,
        CommonModule,
        MatDialogModule
    ],
    templateUrl: './category-list.component.html',
    styleUrl: './category-list.component.scss'
})
export class CategoryListComponent implements OnInit {
    dataSource = new MatTableDataSource<Category>();
    displayedColumns: string[] = ['id', 'name', 'action'];

    constructor(
        private categoryService: CategoryService,
        private dialog: MatDialog,
        private alertService: AlertService,
        private errDialogService: DialogMessageService
    ) { }

    ngOnInit(): void {
        this.categoryService.getCategories().subscribe(
            (categories) => {
                this.dataSource.data = categories;
            }
        );
    }

    createCategory() {
        const dialogRef = this.dialog.open(CategoryEditComponent, {
            data: {}
        });

        dialogRef.afterClosed().subscribe(result => {
            this.ngOnInit();
        });
    }

    editCategory(category: Category) {
        const dialogRef = this.dialog.open(CategoryEditComponent, {
            data: { category }
        });

        dialogRef.afterClosed().subscribe(result => {
            this.ngOnInit();
        });
    }

    deleteCategory(category: Category) {
        const dialogRef = this.dialog.open(DialogConfirmationComponent, {
            data: { title: "Eliminar categoría", description: "Atención si borra la categoría se perderán sus datos.<br> ¿Desea eliminar la categoría?" }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.categoryService.deleteCategory(category.id).subscribe({
                    next: (result) => {
                        this.alertService.success(result.extendedMessage);
                        this.ngOnInit();
                    },
                    error: (err) => {
                        this.errDialogService.openMsgErrorDialog(err.error.message, err.error.extendedMessage);
                        this.ngOnInit();
                    }
                });
            }
        });
    }
}