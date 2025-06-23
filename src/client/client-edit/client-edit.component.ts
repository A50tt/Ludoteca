import { Component, Inject } from '@angular/core';
import { Client } from '../model/Client';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { DialogMessageComponent } from '../../core/dialog-message/dialog-message.component';


@Component({
  selector: 'app-client-edit',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule 
  ],
  templateUrl: './client-edit.component.html',
  styleUrl: './client-edit.component.scss'
})
export class ClientEditComponent {
  client!: Client;

  constructor(
          public dialogRef: MatDialogRef<ClientEditComponent>,
          @Inject(MAT_DIALOG_DATA) public data: {client : Client},
          private clientService: ClientService,
          public errDialog: MatDialog,
      ) {}

      ngOnInit(): void {
        this.client = this.data.client != null ? this.data.client : new Client();
        this.client = this.data.client ? Object.assign({}, this.data.client) : new Client();
      }

  onSave() {
    this.clientService.saveClient(this.client).subscribe((result) => {
      if(result) {
        this.dialogRef.close();
      } else {
        const dialogRef = this.errDialog.open(DialogMessageComponent, {
              data: { title: "No se ha podido guardar el cliente", description: "El nombre del cliente ya existe en la base de datos." }
            });
      }
    });
  }

  onClose() {
      this.dialogRef.close();
  }
}
