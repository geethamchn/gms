import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpResponseBase } from '@angular/common/http';

import { LoginRequestModel } from '../../core/session/login-request.model';
import { LoginService } from '../service/login.service';
import { SessionService } from '../../core/session/session.service';
import { FormHelperService } from '../../core/form/form-helper.service';
import { HttpStatusCode } from '../../core/response/http-status-code.enum';
import { NotificationService } from '../../core/messages/notification.service';
import { Subscription } from 'rxjs/internal/Subscription';

/**
 * Generates a login component in order to allow users to login into the system
 */
@Component({
  selector: 'gms-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  /**
   * Form for handling the login data inputted by the user.
   */
  loginForm: FormGroup;

  /**
   * Whether the login form was submitted or not.
   * @type {boolean}
   */
  submitted = false;

  /**
   * Subscription to the isLoggedIn Observable
   */
  ili: Subscription;

  /**
   * Component constructor
   * @param loginService LoginService for handling the login API requests.
   * @param sessionService SessionService for storing/retrieving session-related information.
   * @param router Router module in order to perform navigation.
   * @param fb FormBuilder A form builder for generating the login form.
   * @param formHelperService FormHelperService.
   * @param notificationService {NotificationService} Service for showing the messages.
   */
  constructor(private loginService: LoginService, private sessionService: SessionService, private router: Router,
              private fb: FormBuilder, private formHelperService: FormHelperService,
              private notificationService: NotificationService) {
    this.createLoginForm();
  }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.ili = this.sessionService.isLoggedIn().subscribe(logged => {
      if (logged) {
        this.router.navigateByUrl('home');
      }
    });
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    if (this.ili) { this.ili.unsubscribe(); }
  }

  /**
   * Performs a login request using the inputs values the user has typed in as username/email and password.
   */
  login(): void {
    const rm = this.formValueOf('rememberMe');
    if (this.loginForm.valid) {
      this.submitted = true;
      const payload: LoginRequestModel = {
        usernameOrEmail: this.formValueOf('usernameOrEmail'),
        password: this.formValueOf('password')
      };
      const ls = this.loginService.login(payload).subscribe(() => {
        ls.unsubscribe();
        this.sessionService.setRememberMe(rm);
        this.router.navigateByUrl('home');
      }, (response: HttpResponseBase) => {
        if (response.status === HttpStatusCode.UNAUTHORIZED) {
          this.notificationService.error('Wrong credentials', 'Login Failed');
        }
        this.loginForm.reset({rememberMe: rm});
        this.submitted = false;
      });
    } else {
      this.formHelperService.markFormElementsAsTouched(this.loginForm);
    }
  }

  /**
   * Creates the login (reactive) form.
   */
  private createLoginForm() {
    this.loginForm = this.fb.group({
      usernameOrEmail: [ null, Validators.required ],
      password: [ null, Validators.required ],
      rememberMe: true
    });
  }

  /**
   * Returns the value set for a form control in the login form.
   * @param {string} name
   * @returns {any}
   */
  private formValueOf(name: string): any {
    return this.loginForm.get(name).value;
  }

}
