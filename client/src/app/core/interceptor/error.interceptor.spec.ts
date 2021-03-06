import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { ErrorInterceptor } from './error.interceptor';
import { InterceptorHelperService } from './interceptor-helper.service';
import { HttpStatusCode } from '../response/http-status-code.enum';
import { NotificationService } from '../messages/notification.service';

describe('ErrorInterceptor', () => {
  let spyIsExcludedFromErrorHandling: jasmine.Spy;
  let spyMessageServiceError: jasmine.Spy;

  const url = 'sample-url-nk-9fj92md9';
  const errMock = { error: 'error message', message: 'body message', status: 500 };
  const httpErr: HttpErrorResponse = new HttpErrorResponse({
    error: { error: errMock.error, message: errMock.message }, status: errMock.status, statusText: 'Server Error', url: url
  });
  const spy = { isExcludedFromErrorHandling: () => {}, err: (a, b) => {} };
  let isExcludedFromErrorHandlingReal = () => false;
  const intHelperServiceStub = {
    isExcludedFromErrorHandling: (): boolean => {
      spy.isExcludedFromErrorHandling(); return isExcludedFromErrorHandlingReal();
    }
  };
  const notificationService = { error: (a, b) => { spy.err(a, b); } };

  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    isExcludedFromErrorHandlingReal = () => false;
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [
        ErrorInterceptor,
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
        { provide: NotificationService, useValue: notificationService },
        { provide: InterceptorHelperService, useValue: intHelperServiceStub }
      ]
    });
    httpTestingController = TestBed.get(HttpTestingController);
    httpClient = TestBed.get(HttpClient);

    spyIsExcludedFromErrorHandling = spyOn(spy, 'isExcludedFromErrorHandling');
    spyMessageServiceError = spyOn(spy, 'err');
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be created', inject([ErrorInterceptor], (service: ErrorInterceptor) => {
    expect(service).toBeTruthy();
  }));

  it('should do nothing when there is no error in the response', () => {
    httpClient.get(url).subscribe((response: any) => {
      expect(response).toBeTruthy();
      expect(spyIsExcludedFromErrorHandling).not.toHaveBeenCalled();
      expect(spyMessageServiceError).not.toHaveBeenCalled();
    });
    httpTestingController.expectOne(url).flush({ data: 'ok' });
  });

  it('should do nothing when InterceptorHelperService#isExcludedFromErrorHandling returns `true`',
    () => {
      isExcludedFromErrorHandlingReal = () => true;
      httpClient.get(url).subscribe(() => {}, (error) => {
        expect(error).toBeTruthy();
        expect(spyIsExcludedFromErrorHandling).toHaveBeenCalled();
        expect(spyMessageServiceError).not.toHaveBeenCalled();
      });
      // flush response with an HttpErrorResponse in order to meet second condition: event instanceof HttpErrorResponse
      httpTestingController.expectOne(url).flush(errMock, httpErr);
    });

  it('should do nothing when `event instanceof HttpErrorResponse` is `false`',
    () => {
      httpClient.get(url).subscribe(() => {}, (error) => {
        expect(error).toBeTruthy();
        expect(spyIsExcludedFromErrorHandling).toHaveBeenCalled();
        expect(spyMessageServiceError).not.toHaveBeenCalled();
      });
      /*
      flush response with something different from and HttpErrorResponse in order to NOT meet
      second condition: event instanceof HttpErrorResponse
      */
      const errRes: HttpResponse<any> = new HttpResponse({ status: 204, statusText: 'Another Error', url: url });
      httpTestingController.expectOne(url).flush(errRes);
    });

  it('should show as `title` "Error" as default and an empty string as `message`', () => {
    httpClient.get(url).subscribe(() => {}, (error) => {
      expect(error).toBeTruthy();
      expect(spyIsExcludedFromErrorHandling).toHaveBeenCalled();
      expect(spyMessageServiceError).toHaveBeenCalled();
      expect(spyMessageServiceError.calls.first().args[0]).toBe('');
      expect(spyMessageServiceError.calls.first().args[1]).toBe('Error');
    });
    // flush response with an HttpErrorResponse in order to meet second condition: event instanceof HttpErrorResponse
    const copyHttpErr: HttpErrorResponse = new HttpErrorResponse({status: errMock.status, statusText: 'Server Error', url: url});

    httpTestingController.expectOne(url).flush({}, copyHttpErr);
  });

  it('should show as `title` "Unauthorized" when status is "Unauthorized"', () => {
    httpClient.get(url).subscribe(() => {}, (error) => {
      expect(spyMessageServiceError.calls.first().args[1]).toBe('Unauthorized');
    });
    // flush response with an HttpErrorResponse in order to meet second condition: event instanceof HttpErrorResponse
    const copyHttpErr: HttpErrorResponse = new HttpErrorResponse({
      status: HttpStatusCode.UNAUTHORIZED, statusText: 'Server Error', url: url
    });

    httpTestingController.expectOne(url).flush({}, copyHttpErr);
  });

  it('should show as title `Error` as default and add `title` and message if error object is present with ' +
    'both values inside it', () => {
    httpClient.get(url).subscribe(() => {}, (error) => {
      expect(error).toBeTruthy();
      expect(spyIsExcludedFromErrorHandling).toHaveBeenCalled();
      expect(spyMessageServiceError).toHaveBeenCalled();
      expect(spyMessageServiceError.calls.first().args[0]).toBe(errMock.error + ': ' + errMock.message);
      expect(spyMessageServiceError.calls.first().args[1]).toBe('Error');
    });
    // flush response with an HttpErrorResponse in order to meet second condition: event instanceof HttpErrorResponse
    httpTestingController.expectOne(url).flush(errMock, httpErr);
  });
});
