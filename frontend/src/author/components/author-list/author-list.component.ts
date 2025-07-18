import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { AuthorEditComponent } from '../../views/author-edit/author-edit.component';
import { AuthorService } from '../../author.service';
import { Author } from '../../model/Author';
import { Pageable } from '../../../core/model/page/Pageable';
import { DialogConfirmationComponent } from '../../../core/dialog-confirmation/dialog-confirmation.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AlertService } from '../../../core/alerts';
import { DialogMessageService } from '../../../core/dialog-message/dialog-message-service';

@Component({
    selector: 'app-author-list',
    standalone: true,
    imports: [MatButtonModule, MatIconModule, MatTableModule, CommonModule, MatPaginatorModule, MatDialogModule],
    templateUrl: './author-list.component.html',
    styleUrl: './author-list.component.scss',
    providers: []
})
export class AuthorListComponent implements OnInit {
    pageNumber: number = 0;
    pageSize: number = 5;
    totalElements: number = 0;

    dataSource = new MatTableDataSource<Author>();
    displayedColumns: string[] = ['id', 'name', 'nationality', 'action'];

    constructor(
        private authorService: AuthorService,
        private dialog: MatDialog,
        private alertService: AlertService,
        private errDialogService: DialogMessageService
    ) { }

    ngOnInit(): void {
        this.loadPage();
    }

    loadPage(event?: PageEvent) {
        const pageable: Pageable = {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            sort: [
                {
                    property: 'id',
                    direction: 'ASC',
                },
            ],
        };

        if (event != null) {
            pageable.pageSize = event.pageSize;
            pageable.pageNumber = event.pageIndex;
        }

        this.authorService.getAuthors(pageable).subscribe((data) => {
            this.dataSource.data = data.content;
            this.pageNumber = data.pageable.pageNumber;
            this.pageSize = data.pageable.pageSize;
            this.totalElements = data.totalElements;
        });
    }

    createAuthor() {
        const dialogRef = this.dialog.open(AuthorEditComponent, {
            data: {},
        });

        dialogRef.afterClosed().subscribe((result) => {
            this.ngOnInit();
        });
    }

    editAuthor(author: Author) {
        const dialogRef = this.dialog.open(AuthorEditComponent, {
            data: { author: author },
        });

        dialogRef.afterClosed().subscribe((result) => {
            this.ngOnInit();
        });
    }

    deleteAuthor(author: Author) {
        const dialogRef = this.dialog.open(DialogConfirmationComponent, {
            data: {
                title: 'Eliminar autor',
                description:
                    'Atención, si borra el autor se perderán sus datos.<br> ¿Desea eliminar el autor?',
            },
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.authorService.deleteAuthor(author.id).subscribe({
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