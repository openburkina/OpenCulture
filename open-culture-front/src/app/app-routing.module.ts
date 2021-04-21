import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AboutComponent } from './components/pages/about/about.component';
import { AppleNowComponent } from './components/pages/apple-now/apple-now.component';
import { BlogDetailsComponent } from './components/pages/blog-details/blog-details.component';
import { BlogStyleOneComponent } from './components/pages/blog-style-one/blog-style-one.component';
import { BlogStyleTwoComponent } from './components/pages/blog-style-two/blog-style-two.component';
import { CaseStudyComponent } from './components/pages/case-study/case-study.component';
import { ComingSoonComponent } from './components/pages/coming-soon/coming-soon.component';
import { ContactComponent } from './components/pages/contact/contact.component';
import { ErrorComponent } from './components/pages/error/error.component';
import { FaqComponent } from './components/pages/faq/faq.component';
import { HomeOneComponent } from './components/pages/home-one/home-one.component';
import { HomeThreeComponent } from './components/pages/home-three/home-three.component';
import { HomeTwoComponent } from './components/pages/home-two/home-two.component';
import { PrivacyPolicyComponent } from './components/pages/privacy-policy/privacy-policy.component';
import { ProjectsDetailsComponent } from './components/pages/projects-details/projects-details.component';
import { ProjectsComponent } from './components/pages/projects/projects.component';
import { ServicesDetailsComponent } from './components/pages/services-details/services-details.component';
import { ServicesStyleOneComponent } from './components/pages/services-style-one/services-style-one.component';
import { ServicesStyleTwoComponent } from './components/pages/services-style-two/services-style-two.component';
import { SignInComponent } from './components/pages/sign-in/sign-in.component';
import { SignUpComponent } from './components/pages/sign-up/sign-up.component';
import { TeamComponent } from './components/pages/team/team.component';
import { TermsConditionsComponent } from './components/pages/terms-conditions/terms-conditions.component';
import { TestimonialsComponent } from './components/pages/testimonials/testimonials.component';
import {AccountComponent} from './components/pages/account/account.component';
import {OeuvreComponent} from './components/pages/oeuvre/oeuvre.component';
import { ArtisteComponent } from './components/pages/artiste/artiste.component';
import { OeuvreEditComponent } from './components/pages/oeuvre/oeuvre-edit.component';
import { RegroupementComponent } from './components/pages/regroupement/regroupement.component';
import { TypeOeuvreComponent } from './components/pages/type-oeuvre/type-oeuvre.component';
import { OeuvreAfficheComponent } from './components/pages/oeuvre/oeuvre-affiche.component';

const routes: Routes = [
    {path: '', component: HomeOneComponent},
    {path: 'type-oeuvres', component: TypeOeuvreComponent},
    {path: 'oeuvres', component: OeuvreAfficheComponent},
    {path: 'oeuvres-client', component: OeuvreComponent},
    {path: 'regroupements', component: RegroupementComponent},
    {path: 'artistes', component: ArtisteComponent},
    {path: 'home-three', component: HomeThreeComponent},
    {path: 'home-two', component: HomeTwoComponent},
    {path: 'about', component: AboutComponent},
    {path: 'team', component: TeamComponent},
    {path: 'apply-now', component: AppleNowComponent},
    {path: 'projects', component: ProjectsComponent},
    {path: 'projects-details', component: ProjectsDetailsComponent},
    {path: 'case-study', component: CaseStudyComponent},
    {path: 'faq', component: FaqComponent},
    {path: 'testimonials', component: TestimonialsComponent},
    {path: 'error', component: ErrorComponent},
    {path: 'sign-in', component: SignInComponent},
    {path: 'sign-up', component: SignUpComponent},
    {path: 'terms-conditions', component: TermsConditionsComponent},
    {path: 'privacy-policy', component: PrivacyPolicyComponent},
    {path: 'coming-soon', component: ComingSoonComponent},
    {path: 'services-1', component: ServicesStyleOneComponent},
    {path: 'services-2', component: ServicesStyleTwoComponent},
    {path: 'services-details', component: ServicesDetailsComponent},
    {path: 'blog-1', component: BlogStyleOneComponent},
    {path: 'blog-2', component: BlogStyleTwoComponent},
    {path: 'blog-details', component: BlogDetailsComponent},
    {path: 'contact', component: ContactComponent},
    {path: 'account', component: AccountComponent},
    {path: 'signin', component: SignInComponent},


    {path: '**', component: ErrorComponent} // This line will remain down from the whole component list
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
