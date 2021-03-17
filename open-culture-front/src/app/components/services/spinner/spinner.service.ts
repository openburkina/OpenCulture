import { Injectable } from '@angular/core';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {SpinnerComponent} from '../../spinner/spinner.component';

@Injectable({
  providedIn: 'root'
})
export class SpinnerService {
    private myModal: NgbModalRef;
    constructor(
        private modal: NgbModal,
    ) { }

    public loading(): void {
        this.myModal = this.modal.open(SpinnerComponent, {backdrop: 'static', centered: true, container: 'body'});
    }

    public close(): void {
        // console.log(this.modal.hasOpenModals());
        this.myModal.dismiss();
        // this.modal.dismissAll();
        // this.myModal.close();
    }

    public dismissAll(): void {
        this.modal.dismissAll();
    }
}
