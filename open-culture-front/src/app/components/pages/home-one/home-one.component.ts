import { Component, OnInit } from '@angular/core';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { TypeFichier } from '../../models/enumeration/type-fichier.enum';
import { OeuvreService } from '../oeuvre/oeuvre.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SignInComponent } from '../sign-in/sign-in.component';

@Component({
  selector: 'app-home-one',
  templateUrl: './home-one.component.html',
  styleUrls: ['./home-one.component.scss']
})
export class HomeOneComponent implements OnInit {

  oeuvres:  OeuvreDTO[];
  oeuvresVideo =  new Array();
  oeuvresAudio =  new Array();
  oeuvresView = new Array();
  typeFichier: TypeFichier;

  constructor(protected oeuvreService: OeuvreService,private modal: NgbModal,) {
    this.typeFichier = TypeFichier.VIDEO
   }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.oeuvreService.findAll(this.typeFichier).subscribe(
      response => {
          this.oeuvres = response.body;
          this.oeuvreService.forRowView(this.oeuvres,this.typeFichier,this.oeuvres,this.oeuvresVideo,this.oeuvresAudio);
          console.log(this.oeuvres);
      }
    );
  }
  openSignin(): void {
    const currentModal = this.modal.open(SignInComponent, {container: 'body', size: 'lg', centered: true});
}
}
