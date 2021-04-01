import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeOneComponent } from './components/pages/home-one/home-one.component';
import { HomeTwoComponent } from './components/pages/home-two/home-two.component';
import { HomeThreeComponent } from './components/pages/home-three/home-three.component';
import { PreloaderComponent } from './components/common/preloader/preloader.component';
import { FooterComponent } from './components/common/footer/footer.component';
import { NavbarComponent } from './components/common/navbar/navbar.component';
import { BlogStyleTwoComponent } from './components/pages/blog-style-two/blog-style-two.component';
import { BlogStyleOneComponent } from './components/pages/blog-style-one/blog-style-one.component';
import { BlogDetailsComponent } from './components/pages/blog-details/blog-details.component';
import { ServicesDetailsComponent } from './components/pages/services-details/services-details.component';
import { ServicesStyleTwoComponent } from './components/pages/services-style-two/services-style-two.component';
import { ServicesStyleOneComponent } from './components/pages/services-style-one/services-style-one.component';
import { ComingSoonComponent } from './components/pages/coming-soon/coming-soon.component';
import { PrivacyPolicyComponent } from './components/pages/privacy-policy/privacy-policy.component';
import { TermsConditionsComponent } from './components/pages/terms-conditions/terms-conditions.component';
import { SignUpComponent } from './components/pages/sign-up/sign-up.component';
import { SignInComponent } from './components/pages/sign-in/sign-in.component';
import { ErrorComponent } from './components/pages/error/error.component';
import { TestimonialsComponent } from './components/pages/testimonials/testimonials.component';
import { FaqComponent } from './components/pages/faq/faq.component';
import { CaseStudyComponent } from './components/pages/case-study/case-study.component';
import { ProjectsDetailsComponent } from './components/pages/projects-details/projects-details.component';
import { ProjectsComponent } from './components/pages/projects/projects.component';
import { AppleNowComponent } from './components/pages/apple-now/apple-now.component';
import { TeamComponent } from './components/pages/team/team.component';
import { AboutComponent } from './components/pages/about/about.component';
import { ContactComponent } from './components/pages/contact/contact.component';
import { AccountComponent } from './components/pages/account/account.component';
import { SpinnerComponent } from './components/spinner/spinner.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {InterceptorService} from './components/services/interceptor/interceptor.service';
import {NgxWebstorageModule} from 'ngx-webstorage';
import { NgxSpinnerModule } from 'ngx-spinner';
import { OeuvreComponent } from './components/pages/oeuvre/oeuvre.component';
import { ChangePasswordComponent } from './components/pages/change-password/change-password.component';
import { AbonnementComponent } from './components/pages/abonnement/abonnement.component';
import { OeuvreEditComponent } from './components/pages/oeuvre/oeuvre-edit.component';
import { NotifierModule, NotifierOptions } from "angular-notifier";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { RegroupementComponent } from './components/pages/regroupement/regroupement.component';
import { TypeOeuvreComponent } from './components/pages/type-oeuvre/type-oeuvre.component';
import { ArtisteComponent } from './components/pages/artiste/artiste.component';
import { RegroupementDeleteComponent } from "./components/pages/regroupement/regroupement-delete.component";
import { TypeOeuvreDeleteComponent } from "./components/pages/type-oeuvre/type-oeuvre-delete.component";
import { OeuvreDeleteComponent } from "./components/pages/oeuvre/oeuvre-delete.component";
import { RegroupementEditComponent } from "./components/pages/regroupement/regroupement-edit.component";
import { TypeOeuvreEditComponent } from "./components/pages/type-oeuvre/type-oeuvre-edit.component";
import { ArtisteEditComponent } from "./components/pages/artiste/artiste-edit.component";
import { ArtisteDeleteComponent } from "./components/pages/artiste/artiste-delete.component";
import { OeuvreAfficheComponent } from "./components/pages/oeuvre/oeuvre-affiche.component";
const notifierDefaultOptions: NotifierOptions = {
  position: {
    horizontal: {
      position: 'right',
      distance: 12,
    },
    vertical: {
      position: 'top',
      distance: 12,
      gap: 10,
    },
  },
  theme: 'material',
  behaviour: {
    autoHide: 1000,
    onClick: false,
    onMouseover: 'pauseAutoHide',
    showDismissButton: true,
    stacking: 4,
  },
  animations: {
    enabled: true,
    show: {
      preset: 'slide',
      speed: 300,
      easing: 'ease',
    },
    hide: {
      preset: 'fade',
      speed: 300,
      easing: 'ease',
      offset: 50,
    },
    shift: {
      speed: 300,
      easing: 'ease',
    },
    overlap: 150,
  },
};

@NgModule({
  declarations: [
    AppComponent,
    HomeOneComponent,
    HomeTwoComponent,
    HomeThreeComponent,
    PreloaderComponent,
    FooterComponent,
    NavbarComponent,
    BlogStyleTwoComponent,
    BlogStyleOneComponent,
    BlogDetailsComponent,
    ServicesDetailsComponent,
    ServicesStyleTwoComponent,
    ServicesStyleOneComponent,
    ComingSoonComponent,
    PrivacyPolicyComponent,
    TermsConditionsComponent,
    SignUpComponent,
    SignInComponent,
    ErrorComponent,
    TestimonialsComponent,
    FaqComponent,
    CaseStudyComponent,
    ProjectsDetailsComponent,
    ProjectsComponent,
    AppleNowComponent,
    TeamComponent,
    AboutComponent,
    ContactComponent,
    AccountComponent,
    SpinnerComponent,
    OeuvreComponent,
    ChangePasswordComponent,
    AbonnementComponent,
    OeuvreEditComponent,
    RegroupementComponent,
    TypeOeuvreComponent,
    ArtisteComponent,
    RegroupementDeleteComponent,
    TypeOeuvreDeleteComponent,
    OeuvreDeleteComponent,
    RegroupementEditComponent,
    TypeOeuvreEditComponent,
    ArtisteEditComponent,
    ArtisteDeleteComponent,
    OeuvreAfficheComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NgxWebstorageModule.forRoot(),
    FormsModule,
    ReactiveFormsModule,
    NotifierModule.withConfig(notifierDefaultOptions),
    BrowserAnimationsModule,
    NgbDatepickerModule,
    NgxSpinnerModule
  ],    
  providers: [
      {
          provide: HTTP_INTERCEPTORS,
          useClass: InterceptorService,
          multi: true
      },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
