import {Component, OnInit} from '@angular/core';
import {OeuvreDTO} from '../../models/oeuvre.model';
import {OeuvreService} from './oeuvre.service';
import {TypeFichier} from '../../models/enumeration/type-fichier.enum';

@Component({
  selector: 'app-oeuvre',
  templateUrl: './oeuvre.component.html',
  styleUrls: ['./oeuvre.component.scss']
})
export class OeuvreComponent implements OnInit {
  oeuvres:  OeuvreDTO[];
  oeuvresVideo =  new Array();
  oeuvresAudio =  new Array();

  oeuvresView = new Array();
  typeFichier: TypeFichier;
  imagePath = [
      'assets/img/openculture/images-1.jpg',
      'assets/img/openculture/images-2.jpg',
      'assets/img/openculture/images-3.jpg',
      'assets/img/openculture/images-4.jpg',
      'assets/img/openculture/images-5.jpg',
      'assets/img/openculture/images-6.jpg',
      'assets/img/openculture/images-7.jpg',
      'assets/img/openculture/images-8.jpg',
      'assets/img/openculture/images-9.jpg',
      'assets/img/contact/contact-img.png',
  ];
  constructor(
    protected oeuvreService: OeuvreService,

   // protected ngModalService: NgbModal
    ) { this.typeFichier = TypeFichier.VIDEO }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.oeuvreService.findAll(this.typeFichier).subscribe(
      response => {
          this.oeuvres = response.body;
          this.forRowView(this.oeuvres,this.typeFichier);
          console.log(this.oeuvres);
      }
    );
  }

  create(): void{
  }

  delete(): void{
  }

  forRowView(tab: OeuvreDTO[],typeFichier: TypeFichier): void{
      const k = 3;
      for (let i = 0; i < tab.length; i += k ){
          tab[i].pathFile = this.imagePath[i];
      }
      for (let i = 0; i < tab.length; i += k ){
            if(typeFichier == TypeFichier.VIDEO)
                this.oeuvresVideo.push({items: this.oeuvres.slice(i,i+k)});
            else if (typeFichier == TypeFichier.AUDIO)
                this.oeuvresAudio.push({items: this.oeuvres.slice(i,i+k)});
      }
  }

}
