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
    ChangePasswordComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NgxWebstorageModule.forRoot(),
    FormsModule,
    ReactiveFormsModule,
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
