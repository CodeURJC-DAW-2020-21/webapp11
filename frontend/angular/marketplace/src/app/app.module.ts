import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { RouterModule, Routes } from '@angular/router';
import { AboutComponent } from './components/about/about.component';
import { LegalComponent } from './components/legal/legal.component';
import { FaqComponent } from './components/faq/faq.component';
import { PricingComponent } from './components/pricing/pricing.component';
import { ScreenshotsComponent } from './components/screenshots/screenshots.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ErrorComponent } from './components/error/error.component';
import { ServicesComponent } from './components/services/services.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ServiceComponent } from './components/service/service.component';
import { PanelComponent } from './components/panel/panel.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CategoryPurchasesChartComponent } from './components/panel/category-purchases-chart/category-purchases-chart.component';
import { ChartsModule } from 'ng2-charts';
import { WeeklySalesChartComponent } from './components/panel/weekly-sales-chart/weekly-sales-chart.component';

/*
 * Routing to the correspondent components
 */
const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'about', component: AboutComponent },
  { path: 'legal', component: LegalComponent },
  { path: 'faq', component: FaqComponent },
  { path: 'pricing', component: PricingComponent },
  { path: 'screenshots', component: ScreenshotsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'error', component: ErrorComponent },
  { path: 'services', component: ServicesComponent },
  { path: 'service', component: ServiceComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'panel', component: PanelComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    AboutComponent,
    LegalComponent,
    FaqComponent,
    PricingComponent,
    ScreenshotsComponent,
    LoginComponent,
    RegisterComponent,
    ErrorComponent,
    ServicesComponent,
    ProfileComponent,
    ServiceComponent,
    PanelComponent,
    CategoryPurchasesChartComponent,
    WeeklySalesChartComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(routes),
    FormsModule,
    HttpClientModule,
    ChartsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
