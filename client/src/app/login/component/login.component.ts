import { Component, OnInit } from '@angular/core';
import { LoginRequestModel } from '../../core/session/login-request.model';
import { LoginResponseModel } from '../../core/session/login-response.model';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';

/**
 * Generates a login component in order to allow users to login into the system
 */
@Component({
  selector: 'gms-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  /**
   * User's username or email used for login.
   */
  usernameOrEmail: string;

  /**
   * User's password used for login.
   */
  password: string;

  /**
   * Whether the credentials should be stored or not.
   */
  rememberMe = true;

  /**
   * Component constructor
   * @param loginService LoginService for handling the login API requests.
   * @param router Router module in order to perform navigation.
   */
  constructor(private loginService: LoginService, private router: Router) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() { }

  /**
   * Performs a login request using the inputs values the user has typed in as username/email and password.
   */
  login(): void {
    const payload: LoginRequestModel = { usernameOrEmail: this.usernameOrEmail, password: this.password};
    const ls = this.loginService.login(payload).subscribe((response: LoginResponseModel) => {
      ls.unsubscribe();
      this.router.navigateByUrl('home');
    });
  }

}