import React, { useState } from 'react';
import './LoginSignUp.css';
import App from './App';
import ReactDOM from 'react-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import user_icon from './assets/person.png';
import email_icon from './assets/email.png';
import pwd_icon from './assets/password.png';
import axios from 'axios';
import ForgotPassword from './ForgotPassword';

const LoginSignUp = () => {
  const [action,setAction] = useState("Login");
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [email, setEmail] = useState('');
  const [userNameError, setUserNameError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const TASKS_BASE_URL = "http://localhost:8080/api/v1/";

  const pwdValidation = ()=> {
    if (passwordConfirm === '') {
      toast.error("Please confirm your password!");
      return false;
    }
    if (passwordConfirm != password) {
      toast.error("Password is not matching!");
      return false;
    }
    return true;
  }
  const onSignUp = () => {
    if (!formValidation()) {
      return;
    }
    if (!pwdValidation()) {
      return;
    }
    let signUpRequest = {username: userName, password: Buffer.from(password).toString('base64'), email: email};
    try {
      axios.put(TASKS_BASE_URL + "auth/signup", signUpRequest);
      toast.success("Registration Successful!");
      setUserName('');
      setPassword('');
      setPasswordConfirm('');
      setEmail('');
      //window.location.reload();
    } catch (e) {
      toast.error(e);
    }
    
  }
  const formValidation = () => {
     // Check if the user has entered both fields correctly
     if ('' === userName) {
      setUserNameError('Please enter your username');
      toast.error("Please enter your username");
      return false;
    }
  
    if ('' === password) {
      setPasswordError('Please enter a password');
      toast.error("Please enter a password");
      return false;
    }
  
    if (password.length < 7) {
      setPasswordError('The password must be 8 characters or longer');
      toast.error("The password must be 8 characters or longer");
      return false;
    }
    return true;
  }
  const forgotPwd = () => {
    setTimeout(() => {
      ReactDOM.render(
        <React.StrictMode>
          <ForgotPassword />
        </React.StrictMode>,
        document.getElementById('root')
      );
    }, 200);
  }
  const onButtonClick = () => {
    // Set initial error values to empty
    setUserNameError('')
    setPasswordError('')
    if (!formValidation()) {
      return;
    }
   
    //Login API call
    const loginData  ={username: userName, password: Buffer.from(password).toString('base64')};
    fetch(`${TASKS_BASE_URL}auth/login`, {
      method: 'POST',
      body: JSON.stringify(loginData),
      headers: {
        'Content-Type': 'application/json'
      }
    }).then( res => {
      if (!res.ok) {
        toast.error("Bad Credentials!");
        return;
      }
      sessionStorage.setItem("X-AUTH-TOKEN", res.headers.get("x-auth-token"));
          setTimeout(() => {
            ReactDOM.render(
              <React.StrictMode>
                <App />
              </React.StrictMode>,
              document.getElementById('root')
            );
          }, 200);
       
        //Set Auth Token in session storage
        
         
    });
    
  }
  return (
  <div className="container">
    <div className='header'>
      <div className='text'>{action}</div>
      <div className='underline'></div>
    </div>
    <div className="inputs">
      <div className="input">
        <img src={user_icon} alt=""/>
        <input type="text" value={userName} onChange={(ev) => setUserName(ev.target.value)} placeholder='User Name'/>
        
      </div>
      {action==="Sign Up"?<div className="input"><img src={email_icon} alt=""/><input type="email" value={email} onChange={(ev) => setEmail(ev.target.value)} placeholder='Email Id'/></div>:<div></div>}
      
      <div className="input">
        <img src={pwd_icon} alt=""/>
        <input type="password" value={password} onChange={(ev) => setPassword(ev.target.value)} placeholder='Password'/>
      </div>
      {action==="Sign Up"?<div className="input"><img src={pwd_icon} alt=""/><input type="password" value={passwordConfirm} onChange={(ev) => setPasswordConfirm(ev.target.value)} placeholder='Confirm Password'/></div>:<div></div>}
      </div>
      {action==="Login"?<div className="forgot-password">Forgot password? <span onClick={()=>{forgotPwd()}}>Click Here!</span></div>:<div></div>}
      <div className='submit-container'>
        <div className={action==="Login"?"submit gray":"submit"} onClick={()=>{ if (action ==="Sign Up") {onSignUp()} setAction("Sign Up");}}>Sign Up</div>
        <div className={action==="Sign Up"?"submit gray":"submit"} onClick={()=>{if (action ==="Login") {onButtonClick()} setAction("Login");}}>Login</div>
      </div>
      <div>
      <ToastContainer position="bottom-right" />
      </div>
  </div>
  )
}

export default LoginSignUp;