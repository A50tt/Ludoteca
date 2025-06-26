import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Category } from '../model/Category';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CategoryService } from '../category.service';
import { MatDialog } from '@angular/material/dialog';
import { CategoryEditComponent } from '../category-edit/category-edit.component';
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { DialogMessageComponent } from '../../core/dialog-message/dialog-message.component';
import { AlertService } from '../../core/alerts';


@Component({
    selector: 'app-category-list',
    standalone: true,
    imports: [
        MatButtonModule,
        MatIconModule,
        MatTableModule,
        CommonModule
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
        private alertService: AlertService
    ) { }

    ngOnInit(): void {
        this.categoryService.getCategories().subscribe(
            (categories) => {
                this.dataSource.data = categories;
                console.log(this.dataSource.data.toString());
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
                        console.log(result.message);
                        console.log(result.extendedMessage);
                        this.alertService.success('Categoría registrada.');
                        this.ngOnInit();
                    },
                    error: (err) => {
                        console.log(err);
                        this.dialog.open(DialogMessageComponent, {
                            data: { description: err.error.extendedMessage }
                        });
                        this.ngOnInit();
                    }
                });
            }
        });
    }
}