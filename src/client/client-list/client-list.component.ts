import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Client } from '../model/Client';
import { MatDialog } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { ClientEditComponent } from '../client-edit/client-edit.component';
import { DialogMessageComponent } from '../../core/dialog-message/dialog-message.component';
import { AlertService } from '../../core/alerts';
import { DialogMessageService } from '../../core/dialog-message/dialog-message-service';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    CommonModule
  ],
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.scss'
})
export class ClientListComponent {
  dataSource = new MatTableDataSource<Client>();
  displayedColumns: string[] = ['id', 'name', 'action'];

  constructor(
    private clientService: ClientService,
    private dialog: MatDialog,
    private alertService: AlertService,
    private errDialogService: DialogMessageService
  ) { }

  ngOnInit(): void {
    this.clientService.getClients().subscribe(
      (clients) => {
        this.dataSource.data = clients;
      }
    );
  }

  createClient() {
    const dialogRef = this.dialog.open(ClientEditComponent, {
      data: {},
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.ngOnInit();
    });
  }

  editClient(client: Client) {
    const dialogRef = this.dialog.open(ClientEditComponent, {
      data: { client }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(result)
      this.ngOnInit();
    });
  }

  deleteClient(client: Client) {
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      data: {
        title: "Eliminar cliente",
        description: "Atención, si borra el cliente se perderá sus datos.<br> ¿Desea eliminar el cliente?"
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.clientService.deleteClient(client.id).subscribe({
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
