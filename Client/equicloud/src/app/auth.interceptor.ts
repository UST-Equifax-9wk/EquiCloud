import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {


  // const loginUrl = 'http://localhost:8080/auth/login'; 
  // const registerUrl = 'http://localhost:8080/auth/register'
  // if (req.url === loginUrl ||
  //   req.url === registerUrl ) {
  //   return next(req);
  // }
  // const token = localStorage.getItem('jwtToken');

  // console.log("inside interceptor, token = " + token)
  // if (token) {
  //   const authReq = req.clone({
  //     setHeaders: { Authorization: `Bearer ${token}` }
  //   });

  //   return next(authReq);
  // }

  return next(req);
};


